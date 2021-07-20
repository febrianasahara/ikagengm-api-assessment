package com.ikymasie.ny_times_api_assessment

import android.app.Application
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump


class GlobalApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.AppTheme)
        initCustomFonts()
    }
    /**
     * Fors richer UII, custom font have been applied to
     * */
    private fun initCustomFonts() {
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/RobotoLight.ttf")
                            .setFontAttrId(android.R.attr.font)
                            .build()
                    )
                )
                .build()
        )

    }
}