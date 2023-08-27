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
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.databinding.ActivityMainBinding
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener
import ru.crazypeppers.problemsassistant.util.HideInputMode


/**
 * Основная активити приложения
 */
class MainActivity : AppCompatActivity(), HideInputMode {

    var onBackPressedListener: OnBackPressedListener? = null

    /**
     * Размер экрана телефона
     */
    val windowSize = Point()

    /**
     * Меню приложения
     */
    private lateinit var menu: Menu

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)

        val display: Display = windowManager.defaultDisplay
        display.getSize(windowSize)

        binding.inputAdd.setOnClickListener {
            Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onStart() {
        super.onStart()

        val data = intent.data
        if (data != null) {
            intent.data = null
            binding.content.navHostFragment.findNavController()
                .navigate(R.id.ImportFragment, bundleOf("Uri" to data))
        }
    }

    //Заготовка под меню
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        this.menu = menu
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            this.menu = menu
        }
        return super.onPrepareOptionsMenu(menu)
    }

    //Заготовка под меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                item.isVisible = false
                binding.content.navHostFragment.findNavController().navigate(R.id.SettingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Обработка нажатия системной кнопки назад
     */
    override fun onBackPressed() {
        hideInputMode(this)
        if (onBackPressedListener?.onBackPressed() == true)
            return
        super.onBackPressed()
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
}
