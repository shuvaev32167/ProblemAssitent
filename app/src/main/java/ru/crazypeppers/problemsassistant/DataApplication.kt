package ru.crazypeppers.problemsassistant

import android.app.Application
import com.google.gson.Gson
import ru.crazypeppers.problemsassistant.data.Data
import ru.crazypeppers.problemsassistant.data.dto.Card
import ru.crazypeppers.problemsassistant.data.dto.Point
import ru.crazypeppers.problemsassistant.data.dto.Problem
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

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

    /**
     * Сохранение данных
     *
     * @param data данные
     */
    private fun save(data: Data) {
        data.problems.forEach { problem ->
            problem.cards.forEach { card ->
                val pointsSortedByDescDate = card.points.sortedByDescending { it.cdate }
                var prevPoint: Point? = null
                for (point in pointsSortedByDescDate) {
                    if (prevPoint == null) {
                        prevPoint = point
                    } else if (prevPoint.cdate == point.cdate) {
                        card.points.remove(point)
                    }
                }
            }
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

    /**
     * Загрузка данных
     *
     * @return данные
     */
    private fun load(): Data {
        return try {
            openFileInput("data.json").use { fileInputStream ->
                InputStreamReader(fileInputStream).use { inputStreamReader ->
                    BufferedReader(inputStreamReader).use { bufferedReader ->
                        val json = bufferedReader.readText()
                        val data = gson.fromJson(json, Data::class.java)
                        data.actualize()
                        save(data)
                        return data
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
}
