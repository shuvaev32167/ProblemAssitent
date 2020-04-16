package ru.crazypeppers.problemsassistant.data.dto

import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData

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

    init {
        this.description = cardDescription
        this.type = cardType
    }

    override fun actualize(
        parent: Problem,
        versionFrom: SupportedVersionData,
        versionTo: SupportedVersionData
    ) {
        if (this.parent == null) {
            this.parent = parent
        }
    }

    override fun calculateColor(): Int {
        return 0xFF000000.toInt()
    }
}