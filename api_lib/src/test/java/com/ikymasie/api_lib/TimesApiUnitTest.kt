package com.ikymasie.api_lib

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TimesApiUnitTest {

    @Test
    fun Given_valid_key_When_service_executes_Then_apiServiceIsCreatedSuccessfully() {
        val apiClient = ApiClient("https://some-url.com","some-key")
        assertNotNull(apiClient.apiService)
    }
}