package ru.crazypeppers.problemsassistant.data

import ru.crazypeppers.problemsassistant.data.dto.Problem
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData

/**
 * Описание данных для сохранения в файл
 *
 * @property problems Список проблем
 */
data class Data(val problems: MutableList<Problem>) {
    /**
     * Версия формата данных
     */
    var version: SupportedVersionData? = null

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
            if (it.problemName.equals(problemName, true) && it !== currentProblem)
                return true
        }
        return false
    }
}
