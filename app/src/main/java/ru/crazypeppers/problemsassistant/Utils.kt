@file:JvmName("Utils")

package ru.crazypeppers.problemsassistant

import android.text.SpannableString
import android.text.Spanned
import android.text.style.URLSpan
import android.widget.TextView
import java.math.RoundingMode
import java.util.*
import java.util.Calendar.DATE

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
fun Calendar.withoutTime(): Calendar {
    this[Calendar.HOUR_OF_DAY] = 0
    this[Calendar.MINUTE] = 0
    this[Calendar.SECOND] = 0
    this[Calendar.MILLISECOND] = 0

    return this
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
    calendar.add(DATE, day)
    return calendar
}

/**
 * Придание тексту в [TextView] стиля гиперссылки
 */
fun TextView.hyperlinkStyle() {
    setText(
        SpannableString(text).apply {
            setSpan(
                URLSpan(""),
                0,
                length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        },
        TextView.BufferType.SPANNABLE
    )
}