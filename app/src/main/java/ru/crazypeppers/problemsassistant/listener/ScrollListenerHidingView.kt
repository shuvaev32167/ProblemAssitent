package ru.crazypeppers.problemsassistant.listener

import android.app.Activity
import android.graphics.Point
import android.view.Display
import android.view.View
import android.widget.AbsListView

/**
 * Слушатель, скрывающий [view], при скролиге списка
 *
 * @property activity активити, на которой скрываем
 * @property view элемент, для плавного скрытия
 */
class ScrollListenerHidingView(val activity: Activity, val view: View) :
    AbsListView.OnScrollListener {
    private val display: Display = activity.windowManager.defaultDisplay
    private val size = Point()

    init {
        display.getSize(size)
    }

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
            this.view.animate().translationYBy(size.y - this.view.y)
        } else {
            this.view.animate().cancel()
            this.view.animate().translationY(btnInitPosY.toFloat())
        }
    }
}