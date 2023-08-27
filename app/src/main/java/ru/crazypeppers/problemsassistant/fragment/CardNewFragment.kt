package ru.crazypeppers.problemsassistant.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.dto.DescartesSquaredCard
import ru.crazypeppers.problemsassistant.data.dto.LinearCard
import ru.crazypeppers.problemsassistant.data.dto.Point
import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType
import ru.crazypeppers.problemsassistant.databinding.FragmentCardNewBinding
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener
import ru.crazypeppers.problemsassistant.util.HideInputMode

/**
 * A simple [Fragment] subclass.
 */
class CardNewFragment : Fragment(), OnBackPressedListener, HideInputMode {
    private var positionProblem = NOT_POSITION

    private var _binding: FragmentCardNewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardNewBinding.inflate(inflater, container, false)
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
        }

        val problem = application.data[positionProblem]

        if (problem.type == ProblemType.LINE) {
            activity.title = getString(R.string.advantage_disadvantageNewLabel)
            binding.layoutVariants.layoutVariants.visibility = VISIBLE
            binding.checkScoreNow.visibility = VISIBLE
        } else if (problem.type == ProblemType.DESCARTES_SQUARED) {
            activity.title = getString(R.string.cardNewLabel)
            binding.layoutVariants.layoutVariants.visibility = GONE
            binding.checkScoreNow.visibility = GONE
            binding.descartesSquaredLayout.visibility = VISIBLE
        }


        binding.checkScoreNow.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.layoutVariants.layoutVariants.visibility = GONE
                activity.title = getString(R.string.advantage_disadvantageNewLabel)
            } else {
                binding.layoutVariants.layoutVariants.visibility = VISIBLE
                setActivityTitle(binding.layoutVariants.seekBarVariants.progress - 5)
            }
        }

        binding.cancelButton.setOnClickListener {
            hideInputMode(activity)
            findNavController().popBackStack()
        }

        binding.saveButton.setOnClickListener {
            hideInputMode(activity)
            if (positionProblem == NOT_POSITION) {
                findNavController().popBackStack()
                return@setOnClickListener
            }
            val newName = binding.cardName.text.toString()

            val adb: AlertDialog.Builder = AlertDialog.Builder(activity)
            val titleId: Int
            val messageId: Int

            if (problem.type == ProblemType.LINE) {
                when {
                    binding.layoutVariants.seekBarVariants.progress > 5 -> {
                        titleId = R.string.cardAdvantageNameBusyTitle
                        messageId = R.string.cardAdvantageNameBusyMessage
                    }

                    binding.layoutVariants.seekBarVariants.progress < 5 -> {
                        titleId = R.string.cardDisadvantageNameBusyTitle
                        messageId = R.string.cardDisadvantageNameBusyMessage
                    }

                    else -> {
                        titleId = R.string.cardAdvantage_DisadvantageNameBusyTitle
                        messageId = R.string.cardAdvantage_DisadvantageNameBusyMessage
                    }
                }
            } else {
                titleId = R.string.cardDescartesSquaredNameBusyTitle
                messageId = R.string.cardDescartesSquaredNameBusyMessage
            }

            adb.setTitle(titleId)
            adb.setMessage(
                String.format(
                    getString(messageId),
                    newName
                )
            )
            adb.setIcon(android.R.drawable.ic_dialog_alert)
            adb.setNeutralButton(R.string.fixButton, null)
            val alert = adb.create()

            if (problem.hasCardWithName(newName)) {
                alert.show()
                return@setOnClickListener
            } else {
                if (problem.type == ProblemType.LINE) {
                    if (binding.checkScoreNow.isChecked) {
                        problem.add(LinearCard(newName, binding.cardDescription.text.toString()))
                    } else {
                        problem.add(
                            LinearCard(
                                cardName = newName,
                                cardDescription = binding.cardDescription.text.toString(),
                                parent = problem,
                                points = mutableListOf(
                                    Point(
                                        binding.layoutVariants.seekBarVariants.progress - 5
                                    )
                                )
                            )
                        )
                    }
                } else if (problem.type == ProblemType.DESCARTES_SQUARED) {
                    problem.add(
                        DescartesSquaredCard(
                            cardName = newName,
                            cardDescription = binding.cardDescription.text.toString(),
                            cardType = getCardTypeFromSpinner()
                        )
                    )
                }
            }

            application.saveData()
            findNavController().popBackStack()
        }

        binding.layoutVariants.seekBarVariants.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val score = progress - 5
                setActivityTitle(score)
                binding.layoutVariants.scoreSeekBar.text = score.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    /**
     * Установка заголовка активити по оценке карты
     *
     * @param score оценка карты
     */
    private fun setActivityTitle(score: Int) {
        activity?.title = when {
            score < 0 -> getString(R.string.disadvantageNewLabel)
            score > 0 -> getString(R.string.advantageNewLabel)
            else -> getString(R.string.advantage_disadvantageNewLabel)
        }
    }

    /**
     * Получения типа карты, согласно выпадающему меню [descartesSquaredQuarterSpinner]
     *
     * @return Тип карты
     */
    private fun getCardTypeFromSpinner(): CardType {
        return when (binding.descartesSquaredQuarterSpinner.selectedItemPosition) {
            0 -> CardType.SQUARE_DO_HAPPEN
            1 -> CardType.SQUARE_NOT_DO_HAPPEN
            2 -> CardType.SQUARE_DO_NOT_HAPPEN
            else -> CardType.SQUARE_NOT_DO_NOT_HAPPEN
        }
    }

}
