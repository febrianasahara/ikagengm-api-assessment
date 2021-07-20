package com.ikymasie.ny_times_api_assessment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ikymasie.ny_times_api_assessment.views.HomeActivity

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var intent = Intent(this,HomeActivity::class.java)
        startActivity(intent)
    }
}