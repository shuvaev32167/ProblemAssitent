package ru.crazypeppers.problemsassistant.data.dto

import ru.crazypeppers.problemsassistant.data.Data
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData

/**
 * Описание решаемой проблемы
 *
 * @property name Название проблемы
 * @property cards Список карт (мотиваций/якорей)
 */
class Problem(
    var name: String,
    val cards: MutableList<Card> = mutableListOf()
) {
    /**
     * Тип проблемы
     */
    var type: ProblemType = ProblemType.LINE

    /**
     * Картинка проблемы
     */
    var imageBase64: String? = null

    /**
     * Данные, в которые входит проблема
     */
    @Transient
    var parent: Data? = null

    init {
        for (card in cards) {
            card.parent = this
        }
    }

    /**
     * Получение карты по её позиции [positionCard].
     *
     * @param positionCard позиция карты
     * @return Карта
     * @throws IndexOutOfBoundsException при передачи значения [positionCard] выходящего за границы списка карт [cards]
     */
    operator fun get(positionCard: Int): Card {
        return cards[positionCard]
    }

    /**
     * Добавить карту к списку карт
     *
     * @param card карта
     */
    fun add(card: Card) {
        card.parent = this
        cards.add(card)
    }

    /**
     * Удалить карту из списка карт по её индексу
     *
     * @param positionCard позиция карты
     * @throws IndexOutOfBoundsException при передачи значения [positionCard] выходящего за границы списка карт [cards]
     */
    fun removeAt(positionCard: Int) {
        cards.removeAt(positionCard)
    }

    /**
     * Определение наличие карты с именем [cardName] в списке карт [cards]
     *
     * @param cardName имя карты для поиска
     * @param currentCard текущая редактируемая карта
     * @return `true`, если карта с таким именем есть в списке [cards], иначе `false`
     */
    fun hasCardWithName(cardName: String, currentCard: Card? = null): Boolean {
        cards.forEach {
            if (it.name.equals(cardName, true) && it !== currentCard)
                return true
        }
        return false
    }

    /**
     * Акутуализация полей проблемы с версии [versionFrom] по версия [versionTo].
     *
     * @param parent данные, в которые входит проблема
     * @param versionFrom версия с которой производить актуализацию данных
     * @param versionTo версия по которую производить актуализацию данных
     */
    fun actualize(
        parent: Data,
        versionFrom: SupportedVersionData,
        versionTo: SupportedVersionData
    ) {
        if (this.parent == null) {
            this.parent = parent
        }
        if (versionFrom == SupportedVersionData.ONE) {
            actualizeFromVersionOne()
        }
        this.cards.forEach { it.actualize(this, versionFrom, versionTo) }
    }

    /**
     * Актуализация данных с первой версии
     */
    private fun actualizeFromVersionOne() {
        type = ProblemType.LINE
    }
}