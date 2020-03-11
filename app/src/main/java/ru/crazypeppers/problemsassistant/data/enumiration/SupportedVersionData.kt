package ru.crazypeppers.problemsassistant.data.enumiration

/**
 * Список поддерживаемых версий форматов данных
 *
 * @property code Код поддерживаемой версии формата данных
 */
enum class SupportedVersionData(val code: Int) {
    /**
     * Первая версия формата данных
     */
    ONE(1);

    companion object {
        /**
         * Получение последней поддерживаемой версии формата данных.
         *
         * @return Полследняя поддерживаемая версия формата данных.
         */
        @JvmStatic
        fun lastVersion(): SupportedVersionData = values().maxBy { it.code }!!
    }
}