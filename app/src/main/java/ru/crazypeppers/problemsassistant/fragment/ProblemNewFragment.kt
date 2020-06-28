package ru.crazypeppers.problemsassistant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_problem_new.*
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.dto.Problem
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener
import ru.crazypeppers.problemsassistant.util.HideInputMode

/**
 * Фрагмент, отвечающий за добавление новой проблемы
 */
class ProblemNewFragment : Fragment(), OnBackPressedListener, HideInputMode {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_problem_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity


        if (activity is MainActivity) {
            activity.onBackPressedListener = this
            val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
            inputAdd.hide()
            activity.title = getString(R.string.problem_new_fragment_label)
        }

        cancelButton.setOnClickListener {
            hideInputMode(activity)
            findNavController().popBackStack()
        }

        saveButton.setOnClickListener {
            hideInputMode(activity)
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

            if (application.data.hasProblemWithName(newName)) {
                alert.show()
                return@setOnClickListener
            } else {
                application.data.add(
                    Problem(
                        newName,
                        if (problemTypeSpinner.selectedItemPosition == 0)
                            ProblemType.LINE
                        else
                            ProblemType.DESCARTES_SQUARED
                    )
                )
            }
            application.saveData()
            findNavController().popBackStack()
        }
    }
}
