package ru.crazypeppers.problemsassistant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
                        (activity.application as DataApplication).data[positionProblem].name
                    problemName.setText(name)
                    problemName.setSelection(name.length)
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
            val newName = problemName.text.toString()

            val adb: AlertDialog.Builder = AlertDialog.Builder(activity)
            adb.setTitle(R.string.problemNameBusyTitle)
            adb.setMessage(
                String.format(
                    getString(R.string.problemNameBusyMessage),
                    newName
                )
            )
            adb.setIcon(android.R.drawable.ic_dialog_alert)
            adb.setNeutralButton(R.string.fixButton, null)
            val alert = adb.create()

            if (positionProblem != NOT_POSITION) {
                if (application.data.hasProblemWithName(
                        newName,
                        application.data[positionProblem]
                    )
                ) {
                    alert.show()
                    return@setOnClickListener
                } else {
                    application.data[positionProblem].name = newName
                }
            } else {
                if (application.data.hasProblemWithName(newName)) {
                    alert.show()
                    return@setOnClickListener
                } else {
                    application.data.add(
                        Problem(
                            newName,
                            mutableListOf()
                        )
                    )
                }
            }
            application.saveData()
            findNavController().popBackStack()
        }
    }
}
