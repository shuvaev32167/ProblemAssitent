package ru.crazypeppers.problemsassistant.data.dto

import com.google.gson.annotations.SerializedName
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType

/**
 * Описание решаемой проблемы
 *
 * @property problemName Название проблемы
 * @property cards Список карт (мотиваций/якорей)
 * @property type Тип проблемы
 * @property imageBase64 Картинка проблемы
 */
data class Problem(
    @SerializedName("name")
    var problemName: String,
    val cards: MutableList<Card>,
    var type: ProblemType = ProblemType.LINE,
    var imageBase64: String? = null
) {
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
}