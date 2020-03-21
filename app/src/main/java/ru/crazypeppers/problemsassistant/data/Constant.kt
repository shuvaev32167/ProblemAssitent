package ru.crazypeppers.problemsassistant.data

import java.text.DateFormat

/**
 * Наименование поля для передачи выбраной позиции проблемы
 */
const val PROBLEM_POSITION_TEXT = "problemPosition"

/**
 * Наименование поля для передачи выбраной позиции карты
 */
const val CARD_POSITION_TEXT = "cardPosition"

/**
 * Значение не переданной позиции
 */
const val NOT_POSITION = -1

/**
 * Наименование поля для передачи лямбда-варажений
 */
const val LAMBDA_TEXT = "lambda"

val DATE_FORMAT: DateFormat = DateFormat.getDateInstance(DateFormat.SHORT)