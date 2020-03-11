package ru.crazypeppers.problemsassistant

import android.app.Application
import com.google.gson.Gson
import ru.crazypeppers.problemsassistant.data.Data
import ru.crazypeppers.problemsassistant.data.dto.Card
import ru.crazypeppers.problemsassistant.data.dto.Point
import ru.crazypeppers.problemsassistant.data.dto.Problem
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

/**
 * Реализация [Application] с возможносью оперировать данными
 */
class DataApplication : Application() {
    private val gson = Gson()

    /**
     * Данные
     */
    val data: Data by lazy { load() }

    /**
     * Сохранить данные
     */
    fun saveData() {
        save(data)
    }

    private fun save(data: Data) {
        data.problems.forEach { problem ->
            problem.cards.forEach { card ->
                val pointsSortedByDescDate = card.points.sortedByDescending { it.date }
                var prevPoint: Point? = null
                for (point in pointsSortedByDescDate) {
                    point.date = point.date.withoutTime()
                    if (prevPoint == null) {
                        prevPoint = point
                    } else if (prevPoint.date == point.date) {
                        card.points.remove(point)
                    }
                }
            }
        }
        if (data.version == null) {
            data.version = SupportedVersionData.ONE
        }
        val json = gson.toJson(data)
        openFileOutput("data.json", MODE_PRIVATE).use { fileOutputStream ->
            OutputStreamWriter(fileOutputStream).use { outputStreamWriter ->
                BufferedWriter(outputStreamWriter).use { bufferedWriter ->
                    bufferedWriter.write(json)
                }
            }
        }
    }

    private fun load(): Data {
        return try {
            openFileInput("data.json").use { fileInputStream ->
                InputStreamReader(fileInputStream).use { inputStreamReader ->
                    BufferedReader(inputStreamReader).use { bufferedReader ->
                        val json = bufferedReader.readText()
                        return gson.fromJson(json, Data::class.java)
                    }
                }
            }
        } catch (e: Exception) {
            val problem = Problem(
                "test",
                mutableListOf()
            )
            val card = Card(
                "ttest",
                mutableListOf()
            )
            problem.add(card)
            Data(
                mutableListOf(
                    problem
                )
            )
        }
    }

    /**
     * Отрезание времени от даты.
     *
     * @return Дата без времени
     */
    private fun Date.withoutTime(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = this
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        return calendar.time
    }
}
