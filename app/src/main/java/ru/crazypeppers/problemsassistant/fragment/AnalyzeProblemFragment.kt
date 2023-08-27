package ru.crazypeppers.problemsassistant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.databinding.FragmentAnalyzeProblemBinding
import ru.crazypeppers.problemsassistant.fragment.tabled.AnalyzeProblemSectionsPagerAdapter
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener


/**
 * Фрагмент, отвечающий за анализ проблемы
 */
class AnalyzeProblemFragment : Fragment(), OnBackPressedListener {

    private var _binding: FragmentAnalyzeProblemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyzeProblemBinding.inflate(inflater, container, false)
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
        activity.title = getString(R.string.analyzeProblemFragmentLabel)

        val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
        inputAdd.hide()

        binding.viewPager.adapter =
            AnalyzeProblemSectionsPagerAdapter(
                childFragmentManager,
                activity,
                arguments,
                (activity.application as DataApplication).data[requireArguments().getInt(
                    PROBLEM_POSITION_TEXT,
                    0
                )]
            )

        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}
