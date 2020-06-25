package ru.crazypeppers.problemsassistant.enumiration

/**
 * Тип импорта данных
 */
enum class ImportType {
    /**
     * Полная замена данных
     */
    FULL_REPLACE,

    /**
     * Обогащение
     */
    ENRICHMENT,

    /**
     * Только новое
     */
    ONLY_NEW
}