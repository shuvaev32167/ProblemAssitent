package ru.crazypeppers.problemsassistant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.databinding.FragmentProblemEditBinding
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener
import ru.crazypeppers.problemsassistant.util.HideInputMode

/**
 * Фрагмент отвечающий за редактирование существующей проблемы.
 */
class ProblemEditFragment : Fragment(), OnBackPressedListener, HideInputMode {
    private var positionProblem = NOT_POSITION

    private var _binding: FragmentProblemEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProblemEditBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity

        if (activity is MainActivity) {
            activity.onBackPressedListener = this
            val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
            inputAdd.hide()

            val arg = arguments
            if (arg != null) {
                positionProblem = arg.getInt(PROBLEM_POSITION_TEXT, NOT_POSITION)
                if (positionProblem != NOT_POSITION) {
                    val name =
                        (activity.application as DataApplication).data[positionProblem].name
                    binding.problemName.setText(name)
                    binding.problemName.setSelection(name.length)
                }
            }

            activity.title = getString(R.string.problem_edit_fragment_label)
        }

        binding.cancelButton.setOnClickListener {
            hideInputMode(activity)
            findNavController().popBackStack()
        }

        binding.saveButton.setOnClickListener {
            hideInputMode(activity)
            val application = activity?.application as DataApplication
            val newName = binding.problemName.text.toString()

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
                application.saveData()
            }

            findNavController().popBackStack()
        }
    }
}
