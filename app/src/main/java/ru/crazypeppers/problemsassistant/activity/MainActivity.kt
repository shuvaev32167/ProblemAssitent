package ru.crazypeppers.problemsassistant.activity

import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
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

    //Заготовка под меню
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //Заготовка под меню
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
//            R.id.action_settings -> true
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
