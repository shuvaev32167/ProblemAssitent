package ru.crazypeppers.problemsassistant.data

import java.text.DateFormat

/**
 * Наименование поля для передачи выбранной позиции проблемы
 */
const val PROBLEM_POSITION_TEXT = "problemPosition"

/**
 * Наименование поля для передачи выбранной позиции карты
 */
const val CARD_POSITION_TEXT = "cardPosition"

/**
 * Значение не переданной позиции
 */
const val NOT_POSITION = -1

/**
 * Наименование поля для передачи лямбда-выражений
 */
const val LAMBDA_TEXT = "lambda"

/**
 * Форматор дат
 */
val DATE_FORMAT: DateFormat = DateFormat.getDateInstance(DateFormat.SHORT)

/**
 * Тэг, для логирования
 */
const val TAG = "ProblemAssistant"