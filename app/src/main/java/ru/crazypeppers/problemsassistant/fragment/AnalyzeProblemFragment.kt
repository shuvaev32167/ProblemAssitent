package ru.crazypeppers.problemsassistant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_analyze_problem.*
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.fragment.tabled.AnalyzeProblemSectionsPagerAdapter
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener


/**
 * Фрагмент, отвечающий за анализ проблемы
 */
class AnalyzeProblemFragment : Fragment(), OnBackPressedListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analyze_problem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.onBackPressedListener = this
        activity.title = getString(R.string.analyzeProblemFragmentLabel)

        val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
        inputAdd.hide()

        viewPager.adapter =
            AnalyzeProblemSectionsPagerAdapter(childFragmentManager, activity, arguments)

        tabLayout.setupWithViewPager(viewPager)
    }
}
