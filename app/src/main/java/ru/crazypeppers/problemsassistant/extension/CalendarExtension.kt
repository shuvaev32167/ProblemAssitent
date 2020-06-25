@file:JvmName("CalendarExtension")

package ru.crazypeppers.problemsassistant.extension

import java.util.*

/**
 * Отрезание времени от даты.
 *
 * @return Дата без времени
 */
fun Calendar.withoutTime(): Calendar {
    this[Calendar.HOUR_OF_DAY] = 0
    this[Calendar.MINUTE] = 0
    this[Calendar.SECOND] = 0
    this[Calendar.MILLISECOND] = 0

    return this
}

/**
 * Число миллисекунд в дне
 */
private const val millisecondPerDay = 24 * 60 * 60 * 1000

/**
 * Расчёт разницы между двумя датами в полных днях.
 * Текущее считается большем
 *
 * @param date дата, до которой надо найти разницу
 * @return Число дней, между текущей датой и [date]
 */
fun Calendar.diffDay(date: Calendar): Int =
    ((this.timeInMillis - date.timeInMillis) / millisecondPerDay).toInt()

/**
 * Создаёт новый обект, на основе текущего со звигом на указанное [day] число дней
 *
 * @param day число дней, которые надо добавить
 * @return Дата, с придавленым числом дней [day]
 */
fun Calendar.addDayAsNewInstance(day: Int): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this.time
    calendar.add(Calendar.DATE, day)
    return calendar
}