package ru.crazypeppers.problemsassistant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_problem_edit.*
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.dto.Problem

/**
 * Фрагмент отвечающий за редактирование существующей проблемы или добавление новой проблемы.
 */
class ProblemEditFragment : Fragment() {
    private var positionProblem = NOT_POSITION

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_problem_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity


        if (activity is MainActivity) {
            val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
            inputAdd.hide()

            val arg = arguments
            if (arg != null) {
                positionProblem = arg.getInt(PROBLEM_POSITION_TEXT, NOT_POSITION)
                if (positionProblem != NOT_POSITION) {
                    val name =
                        (activity.application as DataApplication).data[positionProblem].problemName
                    cardName.setText(name)
                    cardName.setSelection(name.length)
                }
            }

            if (positionProblem != NOT_POSITION) {
                activity.title = getString(R.string.problem_edit_fragment_label)
            } else {
                activity.title = getString(R.string.problem_new_fragment_label)
            }
        }

        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        saveButton.setOnClickListener {
            val application = activity?.application as DataApplication
            if (positionProblem != NOT_POSITION) {
                application.data[positionProblem].problemName =
                    cardName.text.toString()
            } else {
                application.data.add(
                    Problem(
                        cardName.text.toString(),
                        mutableListOf()
                    )
                )
            }
            application.saveData()
            findNavController().popBackStack()
        }
    }
}
