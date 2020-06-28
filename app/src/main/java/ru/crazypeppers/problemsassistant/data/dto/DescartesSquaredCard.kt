package ru.crazypeppers.problemsassistant.data.dto

import com.google.gson.annotations.SerializedName
import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData
import ru.crazypeppers.problemsassistant.extension.withoutTime
import java.util.*

/**
 * Карта для решения проблемы методом декарта
 *
 * @constructor
 * @param cardName название карты
 * @param cardDescription описание карты
 * @param cardType тип карты
 */
class DescartesSquaredCard(cardName: String, cardDescription: String, cardType: CardType) :
    BaseCard(cardName) {

    /**
     * Дата создания карты
     */
    @SerializedName("createCardDate")
    var createCardDate: Calendar
        private set

    init {
        this.description = cardDescription
        this.type = cardType
        createCardDate = Calendar.getInstance().withoutTime()
    }

    override fun actualize(
        parent: Problem,
        versionFrom: SupportedVersionData,
        versionTo: SupportedVersionData
    ) {
        if (this.parent == null) {
            this.parent = parent
        }
        if (createCardDate == null) {
            createCardDate = Calendar.getInstance().withoutTime()
        }
    }

    override fun calculateColor(): Int {
        return 0xFF000000.toInt()
    }
}