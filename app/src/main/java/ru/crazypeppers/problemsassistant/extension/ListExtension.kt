@file:JvmName("ListExtension")

package ru.crazypeppers.problemsassistant.extension

import android.util.Log
import ru.crazypeppers.problemsassistant.data.TAG

/**
 * Удаление элемента по [индексу][position], для мутабельного списка.
 * В случае вызова метода у иммутабельного — ничего не произойдёт.
 * Проверка на мутабельность делается методом [isMutable].
 *
 * @param E типизатор списка
 * @param position позиция, элемент на которой надо удалить
 */
fun <E> List<E>.removeAt(position: Int) {
    if (this.isMutable()) {
        (this as MutableList).removeAt(position)
    }
}

/**
 * Добавление [элемента][element], для мутабельного списка.
 * В случае вызова метода у иммутабельного — ничего не произойдёт.
 * Проверка на мутабельность делается методом [isMutable].
 *
 * @param E типизатор списка
 * @param element элемент для добавления к списку
 */
fun <E> List<E>.add(element: E) {
    if (this.isMutable()) {
        (this as MutableList).add(element)
    }
}

/**
 * Очистка содержимого для мутабельного списка.
 * В случае вызова метода у иммутабельного — ничего не произойдёт.
 * Проверка на мутабельность делается методом [isMutable].
 *
 * @param E типизатор списка
 */
fun <E> List<E>.clear() {
    if (this.isMutable()) {
        (this as MutableList).clear()
    }
}

/**
 * Добавление [списка][list] к исходному списку, для мутабельного списка.
 * В случае вызова метода у иммутабельного — ничего не произойдёт.
 * Проверка на мутабельность делается методом [isMutable].
 *
 * @param E типизатор списка
 * @param list список для добавления
 */
fun <E> List<E>.addAll(list: List<E>) {
    if (this.isMutable()) {
        (this as MutableList).addAll(list)
    }
}

/**
 * Проверка на мутабельность списка
 *
 * @param E типизатор списка
 * @return `true`, если список мутабельный, иначе — `false`
 */
fun <E> List<E>.isMutable(): Boolean {
    if (this is ArrayList) {
        return true
    } else if (this is MutableList) {
        try {
            this.add(this.first())
            this.removeAt(this.lastIndex)
        } catch (e: UnsupportedOperationException) {
            Log.e(TAG, "Попытка изменить не изменяемую коллекцию")
            return false
        }
        return true
    }
    return false
}