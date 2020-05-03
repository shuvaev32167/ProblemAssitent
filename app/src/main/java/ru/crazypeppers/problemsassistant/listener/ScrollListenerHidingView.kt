package ru.crazypeppers.problemsassistant.listener

import android.view.View
import android.widget.AbsListView
import ru.crazypeppers.problemsassistant.activity.MainActivity

/**
 * Слушатель, скрывающий [view], при скролиге списка
 *
 * @property activity активити, на которой скрываем
 * @property view элемент, для плавного скрытия
 */
class ScrollListenerHidingView(val activity: MainActivity, val view: View) :
    AbsListView.OnScrollListener {

    override fun onScroll(
        view: AbsListView?,
        firstVisibleItem: Int,
        visibleItemCount: Int,
        totalItemCount: Int
    ) {
    }

    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
        val btnInitPosY: Int = this.view.scrollY
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            this.view.animate().cancel()
            this.view.animate().translationYBy(activity.windowSize.y - this.view.y)
        } else {
            this.view.animate().cancel()
            this.view.animate().translationY(btnInitPosY.toFloat())
        }
    }
}