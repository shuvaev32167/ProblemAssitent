@file:JvmName("FloatExtension")

package ru.crazypeppers.problemsassistant.extension

import java.math.RoundingMode

/**
 * Округление до [numFractionDigits] знаков после запятой.
 *
 * @param numFractionDigits число знаков после запятой, которое надо оставить.
 */
fun Float.roundTo(numFractionDigits: Int) =
    toBigDecimal().setScale(numFractionDigits, RoundingMode.UP).toFloat()

/**
 * Преобразование в строку с округлением дл [numFractionDigits] знаков после запятой.
 *
 * @param numFractionDigits число знаков после запятой, которое надо оставить.
 */
fun Float.toStringRound(numFractionDigits: Int) =
    toBigDecimal().setScale(numFractionDigits, RoundingMode.UP).toString()
