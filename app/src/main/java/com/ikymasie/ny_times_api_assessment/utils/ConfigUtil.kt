package com.ikymasie.ny_times_api_assessment.utils

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class ConfigUtil {
    private var remoteConfig = FirebaseRemoteConfig.getInstance()

    var apiKey: String = ""
    var baseUrl: String = ""

    fun init(){
        remoteConfig.fetch()
        baseUrl = remoteConfig.getString("baseUrl")
        apiKey = remoteConfig.getString("apiKey")
    }

    companion object {
        fun getBaseUrl(configUtil: ConfigUtil): String {
            return configUtil.baseUrl
        }
        fun getApiKey(configUtil: ConfigUtil): String {
            return configUtil.apiKey
        }
    }
}
