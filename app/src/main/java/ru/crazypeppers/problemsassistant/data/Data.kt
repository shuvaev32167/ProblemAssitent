package ru.crazypeppers.problemsassistant.data

import com.google.gson.annotations.SerializedName
import ru.crazypeppers.problemsassistant.data.dto.Problem
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData.Companion.compare
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData.Companion.inc
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData.Companion.lastVersion
import ru.crazypeppers.problemsassistant.enumiration.ImportType
import ru.crazypeppers.problemsassistant.enumiration.ImportType.*
import ru.crazypeppers.problemsassistant.extension.add
import ru.crazypeppers.problemsassistant.extension.clear
import ru.crazypeppers.problemsassistant.extension.removeAt

/**
 * Описание данных для сохранения в файл
 *
 * @property problems Список проблем
 */
data class Data(@SerializedName("problems") val problems: List<Problem>) {
    /**
     * Версия формата данных
     */
    @SerializedName("version")
    var version: SupportedVersionData? = null

    init {
        for (problem in problems) {
            problem.parent = this
        }
    }

    /**
     * Удаление проблемы по её позиции.
     *
     * @param positionProblem позиция проблемы
     * @throws IndexOutOfBoundsException при передачи значения [positionProblem] выходящего за границы списка проблем [problems]
     */
    fun removeAt(positionProblem: Int) {
        problems.removeAt(positionProblem)
    }

    /**
     * Получение проблемы по её позиции [positionProblem].
     *
     * @param positionProblem позиция карты
     * @return Проблема
     * @throws IndexOutOfBoundsException при передачи значения [positionProblem] выходящего за границы списка карт [problems]
     */
    operator fun get(positionProblem: Int): Problem {
        return problems[positionProblem]
    }

    /**
     * Добавление проблемы [problem] в список проблем [problems]
     *
     * @param problem проблема
     */
    fun add(problem: Problem) {
        problem.parent = this
        problems.add(problem)
    }

    /**
     * Определение наличие проблемы с именем [problemName] в списке проблем [problems]
     *
     * @param problemName имя проблемы для поиска
     * @param currentProblem текущая редактируемая проблема
     * @return `true`, если проблема с таким именем есть в списке [problems], иначе `false`
     */
    fun hasProblemWithName(problemName: String, currentProblem: Problem? = null): Boolean {
        problems.forEach {
            if (it.name.equals(problemName, true) && it !== currentProblem)
                return true
        }
        return false
    }

    private fun findProblemWithName(problemName: String): Problem? {
        problems.forEach {
            if (it.name.equals(problemName, true))
                return it
        }
        return null
    }

    /**
     * Актуализация данных по последней версии
     */
    fun actualize() {
        val versionTo = lastVersion()
        version = SupportedVersionData.ONE
        do {
            this.problems.forEach {
                it.actualize(
                    this,
                    version ?: SupportedVersionData.ONE,
                    versionTo
                )
            }

            if (version.compare(versionTo) < 0) {
                version = version.inc()
            }
        } while (version != versionTo)
    }

    /**
     * Очищение списка проблем [problems]
     */
    fun clearData() {
        problems.clear()
    }

    fun replaceProblems(
        problems: List<Problem>,
        importType: ImportType
    ) {
        when (importType) {
            FULL_REPLACE -> if (this.problems is MutableList) {
                clearData()
                addAll(problems)
                for (problem in problems) {
                    problem.parent = this
                }
            }
            ENRICHMENT -> {
                for (problem in problems) {
                    val findProblemWithName = findProblemWithName(problem.name)
                    if (findProblemWithName == null) {
                        add(problem)
                    } else {
                        findProblemWithName.replaceCard(problem.cards, importType)
                    }
                }
            }
            ONLY_NEW -> {
                for (problem in problems) {
                    if (!hasProblemWithName(problem.name)) {
                        add(problem)
                    }
                }
            }

        }
    }

    private fun addAll(problems: List<Problem>) {
        if (this.problems is MutableList) {
            this.problems.addAll(problems)
            for (problem in problems) {
                problem.parent = this
            }
        }
    }
}
