package ru.crazypeppers.problemsassistant.data.dto

import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData
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
    private var cardDescription: String? = null

    /**
     * Картинка карты
     */
    var imageBase64: String? = null

    /**
     * @param cardName название карты
     * @param cardDescription пояснение карты
     * @param points список очков
     */
    constructor(cardName: String, cardDescription: String, points: List<Point>) : this(
        cardName,
        points
    ) {
        this.description = cardDescription
        calculateAvgPoints()
    }

    /**
     * Среднее зачение очков, привязанных к карте
     */
    var avgPoints: Float = Float.NaN

    /**
     * Проблема, к которой относится карты
     */
    @Transient
    var parent: Problem? = null

    /**
     * Расчёт среднего значения очков. Расчитанное значение сохранится.
     * Для сброса расчитанного значения использовать метод [dischargeAvgPoints].
     *
     * @return Среднее значение очков
     */
    private fun calculateAvgPoints() {
        avgPoints = points.sumBy { it.score } / points.size.toFloat()
        if (parent!!.type == ProblemType.LINE) {
            if (avgPoints > 0) {
                type = CardType.LINER_MOTIVATIONS
            } else if (avgPoints < 0) {
                type = CardType.LINER_ANCHOR
            } else {
                type = CardType.NONE
            }
        }
    }


    /**
     * Сброс сохранённого значения среднего значения очков
     */
    fun dischargeAvgPoints() {
        avgPoints = Float.NaN
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
        if (cardDescription != null) {
            description = cardDescription!!
            cardDescription = null
        }
        if (this.description == null) {
            description = ""
        }
    }
}