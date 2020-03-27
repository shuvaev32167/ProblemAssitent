package ru.crazypeppers.problemsassistant.data.dto

import android.util.Log
import ru.crazypeppers.problemsassistant.data.TAG
import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData
import ru.crazypeppers.problemsassistant.diffDay
import ru.crazypeppers.problemsassistant.roundTo
import java.util.*

/**
 * Описание карты (мотивации/якоря) для решения проблемы
 *
 * @property name Название карты
 * @property points Список очков
 */
class Card(
    var name: String,
    val points: List<Point>
) {
    /**
     * Тип карты
     */
    var type: CardType = CardType.NONE

    /**
     * Пояснение карты
     */
    var description: String = ""

    /**
     * Картинка карты
     */
    var imageBase64: String? = null

    init {
        for (point in points) {
            point.parent = this
        }
    }

    /**
     * @param cardName название карты
     * @param cardDescription пояснение карты
     * @param points список очков
     * @param parent родительская проблема [Problem] для данной карты
     */
    constructor(
        cardName: String,
        cardDescription: String,
        points: List<Point>,
        parent: Problem
    ) : this(
        cardName,
        points
    ) {
        this.description = cardDescription
        this.parent = parent
        calculateAvgPoints()
    }

    /**
     * Среднее зачение очков, привязанных к карте
     */
    var avgPoints: Float = 0f

    /**
     * Проблема, к которой относится карты
     */
    @Transient
    var parent: Problem? = null

    private fun calculateWeightElement(maxDay: Int, diffDay: Int, position: Int): Double {
        val weight = (100 + maxDay - diffDay / position.toDouble()) / position.toDouble()
        if (weight < 0) {
            throw IllegalArgumentException()
        }
        return weight
    }

    /**
     * Расчёт среднего значения очков. Расчитанное значение сохранится.
     * Для сброса расчитанного значения использовать метод [dischargeAvgPoints].
     *
     * @return Среднее значение очков
     */
    private fun calculateAvgPoints() {
        if (points.isEmpty()) {
            avgPoints = 0f
        } else {
            val lastRatingDate = points.first().cdate
            val maxDiffDay = points.last().cdate.diffDay(lastRatingDate)
            var sumValue = .0
            var sumWeight = .0
            for ((position, point) in points.withIndex()) {
                val weight = calculateWeightElement(
                    maxDiffDay,
                    point.cdate.diffDay(lastRatingDate),
                    position + 1
                )
                sumValue += point.score * weight
                sumWeight += weight
            }
            avgPoints = (sumValue / sumWeight).toFloat().roundTo(2)

            if (avgPoints.isNaN()) {
                avgPoints = 0f
            }
        }


        if (parent == null) {
            Log.e(TAG, "Не определён родитель для карты $name, $description")
        } else
        // Установка типа карты
            if (parent!!.type == ProblemType.LINE) {
                type = when {
                    avgPoints > 0 -> {
                        CardType.LINER_MOTIVATIONS
                    }
                    avgPoints < 0 -> {
                        CardType.LINER_ANCHOR
                    }
                    else -> {
                        CardType.NONE
                    }
                }
            }
    }


    /**
     * Сброс сохранённого значения среднего значения очков
     */
    private fun dischargeAvgPoints() {
        avgPoints = 0f
        calculateAvgPoints()
    }

    /**
     * Расчёт цвета карты, на основе её среднего значения очков [calculateAvgPoints]
     *
     * @return Цвет карты, представленный типом `Int`
     */
    fun calculateColor(): Int {
        val avgPoints = (avgPoints * 38).toInt()
        return when {
            avgPoints > 0 -> {
                (0xFF000000 + avgPoints * 0x100).toInt()
            }
            avgPoints < 0 -> {
                (0xFF000000 + avgPoints * -0x10000).toInt()
            }
            else -> {
                0xFF000000.toInt()
            }
        }
    }

    /**
     * Добавить очки
     *
     * @param point очко
     */
    fun add(point: Point) {
        val findPointByDate = findPointByDate(point.cdate)
        if (findPointByDate != null) {
            findPointByDate.score = point.score
        } else {
            point.parent = this
            if (points is MutableList) {
                points.add(point)
            }
        }
        dischargeAvgPoints()
    }

    private fun findPointByDate(data: Calendar): Point? {
        points.forEach { if (it.cdate == data) return it }
        return null
    }

    /**
     * Акутуализация полей карты с версии [versionFrom] по версия [versionTo].
     *
     * @param parent проблема, к которой относятся карта
     * @param versionFrom версия с которой производить актуализацию данных
     * @param versionTo версия по которую производить актуализацию данных
     */
    fun actualize(
        parent: Problem,
        versionFrom: SupportedVersionData,
        versionTo: SupportedVersionData
    ) {
        if (this.parent == null) {
            this.parent = parent
        }
        if (versionFrom == SupportedVersionData.ONE) {
            actualizeFromVersionOne()
        }
        this.points.forEach { it.actualize(this, versionFrom, versionTo) }
    }

    /**
     * Актуализация данных с первой версии
     */
    private fun actualizeFromVersionOne() {
        calculateAvgPoints()
    }

    /**
     * Получение оценки карты по её позиции [positionPoint].
     *
     * @param positionPoint позиция оценки карты
     * @return Оценка карты
     * @throws IndexOutOfBoundsException при передачи значения [positionPoint] выходящего за границы списка оценок карты [points]
     */
    operator fun get(positionPoint: Int): Point {
        return points[positionPoint]
    }
}