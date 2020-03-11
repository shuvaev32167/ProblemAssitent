package ru.crazypeppers.problemsassistant.fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
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

/**
 * Фрагмент отвечающий за работу со списком карт
 */
class CardListFragment : ListFragment() {
    private var problemPosition = NOT_POSITION

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity

        if (activity is MainActivity) {
            activity.title = getString(R.string.card_list_fragment_label)

            val inputAdd = activity.findViewById<FloatingActionButton>(R.id.inputAdd)
            inputAdd.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt(PROBLEM_POSITION_TEXT, problemPosition)
                findNavController().navigate(
                    R.id.action_CardListFragment_to_CardEditFragment,
                    bundle
                )
            }

            inputAdd.show()
            val arg = arguments
            if (arg != null) {
                val data = (activity.application as DataApplication).data
                problemPosition = arg.getInt(PROBLEM_POSITION_TEXT)
                listAdapter =
                    CardArrayAdapter(activity, data[problemPosition].cards) { it.cardName }
            }


            listView.setOnItemLongClickListener { parent, viewListItem, position, id ->
                val popupMenu = PopupMenu(activity, viewListItem)
                popupMenu.menu.add(1, 0, 1, getString(R.string.popupEdit))
                popupMenu.menu.add(1, 1, 2, getString(R.string.popupDelete))
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
                            application.data[problemPosition].removeAt(position)
                            application.saveData()
                            (listAdapter as ArrayAdapter<*>).notifyDataSetChanged()
                        }
                    }

                    true
                }
                true
            }
        }

//        view.findViewById<Button>(R.id.button_second).setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_ProblemListFragment)
//        }


    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        val bundle = Bundle()
        bundle.putInt(PROBLEM_POSITION_TEXT, problemPosition)
        bundle.putInt(CARD_POSITION_TEXT, position)
        findNavController().navigate(R.id.action_CardListFragment_to_CardFragment, bundle)
    }
}
