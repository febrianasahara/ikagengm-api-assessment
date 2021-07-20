package com.ikymasie.ny_times_api_assessment

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper

open class BaseActivity : AppCompatActivity() {
    /**
     * attachBaseContext applies the view pump library
     * This allows for Custom fonts to be easily applied across the App
     * */
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}