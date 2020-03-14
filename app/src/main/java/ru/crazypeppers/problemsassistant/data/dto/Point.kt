package ru.crazypeppers.problemsassistant.data.dto

import java.util.*

/**
 * Описания выставленных очков карте
 *
 * @property date Даты оценки
 */
class Point(var date: Date = Date()) {
    private var point: Int = 0

    /**
     * Выставленные очки
     */
    var score = 0
        get() {
            if (field == 0 && point != 0) {
                field = point
            }
            return field
        }

    /**
     * @param score выставленные очки
     * @param date дата оценки
     */
    constructor(score: Int, date: Date) : this(date) {
        this.score = score
    }
}