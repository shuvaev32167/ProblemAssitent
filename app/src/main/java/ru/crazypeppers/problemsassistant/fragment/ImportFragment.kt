package ru.crazypeppers.problemsassistant.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_import.*
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.enumiration.ImportType
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener
import ru.crazypeppers.problemsassistant.util.readDataFromBufferedReader
import java.io.BufferedReader
import java.io.InputStreamReader

class ImportFragment : Fragment(), OnBackPressedListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_import, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.onBackPressedListener = this
        val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
        inputAdd.hide()
        activity.title = getString(R.string.importFragmentLabel)
        val uri = requireArguments().getParcelable<Uri>("Uri")!!

        importButton.setOnClickListener {
            val importType: ImportType = when {
                fullReplaceRadioButton.isChecked -> {
                    ImportType.FULL_REPLACE
                }
                enrichmentRadioButton.isChecked -> {
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