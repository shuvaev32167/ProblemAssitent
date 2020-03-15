package ru.crazypeppers.problemsassistant.data.dto

import java.util.*

/**
 * Описания выставленных очков карте
 */
class Point() {
    private var point: Int? = null
    private var date: Date? = null

    /**
     * Выставленные очки
     */
    var score = 0
        get() {
            if (field == 0 && point != null) {
                field = point ?: 0
                point = null
            }
            return field
        }

    /**
     * Дата оценки
     */
    var cdate: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        get() {
            if (date != null) {
                field.time = date!!
                date = null
            }
            return field
        }

    /**
     * @param score выставленные очки
     * @param cdate дата оценки
     */
    constructor(score: Int, cdate: Calendar) : this() {
        this.score = score
        this.cdate = cdate
    }
}