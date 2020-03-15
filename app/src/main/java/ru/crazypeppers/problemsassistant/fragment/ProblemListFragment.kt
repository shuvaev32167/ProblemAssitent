package ru.crazypeppers.problemsassistant.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.ListFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.crazypeppers.problemsassistant.DataApplication
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.activity.MainActivity
import ru.crazypeppers.problemsassistant.adapter.ProblemArrayAdapter
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.dto.Problem

/**
 * Фрагмент отвечающий за работу со списком проблем
 */
class ProblemListFragment : ListFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity
        if (activity is MainActivity) {
            activity.title = getString(R.string.problem_list_fragment_label)

            val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
            inputAdd.setOnClickListener {
                findNavController().navigate(R.id.action_ProblemListFragment_to_ProblemEditFragment)
            }

            inputAdd.show()
            val data = (activity.application as DataApplication).data
            listAdapter = ProblemArrayAdapter(activity, data.problems)

            listView.setOnItemLongClickListener { parent, viewListItem, position, id ->
                val popupMenu = PopupMenu(activity, viewListItem)
                popupMenu.menu.add(1, 0, 1, getString(R.string.popupEdit))
                popupMenu.menu.add(1, 1, 2, getString(R.string.popupDelete))
                popupMenu.menu.add(1, 2, 3, getString(R.string.analyze))
                popupMenu.show()

                popupMenu.setOnMenuItemClickListener {
                    val application = (activity.application as DataApplication)
                    when (it.itemId) {
                        0 -> {
                            val bundle = Bundle()
                            bundle.putInt(PROBLEM_POSITION_TEXT, position)
                            findNavController().navigate(
                                R.id.action_ProblemListFragment_to_ProblemEditFragment,
                                bundle
                            )
                        }
                        1 -> {
                            val adb: AlertDialog.Builder = AlertDialog.Builder(activity)
                            adb.setTitle(R.string.removeItemConfirm)
                            adb.setMessage(
                                String.format(
                                    getString(R.string.removeItemConfirmMessage),
                                    (listAdapter?.getItem(position) as Problem).problemName
                                )
                            )
                            adb.setIcon(android.R.drawable.ic_dialog_alert)
                            adb.setPositiveButton(R.string.yesButton) { dialog, which ->
                                when (which) {
                                    // положительная кнопка
                                    Dialog.BUTTON_POSITIVE -> {
                                        application.data.removeAt(position)
                                        application.saveData()
                                        (listAdapter as ArrayAdapter<*>).notifyDataSetChanged()
                                    }
                                }
                            }
                            adb.setNegativeButton(R.string.noButton, null)
                            adb.create().show()
                        }
                        2 -> {
                            val bundle = Bundle()
                            bundle.putInt(PROBLEM_POSITION_TEXT, position)
                            findNavController().navigate(
                                R.id.action_ProblemListFragment_to_AnalyzeProblemFragment,
                                bundle
                            )
                        }
                    }

                    true
                }
                /**/
                true
            }
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        val bundle = Bundle()
        bundle.putInt(PROBLEM_POSITION_TEXT, position)

        findNavController().navigate(R.id.action_ProblemListFragment_to_CardListFragment, bundle)
    }
}
