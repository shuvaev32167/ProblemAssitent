package ru.crazypeppers.problemsassistant.data.dto

import com.google.gson.annotations.SerializedName
import ru.crazypeppers.problemsassistant.data.enumiration.CardType

/**
 * Описание карты (мотивации/якоря) для решения проблемы
 *
 * @property cardName Название карты
 * @property points Список очков
 */
class Card(
    @SerializedName("name")
    var cardName: String,
    val points: MutableList<Point>
) {
    /**
     * Тип карты
     */
    var cardType: CardType = CardType.NONE

    /**
     * Пояснение карты
     */
    var cardDescription: String = ""

    /**
     * Картинка карты
     */
    var imageBase64: String? = null

    /**
     * @param cardName название карты
     * @param cardDescription пояснение карты
     * @param points список очков
     */
    constructor(cardName: String, cardDescription: String, points: MutableList<Point>) : this(
        cardName,
        points
    ) {
        this.cardDescription = cardDescription
    }

    @Transient
    private var avgPoints: Float = Float.NaN

    /**
     * Расчёт среднего значения очков. Расчитанное значение сохранится.
     * Для сброса расчитанного значения использовать метод [dischargeAvgPoints].
     *
     * @return Среднее значение очков
     */
    fun calculateAvgPoints(): Float {
        if (avgPoints.isNaN() || avgPoints == 0f) {
            avgPoints = points.sumBy { it.score } / points.size.toFloat()
            return avgPoints
        }
        return avgPoints
    }

    /**
     * Сброс сохранённого значения среднего значения очков
     */
    fun dischargeAvgPoints() {
        avgPoints = Float.NaN
    }

    /**
     * Расчёт цвета карты, на основе её среднего значения очков [calculateAvgPoints]
     *
     * @return Цвет карты, представленный типом `Int`
     */
    fun calculateColor(): Int {
        val avgPoints = (calculateAvgPoints() * 38).toInt()
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
        points.add(point)
    }
}