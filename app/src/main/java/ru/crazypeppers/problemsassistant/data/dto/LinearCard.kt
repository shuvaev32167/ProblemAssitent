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
 * Описание карты (мотивации/якоря) для решения линейной проблемы
 *
 * @property points Список очков
 *
 * @constructor
 * @param name название карты
 */
class LinearCard(
    name: String,
    val points: List<Point>
) : BaseCard(name) {
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
     * @param cardName название карты
     * @param cardDescription пояснение карты
     */
    constructor(cardName: String, cardDescription: String) : this(cardName, mutableListOf()) {
        this.description = cardDescription
    }

    /**
     * @param cardName название карты
     * @param cardDescription пояснение карты
     * @param cardType тип карты
     */
    constructor(cardName: String, cardDescription: String, cardType: CardType) : this(
        cardName,
        cardDescription
    ) {
        this.type = cardType
    }

    /**
     * Среднее зачение очков, привязанных к карте
     */
    var avgPoints: Float = 0f

    /**
     * Расчёт веса элемента оценки
     *
     * @param maxDay число дней между первой и последней оценкой
     * @param diffDay разница в днях между текущей и последней оценкой
     * @param position позиция оценки
     * @return вес элемента оценки
     */
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
                        CardType.LINEAR_ADVANTAGE
                    }
                    avgPoints < 0 -> {
                        CardType.LINEAR_DISADVANTAGE
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
    override fun calculateColor(): Int {
        val avgPoints = (avgPoints * 50).toInt()
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
                points.sortByDescending { it.cdate }
            }
        }
        dischargeAvgPoints()
    }

    /**
     * Поиск оценки по дате
     *
     * @param data дата, по которой ищем
     * @return найденная оценка, или `null`, если не удалось найти оценку
     */
    private fun findPointByDate(data: Calendar): Point? {
        points.forEach { if (it.cdate == data) return it }
        return null
    }

    override fun actualize(
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