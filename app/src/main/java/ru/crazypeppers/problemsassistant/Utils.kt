@file:JvmName("Utils")

package ru.crazypeppers.problemsassistant

import java.math.RoundingMode
import java.util.*

/**
 * Округление до [numFractionDigits] знаков после запятой.
 *
 * @param numFractionDigits число знаков после запятой, которое надо оставить.
 */
fun Float.roundTo(numFractionDigits: Int) =
    toBigDecimal().setScale(numFractionDigits, RoundingMode.UP).toFloat()

/**
 * Отрезание времени от даты.
 *
 * @return Дата без времени
 */
fun Date.withoutTime(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0

    return calendar.time
}

/**
 * Число милисекунд в дне
 */
private const val millisecondPerDay = 24 * 60 * 60 * 1000

/**
 * Разчёт разницы между двумя датамы в полных днях
 *
 * @param date дата, до которой надо найти разницу
 * @return Число дней, между текущей датой и [date]
 */
fun Date.diffDay(date: Date): Int = ((this.time - date.time) / millisecondPerDay).toInt()

/**
 * Создаёт новый обект, на основе текущего со звигом на указанное [day] число дней
 *
 * @param day число дней, которые надо добавить
 * @return Дата, с придавленым числом дней [day]
 */
fun Date.addDayAsNewInstance(day: Int): Date = Date(day * millisecondPerDay + this.time)