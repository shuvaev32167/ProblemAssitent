package ru.crazypeppers.problemsassistant.data.dto

import com.google.gson.annotations.SerializedName
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType

/**
 * Описание решаемой проблемы
 *
 * @property problemName Название проблемы
 * @property cards Список карт (мотиваций/якорей)
 */
class Problem(
    @SerializedName("name")
    var problemName: String,
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
            if (it.cardName.equals(cardName, true) && it !== currentCard)
                return true
        }
        return false
    }
}