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

        /**
         * Увеличение версии на еденицу. Если происходит увеличения у `null`, то версия становиться [ONE].
         * Версия не может быть больше, чем последняя версия, возвращаемая [lastVersion].
         *
         * @return Версия, увеличенная на 1
         */
        fun SupportedVersionData?.inc(): SupportedVersionData {
            if (this == null) {
                return ONE
            }
            return findByCode(this.code + 1) ?: lastVersion()
        }

        /**
         * Поиск версии по переданному коду [code] версии.
         *
         * @param code код версии для поиска
         * @return Найденная версия, или `null`, если не удалось найти версию по коду
         */
        private fun findByCode(code: Int): SupportedVersionData? {
            for (supportedVersionData in values()) {
                if (supportedVersionData.code == code)
                    return supportedVersionData
            }
            return null
        }

        /**
         * Сравнение версий между собой.
         * Возможные значения:
         * 1. `< 0` - если текущая версия меньше, чем [other]
         * 1. `0` - если текущая версия равна [other]
         * 1. `> 0` - если текущая версия больше, чем [other]
         *
         * @param other версия, с которой производится сравнение
         * @return Результат стравнения (`< 0`. `0` или `> 0`)
         */
        fun SupportedVersionData?.compare(other: SupportedVersionData?): Int {
            return if (this == null && other == null) 0
            else if (this == null && other != null) -1
            else if (this != null && other == null) 1
            else this!!.code.compareTo(other!!.code)
        }
    }
}