package com.example.currencyapp.data.di

import okhttp3.Interceptor
import okhttp3.Response

class APIKeyInterceptor private constructor(chain: Interceptor.Chain) {
    val response: Response = getApiKeyHeaderInterceptorResponse(chain)


    companion object {
        private var interceptorInstance: APIKeyInterceptor? = null
        fun getInstance(chain: Interceptor.Chain): APIKeyInterceptor =
            interceptorInstance ?: APIKeyInterceptor(chain)
    }


    private fun getApiKeyHeaderInterceptorResponse(chain: Interceptor.Chain): Response {
        val request = chain
            .request()
            .newBuilder()
            .addHeader("apikey", "jwTY3ePdZwCZrZ1kP96pLfnUe9qUpOq9")
            .build()

        return chain.proceed(request)
    }
}