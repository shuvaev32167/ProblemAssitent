package ru.crazypeppers.problemsassistant.data.enumiration

/**
 * Тип карты (монивации/якоря).
 * Зависит от типа решения проблемы ([ProblemType])
 */
enum class CardType {
    /**
     * Тип не определён
     */
    NONE,

    /**
     * Линейный мотиватор
     */
    LINER_ADVANTAGE,

    /**
     * Линейный якорь
     */
    LINER_DISADVANTAGE

}