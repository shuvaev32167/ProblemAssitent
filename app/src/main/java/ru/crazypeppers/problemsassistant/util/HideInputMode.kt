package ru.crazypeppers.problemsassistant.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

/**
 * Скрытие клавиатуры
 */
interface HideInputMode {
    /**
     * Скрыть клавиатуру
     *
     * @param activity активити, для которой происходит скрытие клавиатуры
     */
    fun hideInputMode(activity: Activity?) {
        val imm: InputMethodManager? =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(
            activity?.window?.decorView?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}