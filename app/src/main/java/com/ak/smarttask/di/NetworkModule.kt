package com.ak.smarttask.di

import com.ak.smarttask.network.TaskApiService
import com.ak.smarttask.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.security.cert.CertificateException
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  /**
   * Created a Custom Trust Manger to implement the SSL Pinning but currently it is not completed
   * will update it in future
   */
  fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
    try {
      // Create a trust manager that does not validate certificate chains
      val trustAllCerts =
          arrayOf<TrustManager>(
              object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {}

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {}

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> =
                    arrayOf()
              })

      // Install the all-trusting trust manager
      val sslContext = SSLContext.getInstance("SSL")
      sslContext.init(null, trustAllCerts, java.security.SecureRandom())

      // Create an ssl socket factory with our all-trusting manager
      val sslSocketFactory = sslContext.socketFactory

      val builder = OkHttpClient.Builder()
      builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
      builder.hostnameVerifier { _, _ -> true } // Bypass hostname verification
      return builder
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
  }

  private val okHttpClient =
      OkHttpClient.Builder()
          .connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT))
          .build()

  @Provides
  @Singleton
  fun provideRetrofit(): Retrofit {
    val okHttpClient = getUnsafeOkHttpClient().build()
    return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
  }

  @Provides
  @Singleton
  fun provideTaskApiService(retrofit: Retrofit): TaskApiService =
      retrofit.create(TaskApiService::class.java)
}
