@file:JvmName("AlertDialogExtension")

package ru.crazypeppers.problemsassistant.extension

import androidx.appcompat.app.AlertDialog
import ru.crazypeppers.problemsassistant.R

/**
 * Добавление в билдер ([AlertDialog.Builder]) свойств, характерных для информационных сообщений
 *
 * @return Билдер ([AlertDialog.Builder]) с заполненными полями
 */
fun AlertDialog.Builder.informationBuilder(): AlertDialog.Builder {
    this.setTitle(R.string.informationTitle)
    this.setIcon(android.R.drawable.ic_dialog_info)
    this.setNeutralButton(R.string.okButton, null)
    return this
}