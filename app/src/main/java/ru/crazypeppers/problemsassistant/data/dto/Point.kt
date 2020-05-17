package ru.crazypeppers.problemsassistant.data.dto

import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData
import ru.crazypeppers.problemsassistant.util.withoutTime
import java.util.*

/**
 * Описания выставленных очков карте
 */
class Point() {
    /**
     * Выставленные очки
     */
    var score = 0

    /**
     * Дата оценки
     */
    val cdate: Calendar = Calendar.getInstance().withoutTime()

    /**
     * Карта, к которой относятся очки
     */
    @Transient
    var parent: LinearCard? = null

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
        parent: LinearCard,
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
        cdate.withoutTime()
    }
}