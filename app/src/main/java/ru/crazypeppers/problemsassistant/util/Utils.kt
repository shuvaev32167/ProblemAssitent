@file:JvmName("Utils")

package ru.crazypeppers.problemsassistant.util

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.URLSpan
import android.util.Log
import android.widget.TextView
import com.google.gson.GsonBuilder
import ru.crazypeppers.problemsassistant.adapter.JsonSerializeDeserializeCardAdapter
import ru.crazypeppers.problemsassistant.data.Data
import ru.crazypeppers.problemsassistant.data.TAG
import ru.crazypeppers.problemsassistant.data.dto.BaseCard
import ru.crazypeppers.problemsassistant.data.dto.LinearCard
import ru.crazypeppers.problemsassistant.data.dto.Problem
import java.io.BufferedReader

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
        "example",
        mutableListOf<BaseCard>(
            LinearCard(
                "example",
                mutableListOf()
            )
        )
    )
}

/**
 * Оборачивание строки с `HTML` тегами в Span, для корректной обработки тегов.
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

/**
 * Чтение данных из буфера
 *
 * @param bufferedReader буфер чтения данных
 * @return данные
 */
fun readDataFromBufferedReader(bufferedReader: BufferedReader): Data {
    try {
        bufferedReader.use {
            val json = bufferedReader.readText()
            val data = GSON.fromJson(json, Data::class.java)
            data.actualize()
            return data
        }
    } catch (e: Exception) {
        Log.e(TAG, "Ошибка при чтении данных", e)
        throw e
    }
}

/**
 * Сериализатор/десериализотор json
 */
val GSON = GsonBuilder().registerTypeAdapter(
    BaseCard::class.java,
    JsonSerializeDeserializeCardAdapter()
).create()