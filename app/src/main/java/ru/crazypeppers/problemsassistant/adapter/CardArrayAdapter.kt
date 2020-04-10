package ru.crazypeppers.problemsassistant.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.data.dto.Card
import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType
import ru.crazypeppers.problemsassistant.hyperlinkStyle
import ru.crazypeppers.problemsassistant.toStringRound

/**
 * Адаптер для представления списка карт
 *
 * @constructor Конструктор
 * @param context текущий контекст
 * @param cardList список карт
 * @param gettingTitle способ формирования названия карты
 */
class CardArrayAdapter(context: Context, cardList: List<Card>) :
    ArrayAdapter<Card>(context, R.layout.element_card_list, cardList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val card = getItem(position)!!
        val color = card.calculateColor()
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.element_card_list, null)
        }
        val cardName = view?.findViewById(R.id.cardName) as TextView
        cardName.text = card.name
        cardName.setTextColor(color)
        val cardDescription = view.findViewById<TextView>(R.id.cardDescription)
        if (card.description.isNotBlank()) {
            cardDescription.text = card.description
            cardDescription.setTextColor(color)
            cardDescription.visibility = VISIBLE
        } else {
            cardDescription.visibility = GONE
        }
        val cardAvgPoint = view.findViewById<TextView>(R.id.cardAvgPoint)
        val adb: AlertDialog.Builder = AlertDialog.Builder(context)
        adb.setTitle(R.string.informationTitle)
        adb.setIcon(android.R.drawable.ic_dialog_info)
        adb.setNeutralButton(R.string.okButton, null)
        if (card.parent?.type == ProblemType.LINE) {
            if (card.points.isEmpty()) {
                cardAvgPoint.visibility = INVISIBLE
            } else {
                cardAvgPoint.visibility = VISIBLE
                cardAvgPoint.text = card.avgPoints.toStringRound(2)
                cardAvgPoint.setOnClickListener {
                    adb.setMessage(
                        String.format(
                            context.getString(R.string.informationCardAlertBody),
                            card.avgPoints.toStringRound(2)
                        )
                    )
                    adb.create().show()
                }
                cardAvgPoint.hyperlinkStyle()
            }
        } else if (card.parent?.type == ProblemType.DESCARTES_SQUARED) {
            cardAvgPoint.visibility = VISIBLE
            cardAvgPoint.text = context.getString(
                when (card.type) {
                    CardType.SQUARE_DO_HAPPEN -> R.string.descartesSquaredIQuarterShort
                    CardType.SQUARE_NOT_DO_HAPPEN -> R.string.descartesSquaredIIQuarterShort
                    CardType.SQUARE_DO_NOT_HAPPEN -> R.string.descartesSquaredIIIQuarterShort
                    else -> R.string.descartesSquaredIVQuarterShort
                }
            )
            cardAvgPoint.hyperlinkStyle()
            cardAvgPoint.setOnClickListener {
                adb.setMessage(
                    String.format(
                        context.getString(R.string.informationCardDescartesSquaredAlertBody),
                        context.getString(
                            when (card.type) {
                                CardType.SQUARE_DO_HAPPEN -> R.string.descartesSquaredIQuarter
                                CardType.SQUARE_NOT_DO_HAPPEN -> R.string.descartesSquaredIIQuarter
                                CardType.SQUARE_DO_NOT_HAPPEN -> R.string.descartesSquaredIIIQuarter
                                else -> R.string.descartesSquaredIVQuarter
                            }
                        )
                    )
                )
                adb.create().show()
            }
        }
        return view
    }
}
