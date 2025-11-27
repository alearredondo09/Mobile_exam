package com.app.examenmoviles.di

import android.content.Context
import com.app.examenmoviles.data.local.preferences.HoroscopePreferences
import com.app.examenmoviles.data.remote.api.ApiKeyInterceptor
import com.app.examenmoviles.data.remote.api.SudokuApi
import com.app.examenmoviles.data.repository.HoroscopeRepositoryImpl
import com.app.examenmoviles.data.repository.SudokuRepositoryImpl
import com.app.examenmoviles.domain.model.Sudoku
import com.app.examenmoviles.domain.repository.HoroscopeRepository
import com.app.examenmoviles.domain.repository.SudokuRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// di/AppModule.kt
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideOKHttpClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(
                ApiKeyInterceptor(
                    "ywXNy+QO8Jyor9W7xtSJfQ==L0iSSTGO1VF2uiZx",
                ),
            ).build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl("https://api.api-ninjas.com/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideSudokuApi(retrofit: Retrofit): SudokuApi = retrofit.create(SudokuApi::class.java)

    @Provides
    @Singleton
    fun provideSudokuRepository(api: SudokuApi): SudokuRepository = SudokuRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideHoroscopePreferences(
        @ApplicationContext context: Context,
        gson: Gson,
    ): HoroscopePreferences = HoroscopePreferences(context, gson)
}
