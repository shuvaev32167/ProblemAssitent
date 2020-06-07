package ru.crazypeppers.problemsassistant.fragment

import android.graphics.Color.BLACK
import android.graphics.Color.GRAY
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_analyse_card.*
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.CARD_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.DATE_FORMAT
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.dto.BaseCard
import ru.crazypeppers.problemsassistant.data.dto.LinearCard
import ru.crazypeppers.problemsassistant.data.dto.Point
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener
import ru.crazypeppers.problemsassistant.util.addDayAsNewInstance
import ru.crazypeppers.problemsassistant.util.diffDay
import ru.crazypeppers.problemsassistant.view.ChartGraphMarkerView

/**
 * Фрагмент, отвечающий за отображение изменение оценки карты
 */
class AnalyseCardFragment : Fragment(), OnBackPressedListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_analyse_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.onBackPressedListener = this
        val application = activity.application as DataApplication

        val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
        inputAdd.hide()
        activity.title = getString(R.string.analyzeCardFragmentLabel)

        var positionProblem = NOT_POSITION
        var positionCard = NOT_POSITION
        val arg = arguments
        if (arg != null) {
            positionProblem = arg.getInt(PROBLEM_POSITION_TEXT, NOT_POSITION)
            positionCard = arg.getInt(CARD_POSITION_TEXT, NOT_POSITION)
        }

        if (positionProblem != NOT_POSITION && positionCard != NOT_POSITION) {
            val card = application.data[positionProblem][positionCard]
            graphLabel.text =
                String.format(getString(R.string.analyzeCardFragmentGraphLabel), card.name)
            if (card.isHideChart()) {
                lineChart.visibility = GONE
            } else if (card is LinearCard) {
                lineChart.visibility = VISIBLE
                val values = ArrayList<Entry>(card.points.size)
                val firstDay = card.points.last().cdate
                card.points.forEach {
                    values.add(
                        Entry(
                            it.cdate.diffDay(firstDay).toFloat(),
                            it.score.toFloat(),
                            it
                        )
                    )
                }
                values.sortBy { it.x }
                val dataSet = LineDataSet(values, "points")
                dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                dataSet.color = BLACK
                dataSet.setCircleColor(BLACK)
                dataSet.lineWidth = 3f
                dataSet.circleRadius = 6f
                dataSet.setDrawCircleHole(false)
                val dataSets = ArrayList<ILineDataSet>()
                dataSets.add(dataSet)

                val data = LineData(dataSets)
                data.setDrawValues(false)

                lineChart.axisRight.isEnabled = false
                val axisLeft = lineChart.axisLeft
                axisLeft.granularity = 1f
                axisLeft.gridColor = GRAY
                val xAxis = lineChart.xAxis
                xAxis.position = XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                xAxis.gridColor = GRAY
                if (card.points.size == 1) {
                    xAxis.axisMinimum = -1f
                    xAxis.axisMaximum = 1f
                } else {
                    xAxis.resetAxisMinimum()
                    xAxis.resetAxisMaximum()
                }

                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getPointLabel(entry: Entry): String {
                        return DATE_FORMAT.format((entry.data as Point).cdate.time)
                    }

                    override fun getFormattedValue(value: Float): String {
                        return DATE_FORMAT.format(firstDay.addDayAsNewInstance(value.toInt()).time)
                    }

                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        return getFormattedValue(value)
                    }
                }

                val chartGraphMarker = ChartGraphMarkerView(activity)
                chartGraphMarker.chartView = lineChart
                lineChart.marker = chartGraphMarker

                lineChart.data = data
                lineChart.legend.isEnabled = false
                lineChart.setPinchZoom(true)
            }
        }
    }

    /**
     * Определение надо ли для данной карты скрывать график
     *
     * @return `true`, если надо скрывать, в противном случае `false`
     */
    private fun BaseCard.isHideChart(): Boolean {
        return when (this) {
            is LinearCard -> this.points.isEmpty()
            else -> true
        }
    }
}
