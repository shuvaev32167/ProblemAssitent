package ru.crazypeppers.problemsassistant.listener

interface OnBackPressedListener {
    /**
     * Обработка нажатия на кнопку назад.
     * Если из метода вернулся `true`, то обработку стоит прекратить
     *
     * @return `true`, если обработку кнопки назад стоит прекратить, в противном случае — `false`
     */
    fun onBackPressed(): Boolean {
        return false
    }
}