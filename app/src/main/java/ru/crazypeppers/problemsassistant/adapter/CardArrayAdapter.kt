package ru.crazypeppers.problemsassistant.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.crazypeppers.problemsassistant.data.dto.Card

/**
 * Адаптер для представления списка карт
 *
 * @constructor Конструктор
 * @param context текущий контекст
 * @param cardList список карт
 * @param gettingTitle способ формирования названия карты
 */
class CardArrayAdapter(context: Context, cardList: List<Card>, val gettingTitle: (Card) -> String) :
    ArrayAdapter<Card>(context, android.R.layout.simple_list_item_2, cardList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val card = getItem(position)!!
        if (view == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, null)
        }
        val color = card.calculateColor()
        val textView1 = view?.findViewById(android.R.id.text1) as TextView
        textView1.text = gettingTitle(card)
        textView1.setTextColor(color)
        val textView2 = view.findViewById(android.R.id.text2) as TextView
        if (card.description.isBlank()) {
            textView2.visibility = View.GONE
        } else {
            textView2.text = card.description
            textView2.setTextColor(color)
        }
        return view
    }
}
