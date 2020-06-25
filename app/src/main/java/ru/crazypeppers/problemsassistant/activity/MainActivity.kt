package ru.crazypeppers.problemsassistant.activity

import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener


/**
 * Оснавная активити приложения
 */
class MainActivity : AppCompatActivity() {

    var onBackPressedListener: OnBackPressedListener? = null

    /**
     * Размер экрана телефона
     */
    val windowSize = Point()

    /**
     * Меню приложения
     */
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val display: Display = windowManager.defaultDisplay
        display.getSize(windowSize)

        inputAdd.setOnClickListener {
            Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onStart() {
        super.onStart()

        val data = intent.data
        if (data != null) {
            intent.data = null
            nav_host_fragment.findNavController()
                .navigate(R.id.ImportFragment, bundleOf("Uri" to intent.data))
        }
    }

    /**
     * Поиск элемента меню ([menu]]) по id его ресурса
     *
     * @param resId id ресурса элемента меню ([menu]])
     * @return элемент меню, или `null`, если не удалось его найти
     */
    fun findMenuItem(resId: Int): MenuItem? {
        return menu.findItem(resId)
    }

    //Заготовка под меню
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        return true
    }

    //Заготовка под меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                item.isVisible = false
                nav_host_fragment.findNavController().navigate(R.id.SettingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Обработка нажатия системной кнопки назад
     */
    override fun onBackPressed() {
        if (onBackPressedListener?.onBackPressed() == true)
            return
        super.onBackPressed()
    }
}
