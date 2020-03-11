package ru.crazypeppers.problemsassistant.data.dto

import com.google.gson.annotations.SerializedName
import ru.crazypeppers.problemsassistant.data.enumiration.CardType

/**
 * Описание карты (мотивации/якоря) для решения проблемы
 *
 * @property cardName Название карты
 * @property points Список очков
 * @property cardType Тип карты
 * @property cardDescription Пояснение карты
 * @property imageBase64 Картинка карты
 */
data class Card(
    @SerializedName("name")
    var cardName: String,
    var points: MutableList<Point>,
    var cardType: CardType = CardType.NONE,
    var cardDescription: String? = "",
    var imageBase64: String? = null
) {
    @Transient
    private var avgPoints: Float = Float.NaN

    /**
     * Расчёт среднего значения очков. В зависимости от переданного значения [freeze] расчитанное значение сохраниться.
     * Для сброса расчитанного значения использовать метод [dischargeAvgPoints].
     *
     * @param freeze флаг сохранения расчитанного результата, по умолчанию true
     *
     * @return Среднее значение очков
     */
    fun calculateAvgPoints(freeze: Boolean = true): Float {
        if (this.avgPoints.isNaN() || this.avgPoints == 0f) {
            val avgPoints = points.sumBy { it.score } / points.size.toFloat()
            if (freeze) {
                this.avgPoints = avgPoints
            }
            return avgPoints
        }
        return this.avgPoints
    }

    /**
     * Сброс сохранённого значения среднего значения очков
     */
    fun dischargeAvgPoints() {
        avgPoints = Float.NaN
    }

    fun calculateColor(freeze: Boolean = true): Int {
        val avgPoints = (calculateAvgPoints(freeze) * 38).toInt()
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