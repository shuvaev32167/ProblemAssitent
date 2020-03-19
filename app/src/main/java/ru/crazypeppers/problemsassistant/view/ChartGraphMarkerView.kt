package ru.crazypeppers.problemsassistant.view

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import ru.crazypeppers.problemsassistant.R
import kotlin.math.roundToInt

/**
 * Клас, обеспечивающий работу маркеров со значениями на графиках
 *
 * @constructor
 * @param context контекст, в котором находится график
 */
@SuppressLint("ViewConstructor")
class ChartGraphMarkerView(context: Context?) :
    MarkerView(context, R.layout.view_chart_graph_marker) {
    private val tvContent: TextView = findViewById(R.id.tvContent)

    override fun refreshContent(
        e: Entry,
        highlight: Highlight
    ) {
        if (e is CandleEntry) {
            tvContent.text = e.high.roundToInt().toString()
        } else {
            tvContent.text = e.y.roundToInt().toString()
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}