package ru.crazypeppers.problemsassistant.data.dto

import com.google.gson.annotations.SerializedName
import ru.crazypeppers.problemsassistant.data.Data
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData
import ru.crazypeppers.problemsassistant.enumiration.ImportType
import ru.crazypeppers.problemsassistant.enumiration.ImportType.*
import ru.crazypeppers.problemsassistant.extension.*

/**
 * Описание решаемой проблемы
 *
 * @property name Название проблемы
 * @property cards Список карт (мотиваций/якорей)
 */
class Problem(
    @SerializedName("name")
    var name: String,
    @SerializedName("cards")
    val cards: List<BaseCard> = mutableListOf()
) {
    /**
     * Тип проблемы
     */
    @SerializedName("type")
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

    private fun findCardWithName(cardName: String): BaseCard? {
        cards.forEach {
            if (it.name.equals(cardName, true))
                return it
        }
        return null
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

    fun replaceCard(cards: List<BaseCard>, importType: ImportType) {
        when (importType) {
            FULL_REPLACE -> {
                if (this.cards.isMutable()) {
                    this.cards.clear()
                    addAll(cards)
                }
            }
            ENRICHMENT -> {
                for (card in cards) {
                    val findCardWithName = findCardWithName(card.name)
                    if (findCardWithName == null) {
                        add(card)
                    } else {
                        if (findCardWithName is LinearCard && card is LinearCard) {
                            findCardWithName.replacePoints(card.points, importType)
                        }
                    }
                }
            }
            ONLY_NEW -> {
                for (card in cards) {
                    if (!hasCardWithName(card.name)) {
                        add(card)
                    }
                }
            }
        }
    }

    private fun addAll(cards: List<BaseCard>) {
        if (this.cards.isMutable()) {
            this.cards.addAll(cards)
            for (card in cards) {
                card.parent = this
            }
        }
    }
}
