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
    LINEAR_ADVANTAGE,
    /**
     * Линейный якорь
     */
    LINEAR_DISADVANTAGE,

    /**
     * Квадрат Декарта - Будет-Случится
     */
    SQUARE_DO_HAPPEN,
    /**
     * Квадрат Декарта - Не будет-Случится
     */
    SQUARE_NOT_DO_HAPPEN,
    /**
     * Квадрат Декарта - Будет-Не случится
     */
    SQUARE_DO_NOT_HAPPEN,
    /**
     * Квадрат Декарта - Не будет-Не случится
     */
    SQUARE_NOT_DO_NOT_HAPPEN

}