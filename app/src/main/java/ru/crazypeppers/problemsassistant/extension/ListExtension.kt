@file:JvmName("ListExtension")

package ru.crazypeppers.problemsassistant.extension

fun <E> List<E>.removeAt(position: Int) {
    if (this is MutableList) {
        this.removeAt(position)
    }
}

fun <E> List<E>.add(element: E) {
    if (this is MutableList) {
        this.add(element)
    }
}

fun <E> List<E>.clear() {
    if (this is MutableList) {
        this.clear()
    }
}