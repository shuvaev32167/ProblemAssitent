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
import ru.crazypeppers.problemsassistant.data.dto.Problem
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType
import ru.crazypeppers.problemsassistant.databinding.FragmentProblemNewBinding
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener
import ru.crazypeppers.problemsassistant.util.HideInputMode

/**
 * Фрагмент, отвечающий за добавление новой проблемы
 */
class ProblemNewFragment : Fragment(), OnBackPressedListener, HideInputMode {
    private var _binding: FragmentProblemNewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProblemNewBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
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

            if (application.data.hasProblemWithName(newName)) {
                alert.show()
                return@setOnClickListener
            } else {
                application.data.add(
                    Problem(
                        newName,
                        if (binding.problemTypeSpinner.selectedItemPosition == 0)
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
