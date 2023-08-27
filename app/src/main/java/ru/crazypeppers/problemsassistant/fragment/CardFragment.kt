package ru.crazypeppers.problemsassistant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.CARD_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.DATE_FORMAT
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.dto.BaseCard
import ru.crazypeppers.problemsassistant.data.dto.DescartesSquaredCard
import ru.crazypeppers.problemsassistant.data.dto.LinearCard
import ru.crazypeppers.problemsassistant.data.dto.Point
import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.databinding.FragmentCardBinding
import ru.crazypeppers.problemsassistant.extension.informationBuilder
import ru.crazypeppers.problemsassistant.extension.isToday
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener
import ru.crazypeppers.problemsassistant.util.fromHtml

/**
 * Фрагмент отвечающий за оценивание карты
 */
class CardFragment : Fragment(), OnBackPressedListener {
    private var positionProblem = NOT_POSITION
    private var positionCard = NOT_POSITION

    private var _binding: FragmentCardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.onBackPressedListener = this
        val application = activity.application as DataApplication

        val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
        inputAdd.hide()

        val arg = arguments
        if (arg != null) {
            positionProblem = arg.getInt(PROBLEM_POSITION_TEXT, NOT_POSITION)
            positionCard = arg.getInt(CARD_POSITION_TEXT, NOT_POSITION)
        }

        binding.layoutVariants.seekBarVariants.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val score = progress - 5
                activity.title = when {
                    score > 0 -> getString(R.string.advantageFragmentLabel)
                    score < 0 -> getString(R.string.disadvantageFragmentLabel)
                    else -> getString(R.string.advantage_disadvantageFragmentLabel)
                }
                binding.layoutVariants.scoreSeekBar.text = score.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        if (positionProblem != NOT_POSITION && positionCard != NOT_POSITION) {
            val card = application.data[positionProblem][positionCard]
            activity.title = when (card.type) {
                CardType.LINEAR_ADVANTAGE -> getString(R.string.advantageFragmentLabel)
                CardType.LINEAR_DISADVANTAGE -> getString(R.string.disadvantageFragmentLabel)
                CardType.NONE -> getString(R.string.advantage_disadvantageFragmentLabel)
                else -> getString(R.string.cardLabel)
            }
            binding.cardName.text = card.name
            binding.cardNameTitle.text = String.format(
                getString(R.string.cardNameTitle),
                getString(
                    when (card.type) {
                        CardType.LINEAR_ADVANTAGE -> R.string.advantageR
                        CardType.LINEAR_DISADVANTAGE -> R.string.disadvantageR
                        else -> R.string.advantage_disadvantageR
                    }
                )
            )
            if (card.description.isBlank()) {
                binding.cardDescriptionLayout.visibility = GONE
            } else {
                binding.cardDescriptionLayout.visibility = VISIBLE
                binding.cardDescription.text = card.description
                binding.cardDescriptionTitle.text = String.format(
                    getString(R.string.cardDescriptionTitle),
                    getString(
                        when (card.type) {
                            CardType.LINEAR_ADVANTAGE -> R.string.advantageR
                            CardType.LINEAR_DISADVANTAGE -> R.string.disadvantageR
                            else -> R.string.advantage_disadvantageR
                        }
                    )
                )
            }
            if (card is LinearCard) {
                processingAppearanceOfElementsDependingOnAvailabilityOfPoints(
                    card.points
                )

                if (card.points.isNotEmpty() && card.points.first().cdate.isToday()) {
                    binding.confirmButton.visibility = GONE
                }
            } else if (card is DescartesSquaredCard) {
                binding.newLayoutButtons.visibility = GONE
            }

            binding.informationText.text = getInformationTextText(card)
        }

        binding.cancelButton.setOnClickListener {
            onBackPressed()
        }

        binding.saveButton.setOnClickListener {
            val score = binding.layoutVariants.seekBarVariants.progress - 5
            val card = application.data[positionProblem][positionCard] as LinearCard
            card.add(Point(score))
            application.saveData()
            binding.informationText.text = getInformationTextText(card)
            onBackPressed()
        }

        binding.confirmButton.setOnClickListener {
            val card = application.data[positionProblem][positionCard] as LinearCard
            val prevPoint = card.points.first()
            card.add(Point(card.points.first().score))
            application.saveData()
            binding.informationText.text = getInformationTextText(card)
            binding.confirmButton.visibility = GONE

            val informationBuilder =
                AlertDialog.Builder(context ?: activity).informationBuilder()
            informationBuilder.setMessage(
                getString(
                    R.string.confirmButtonInformationAction,
                    prevPoint.score,
                    DATE_FORMAT.format(prevPoint.cdate.time)
                )
            )
            informationBuilder.create().show()
        }

        binding.makeNewScoreButton.setOnClickListener {
            binding.newLayoutButtons.visibility = GONE
            binding.informationText.visibility = GONE
            binding.frameVariant.visibility = VISIBLE
            binding.layoutButtons.visibility = VISIBLE
            val card = application.data[positionProblem][positionCard]
            if (card is LinearCard)
                binding.layoutVariants.seekBarVariants.progress =
                    (card.points.maxByOrNull { it.cdate }?.score ?: 0) + 5
        }
    }

    /**
     * Определение информационного текста, на основе данных карты [card].
     * В зависимости от типа проблемы ([BaseCard.parent]), будет вернут или `String` или `Span`.
     *
     * @param card карта
     * @return текст для информационного сообщения
     */
    private fun getInformationTextText(card: BaseCard): CharSequence {
        return if (card is LinearCard)
            if (card.points.isNotEmpty()) {
                String.format(
                    getString(R.string.informationText),
                    when (card.type) {
                        CardType.LINEAR_ADVANTAGE -> getString(R.string.advantage)
                        CardType.LINEAR_DISADVANTAGE -> getString(R.string.disadvantage)
                        else -> getString(R.string.advantage_disadvantage)
                    },
                    card.name,
                    card.points.first().score,
                    DATE_FORMAT.format(card.points.first().cdate.time)
                )
            } else {
                String.format(
                    getString(R.string.informationTextNotScore),
                    card.name
                )
            } else {
            fromHtml(
                getString(
                    R.string.informationDescartesSquaredText,
                    getString(
                        when (card.type) {
                            CardType.SQUARE_DO_HAPPEN -> R.string.descartesSquaredIQuarterFotGetString
                            CardType.SQUARE_NOT_DO_HAPPEN -> R.string.descartesSquaredIIQuarterFotGetString
                            CardType.SQUARE_DO_NOT_HAPPEN -> R.string.descartesSquaredIIIQuarterFotGetString
                            else -> R.string.descartesSquaredIVQuarterFotGetString
                        }
                    )
                )
            )
        }
    }

    override fun onBackPressed(): Boolean {
        return if (binding.newLayoutButtons.visibility != VISIBLE && binding.informationText.visibility != VISIBLE) {
            binding.newLayoutButtons.visibility = VISIBLE
            binding.informationText.visibility = VISIBLE
            binding.frameVariant.visibility = GONE
            binding.layoutButtons.visibility = GONE
            if (positionProblem != NOT_POSITION && positionCard != NOT_POSITION) {
                val card =
                    (requireActivity().application as DataApplication).data[positionProblem][positionCard]
                if (card is LinearCard)
                    processingAppearanceOfElementsDependingOnAvailabilityOfPoints(
                        card.points
                    )
            }
            true
        } else {
            false
        }
    }

    /**
     * Обработка внешнего вида элементов, в зависимости от наличия оценок
     *
     * @param points оценки карты
     */
    private fun processingAppearanceOfElementsDependingOnAvailabilityOfPoints(points: List<Point>) {
        if (points.isEmpty()) {
            binding.layoutVariants.seekBarVariants.progress = 5
            binding.confirmButton.visibility = GONE
            binding.makeNewScoreButton.setText(R.string.makeScoreButton)
        } else {
            binding.confirmButton.visibility = VISIBLE
            binding.makeNewScoreButton.setText(R.string.makeNewScoreButton)
        }
    }
}
