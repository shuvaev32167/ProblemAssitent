package ru.crazypeppers.problemsassistant.adapter

import com.google.gson.*
import ru.crazypeppers.problemsassistant.data.dto.BaseCard
import ru.crazypeppers.problemsassistant.data.dto.DescartesSquaredCard
import ru.crazypeppers.problemsassistant.data.dto.LinearCard
import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import java.lang.reflect.Type

/**
 * Адаптер для полиморфной Сериализации/Десериализации [BaseCard]
 */
class JsonSerializeDeserializeCardAdapter : JsonSerializer<BaseCard>, JsonDeserializer<BaseCard> {
    override fun serialize(
        src: BaseCard?,
        typeOfSrc: Type?,
        context: JsonSerializationContext
    ): JsonElement {
        return context.serialize(src)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): BaseCard {
        val jsonObject = json.asJsonObject
        val prim = jsonObject["type"]

        var type = prim.asString
        if (type.indexOf("LINER") >= 0) {
            type = type.replace("LINER", "LINEAR")
        }

        val `class` = when (CardType.valueOf(type)) {
            CardType.NONE, CardType.LINEAR_ADVANTAGE, CardType.LINEAR_DISADVANTAGE -> LinearCard::class.java
            CardType.SQUARE_DO_HAPPEN, CardType.SQUARE_DO_NOT_HAPPEN, CardType.SQUARE_NOT_DO_NOT_HAPPEN, CardType.SQUARE_NOT_DO_HAPPEN -> DescartesSquaredCard::class.java
            else -> BaseCard::class.java
        }
        return context!!.deserialize(json, `class`)
    }
}