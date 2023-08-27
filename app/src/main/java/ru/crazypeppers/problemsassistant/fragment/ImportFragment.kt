package ru.crazypeppers.problemsassistant.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.databinding.FragmentImportBinding
import ru.crazypeppers.problemsassistant.enumiration.ImportType
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener
import ru.crazypeppers.problemsassistant.util.readDataFromBufferedReader
import java.io.BufferedReader
import java.io.InputStreamReader

class ImportFragment : Fragment(), OnBackPressedListener {
    private var _binding: FragmentImportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImportBinding.inflate(inflater, container, false)
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
        val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
        inputAdd.hide()
        activity.title = getString(R.string.importFragmentLabel)
        val uri = requireArguments().getParcelable<Uri>("Uri")!!

        binding.importButton.setOnClickListener {
            val importType: ImportType = when {
                binding.fullReplaceRadioButton.isChecked -> {
                    ImportType.FULL_REPLACE
                }

                binding.enrichmentRadioButton.isChecked -> {
                    ImportType.ENRICHMENT
                }

                else -> {
                    ImportType.ONLY_NEW
                }
            }

            activity.contentResolver.openInputStream(uri).use { inputStream ->
                InputStreamReader(inputStream!!).use { inputStreamReader ->
                    BufferedReader(inputStreamReader).use { bufferedReader ->
                        val data = readDataFromBufferedReader(bufferedReader)
                        val dataApplication = activity.application as DataApplication
                        dataApplication.data.replaceProblems(data.problems, importType)
                        dataApplication.saveData()
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
}