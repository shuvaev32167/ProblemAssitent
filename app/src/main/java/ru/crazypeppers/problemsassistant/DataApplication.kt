package ru.crazypeppers.problemsassistant

import android.app.Application
import android.util.Log
import ru.crazypeppers.problemsassistant.data.Data
import ru.crazypeppers.problemsassistant.data.TAG
import ru.crazypeppers.problemsassistant.util.GSON
import ru.crazypeppers.problemsassistant.util.createProblemStub
import ru.crazypeppers.problemsassistant.util.readDataFromBufferedReader
import java.io.*

/**
 * Реализация [Application] с возможностью оперировать данными
 */
class DataApplication : Application() {

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
        val json = GSON.toJson(data)
        openFileOutput("data.problemAssistant", MODE_PRIVATE).use { fileOutputStream ->
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
        try {
            val sourceFile = File(filesDir, "data.json")
            val targetFile = File(filesDir, "data.problemAssistant")
            if (sourceFile.exists() && !targetFile.exists()) {
                sourceFile.renameTo(targetFile)
            }
            openFileInput("data.problemAssistant").use { fileInputStream ->
                InputStreamReader(fileInputStream).use { inputStreamReader ->
                    BufferedReader(inputStreamReader).use { bufferedReader ->
                        val data = readDataFromBufferedReader(bufferedReader)
                        save(data)
                        return data
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при чтении данных", e)
            return Data(
                mutableListOf(
                    createProblemStub()
                )
            )
        }
    }
}
