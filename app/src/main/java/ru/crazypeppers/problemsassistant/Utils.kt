@file:JvmName("Utils")

package ru.crazypeppers.problemsassistant

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.URLSpan
import android.widget.TextView
import ru.crazypeppers.problemsassistant.data.dto.Card
import ru.crazypeppers.problemsassistant.data.dto.Problem
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
 * Преобразование в строку с округлением дл [numFractionDigits] знаков после запятой.
 *
 * @param numFractionDigits число знаков после запятой, которое надо оставить.
 */
fun Float.toStringRound(numFractionDigits: Int) =
    toBigDecimal().setScale(numFractionDigits, RoundingMode.UP).toString()

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
 * Разчёт разницы между двумя датамы в полных днях.
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

/**
 * Создание заглушечной проблемы
 *
 * @return заглушечная проблема
 */
fun createProblemStub(): Problem {
    return Problem(
        "test",
        mutableListOf(
            Card(
                "ttest",
                mutableListOf()
            )
        )
    )
}

/**
 * Оборачиваение строки с `HTML` тегами в Span, для корректной обработки тегов.
 *
 * @param htmlString строка с `HTML` тегами
 * @return Span со стракой
 */
@Suppress("DEPRECATION")
fun fromHtml(htmlString: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(
            htmlString,
            Html.FROM_HTML_MODE_COMPACT
        )
    } else {
        Html.fromHtml(htmlString)
    }
}