package com.example.pc2_grupal.data.remote

import com.example.pc2_grupal.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Configuración común de Retrofit para la API de la NASA.
 * Cada opción crea su propia interfaz con [crear], por ejemplo:
 * `RetrofitInstance.crear<ApodApi>()`.
 */
object RetrofitInstance {

    private const val URL_BASE = "https://api.nasa.gov/"

    // Agrega api_key a las peticiones de api.nasa.gov (a otros dominios no se envía la clave)
    private val interceptorApiKey = Interceptor { chain ->
        val original = chain.request()
        if (original.url.host != "api.nasa.gov") return@Interceptor chain.proceed(original)
        val url = original.url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.NASA_API_KEY)
            .build()
        chain.proceed(original.newBuilder().url(url).build())
    }

    private val cliente = OkHttpClient.Builder()
        .addInterceptor(interceptorApiKey)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL_BASE)
        .client(cliente)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    inline fun <reified T> crear(): T = retrofit.create(T::class.java)
}
