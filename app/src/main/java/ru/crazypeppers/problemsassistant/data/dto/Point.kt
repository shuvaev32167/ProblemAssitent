package ru.crazypeppers.problemsassistant.data.dto

import java.util.*

/**
 * Описания выставленных очков карте
 *
 * @property score Выставленные очки
 * @property date Даты оценки
 */
data class Point(var date: Date = Date()) {
    private var point: Int = 0
    var score = 0
        get() {
            if (field == 0 && point != 0) {
                field = point
            }
            return field
        }

    constructor(score: Int, date: Date) : this(date) {
        this.score = score
    }
}