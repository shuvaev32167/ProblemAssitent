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
    val cards: MutableList<BaseCard> = mutableListOf()
) {
    /**
     * Тип проблемы
     */
    var type: ProblemType = ProblemType.LINE
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
     * @param name название проблемы
     * @param type тип проблемы
     */
    constructor(name: String, type: ProblemType) : this(name) {
        this.type = type
    }

    /**
     * Получение карты по её позиции [positionCard].
     *
     * @param positionCard позиция карты
     * @return Карта
     * @throws IndexOutOfBoundsException при передачи значения [positionCard] выходящего за границы списка карт [cards]
     */
    operator fun get(positionCard: Int): BaseCard {
        return cards[positionCard]
    }

    /**
     * Добавить карту к списку карт
     *
     * @param card карта
     */
    fun add(card: BaseCard) {
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
    fun hasCardWithName(cardName: String, currentCard: BaseCard? = null): Boolean {
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
    }

    /**
     * Расчёт веса проблемы.
     *
     * @return вес проблемы.
     */
    fun calculateScoreProblem(): Float {
        val avgPointsList = cards.filterIsInstance<LinearCard>().filter {
            if (it.points.isEmpty())
                return@filter false
            return@filter true
        }.map { it.avgPoints }
        return avgPointsList.sum() / avgPointsList.size
    }
}