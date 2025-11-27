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

            // 1️⃣ Intentar cache primero si está vigente
            prefs.getHoroscopeCache()?.let { cache ->
                if (prefs.isCacheValid()) {
                    return cache.horoscope // cache fresco
                }
            }

            return try {
                // 2️⃣ Si no hay cache válido → llamar API
                val dto = api.getHoroscope(clean)
                val horoscope = dto.toDomain(clean)

                // Guardar en cache
                prefs.saveHoroscope(horoscope)

                horoscope
            } catch (e: Exception) {
                // 3️⃣ Si falla la red → usar cache viejo si existe
                prefs.getHoroscopeCache()?.let { cache ->
                    return cache.horoscope
                } ?: throw e
            }
        }
    }
