package ru.crazypeppers.problemsassistant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.CARD_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.dto.BaseCard
import ru.crazypeppers.problemsassistant.data.dto.Problem
import ru.crazypeppers.problemsassistant.data.enumiration.CardType
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType
import ru.crazypeppers.problemsassistant.databinding.FragmentCardEditBinding
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener
import ru.crazypeppers.problemsassistant.util.HideInputMode

/**
 * Фрагмент отвечающий за редактирование существующей карты или добавление новой.
 */
class CardEditFragment : Fragment(), OnBackPressedListener, HideInputMode {
    private var positionProblem = NOT_POSITION
    private var positionCard = NOT_POSITION
    private lateinit var card: BaseCard
    private lateinit var problem: Problem

    private var _binding: FragmentCardEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardEditBinding.inflate(inflater, container, false)
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
            problem = application.data[positionProblem]
            card = application.data[positionProblem][positionCard]
            binding.cardName.setText(card.name)
            binding.cardName.setSelection(card.name.length)
            binding.cardDescription.setText(card.description)

            if (problem.type == ProblemType.LINE) {
                binding.descartesSquaredLayout.visibility = GONE
            } else if (problem.type == ProblemType.DESCARTES_SQUARED) {
                binding.descartesSquaredLayout.visibility = VISIBLE
            }
        }

        setActivityTitle(card)

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

            val problem = application.data[positionProblem]
            if (problem.hasCardWithName(newName, card)) {
                val adb: AlertDialog.Builder = AlertDialog.Builder(activity)
                val titleId: Int
                val messageId: Int
                if (problem.type == ProblemType.LINE) {
                    when (card.type) {
                        CardType.LINEAR_ADVANTAGE -> {
                            titleId = R.string.cardAdvantageNameBusyTitle
                            messageId = R.string.cardAdvantageNameBusyMessage
                        }
                        CardType.LINEAR_DISADVANTAGE -> {
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
                adb.create().show()
                return@setOnClickListener
            } else {
                card.name = newName
                card.description = binding.cardDescription.text.toString()
                if (problem.type == ProblemType.DESCARTES_SQUARED) {
                    card.type = getCardTypeFromSpinner()
                }
            }
            application.saveData()
            findNavController().popBackStack()
        }
    }

    /**
     * Установка заголовка активити по карте
     *
     * @param card карта
     */
    private fun setActivityTitle(card: BaseCard) {
        activity?.title = when (card.type) {
            CardType.LINEAR_ADVANTAGE -> getString(R.string.advantageEditLabel)
            CardType.LINEAR_DISADVANTAGE -> getString(R.string.disadvantageEditLabel)
            CardType.NONE -> getString(R.string.advantage_disadvantageEditLabel)
            else -> getString(R.string.cardEditLabel)
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
