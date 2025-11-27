package com.app.examenmoviles.data.repository

import com.app.examenmoviles.data.local.preferences.HoroscopePreferences
import com.app.examenmoviles.data.mapper.toDomain
import com.app.examenmoviles.data.remote.api.SudokuApi
import com.app.examenmoviles.domain.model.Horoscope
import com.app.examenmoviles.domain.repository.HoroscopeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HoroscopeRepositoryImpl
    @Inject
    constructor(
        private val api: SudokuApi,
        private val prefs: HoroscopePreferences,
    ) : HoroscopeRepository {
        override suspend fun getHoroscope(zodiac: String): Horoscope {
            val clean = zodiac.trim().lowercase()

            val dto = api.getHoroscope(clean)

            return dto.toDomain(clean)
        }
    }
