package ru.crazypeppers.problemsassistant.activity

import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.dp
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener
import ru.crazypeppers.problemsassistant.px

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

        MobileAds.initialize(this) {}
        adView.loadAd(AdRequest.Builder().build())
        inputAdd.y -= calculateAdBannerHeight()
    }

    /**
     * Расчёт размера высоты рекламного баннера
     *
     * @return значение высоты рекламного баннера в `px`
     */
    private fun calculateAdBannerHeight(): Int {
        val adSize = adView.adSize
        if (adSize == AdSize.BANNER) {
            return 50.px
        } else if (adSize == AdSize.SMART_BANNER) {
            val windowHeight = windowSize.y.dp
            return when {
                windowHeight <= 400 -> 32.px
                windowHeight <= 720 -> 50.px
                windowHeight > 720 -> 90.px
                else -> 0
            }
        }
        return 0
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
