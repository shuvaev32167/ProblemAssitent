package ru.crazypeppers.problemsassistant.data.dto

import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.data.enumiration.SupportedVersionData

/**
 * Базовое описание карты для решения проблемы
 *
 * @property name Название карты
 */
abstract class BaseCard(var name: String) {
    /**
     * Тип карты
     */
    var type: CardType = CardType.NONE

    /**
     * Пояснение карты
     */
    var description: String = ""

    /**
     * Картинка карты
     */
    var imageBase64: String? = null

    /**
     * Проблема, к которой относится карты
     */
    @Transient
    var parent: Problem? = null

    /**
     * Акутуализация полей карты с версии [versionFrom] по версия [versionTo].
     *
     * @param parent проблема, к которой относятся карта
     * @param versionFrom версия с которой производить актуализацию данных
     * @param versionTo версия по которую производить актуализацию данных
     */
    abstract fun actualize(
        parent: Problem,
        versionFrom: SupportedVersionData,
        versionTo: SupportedVersionData
    )

    /**
     * Расчёт цвета, каким необходимо отобразить карту
     *
     * @return цвет карты, в формате 0xAARRGGBB
     */
    abstract fun calculateColor(): Int
}