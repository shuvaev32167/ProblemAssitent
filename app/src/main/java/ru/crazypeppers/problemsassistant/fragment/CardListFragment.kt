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
import ru.crazypeppers.problemsassistant.adapter.CardArrayAdapter
import ru.crazypeppers.problemsassistant.data.CARD_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.NOT_POSITION
import ru.crazypeppers.problemsassistant.data.PROBLEM_POSITION_TEXT
import ru.crazypeppers.problemsassistant.data.dto.BaseCard
import ru.crazypeppers.problemsassistant.data.enumiration.ProblemType
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener
import ru.crazypeppers.problemsassistant.listener.ScrollListenerHidingView


/**
 * Фрагмент отвечающий за работу со списком карт
 */
class CardListFragment : ListFragment(), OnBackPressedListener {
    private var problemPosition = NOT_POSITION

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity

        if (activity is MainActivity) {
            activity.onBackPressedListener = this

            val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
            inputAdd.setOnClickListener {
                findNavController().navigate(
                    R.id.action_CardListFragment_to_CardNewFragment,
                    arguments
                )
            }

            inputAdd.show()
            val arg = arguments as Bundle
            val data = (activity.application as DataApplication).data
            problemPosition = arg.getInt(PROBLEM_POSITION_TEXT)
            val problem = data[problemPosition]
            listAdapter =
                CardArrayAdapter(activity, problem.cards)

            if (problem.type == ProblemType.LINE) {
                activity.title = getString(R.string.card_list_fragment_label)
            } else if (problem.type == ProblemType.DESCARTES_SQUARED) {
                activity.title = getString(R.string.cardListLabel)
            }


            listView.setOnItemLongClickListener { parent, viewListItem, position, id ->
                val popupMenu = PopupMenu(activity, viewListItem)
                popupMenu.menu.add(1, 0, 1, getString(R.string.popupEdit))
                popupMenu.menu.add(1, 1, 2, getString(R.string.popupDelete))
                if (problem.type == ProblemType.LINE) {
                    popupMenu.menu.add(1, 2, 3, getString(R.string.graphScoreChanged))
                }
                popupMenu.show()

                popupMenu.setOnMenuItemClickListener {
                    val application = activity.application as DataApplication
                    when (it.itemId) {
                        0 -> {
                            val bundle = Bundle()
                            bundle.putInt(PROBLEM_POSITION_TEXT, problemPosition)
                            bundle.putInt(CARD_POSITION_TEXT, position)
                            findNavController().navigate(
                                R.id.action_CardListFragment_to_CardEditFragment,
                                bundle
                            )
                        }
                        1 -> {
                            val adb: AlertDialog.Builder = AlertDialog.Builder(activity)
                            adb.setTitle(R.string.removeItemConfirm)
                            adb.setMessage(
                                String.format(
                                    getString(R.string.removeItemConfirmMessage),
                                    (listAdapter?.getItem(position) as BaseCard).name
                                )
                            )
                            adb.setIcon(android.R.drawable.ic_dialog_alert)
                            adb.setPositiveButton(R.string.yesButton) { dialog, which ->
                                when (which) {
                                    // положительная кнопка
                                    Dialog.BUTTON_POSITIVE -> {
                                        application.data[problemPosition].removeAt(position)
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
                            bundle.putInt(PROBLEM_POSITION_TEXT, problemPosition)
                            bundle.putInt(CARD_POSITION_TEXT, position)
                            findNavController().navigate(
                                R.id.action_CardListFragment_to_AnalyseCardFragment,
                                bundle
                            )
                        }
                    }

                    true
                }
                true
            }

            listView.setOnScrollListener(ScrollListenerHidingView(activity, inputAdd))
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        val bundle = Bundle()
        bundle.putInt(PROBLEM_POSITION_TEXT, problemPosition)
        bundle.putInt(CARD_POSITION_TEXT, position)
        findNavController().navigate(R.id.action_CardListFragment_to_CardFragment, bundle)
    }
}
