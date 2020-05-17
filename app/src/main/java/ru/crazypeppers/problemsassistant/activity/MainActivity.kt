package ru.crazypeppers.problemsassistant.activity

import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize.FULL_WIDTH
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import ru.crazypeppers.problemsassistant.BuildConfig
import ru.crazypeppers.problemsassistant.R
import ru.crazypeppers.problemsassistant.listener.OnBackPressedListener
import ru.crazypeppers.problemsassistant.util.dp
import ru.crazypeppers.problemsassistant.util.px
import ru.crazypeppers.problemsassistant.util.toAdSize

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

        val bannerSize = calculateAdBannerSize()
        val adView = AdView(this)
        adLayout.addView(adView)
        adView.adSize = bannerSize.toAdSize()
        if (BuildConfig.DEBUG) {
            adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        } else {
            adView.adUnitId = "ca-app-pub-5893289139190514/2695107336"
        }
        inputAdd.y -= bannerSize.y.px
        MobileAds.initialize(this) {}
        adView.loadAd(AdRequest.Builder().build())
    }

    /**
     * Расчёт размера рекламного баннера
     *
     * @return размер рекламного баннера
     */
    private fun calculateAdBannerSize(): Point {
        val size = Point()
        size.x = FULL_WIDTH
        val windowHeight = windowSize.y.dp
        size.y = when {
            windowHeight <= 400 -> 20
            windowHeight <= 720 -> 32
            windowHeight > 720 -> 50
            else -> 0
        }
        return size
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
