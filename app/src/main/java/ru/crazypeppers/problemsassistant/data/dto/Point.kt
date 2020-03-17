package ru.crazypeppers.problemsassistant.data.dto

import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData
import ru.crazypeppers.problemsassistant.withoutTime
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
    val cdate: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).withoutTime()

    /**
     * Карта, к которой относятся очки
     */
    @Transient
    var parent: Card? = null

    /**
     * @param score выставленные очки
     */
    constructor(score: Int) : this() {
        this.score = score
    }

    /**
     * Акутуализация полей очков с версии [versionFrom] по версия [versionTo].
     *
     * @param parent карта, к которой относятся очки
     * @param versionFrom версия с которой производить актуализацию данных
     * @param versionTo версия по которую производить актуализацию данных
     */
    fun actualize(
        parent: Card,
        versionFrom: SupportedVersionData,
        versionTo: SupportedVersionData
    ) {
        if (this.parent == null) {
            this.parent = parent
        }
        if (versionFrom == SupportedVersionData.ONE) {
            actualizeFromVersionOne()
        }
    }

    /**
     * Актуализация данных с первой версии
     */
    private fun actualizeFromVersionOne() {
        if (point != null) {
            if (score == 0) {
                score = point!!
            }
            point = null
        }

        if (date != null) {
            cdate.time = date!!
            cdate.withoutTime()
            date = null
        }

        cdate.withoutTime()
    }
}