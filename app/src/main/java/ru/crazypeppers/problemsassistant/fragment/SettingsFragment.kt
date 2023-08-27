package ru.crazypeppers.problemsassistant.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.databinding.FragmentSettingsBinding
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener
import java.io.File


class SettingsFragment : Fragment(), OnBackPressedListener {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.onBackPressedListener = this
        val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
        inputAdd.hide()
        activity.title = getString(R.string.settingsFragmentLabel)
        val settingsMenuItem = activity.findMenuItem(R.id.action_settings)
        if (settingsMenuItem != null) {
            settingsMenuItem.isVisible = false
        }

        binding.exportButton.setOnClickListener {
            val shareIntent = Intent().apply {
                val file = File(activity.filesDir, "data.problemAssistant")
                action = Intent.ACTION_SEND
                val uriForFile = FileProvider.getUriForFile(
                    activity,
                    activity.applicationContext.packageName + ".provider",
                    file
                )
                putExtra(Intent.EXTRA_STREAM, uriForFile)
                type = "application/problemAssistant"
            }
            startActivity(Intent.createChooser(shareIntent, getText(R.string.exportDataAction)))
        }

        binding.importButton.setOnClickListener {
            val intent = Intent().apply {
                type = "application/problemAssistant"
                action = Intent.ACTION_GET_CONTENT
            }

            startActivityForResult(
                Intent.createChooser(intent, getText(R.string.importDataAction)),
                externalImportIntentId
            )
        }
    }

    override fun onBackPressed(): Boolean {
        val activity = activity as MainActivity
        val settingsMenuItem = activity.findMenuItem(R.id.action_settings)
        if (settingsMenuItem != null) {
            settingsMenuItem.isVisible = true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == externalImportIntentId && resultCode == RESULT_OK) {
            findNavController().navigate(R.id.ImportFragment, bundleOf("Uri" to data?.data))
        }
    }

    companion object {
        private const val externalImportIntentId = 123
    }
}