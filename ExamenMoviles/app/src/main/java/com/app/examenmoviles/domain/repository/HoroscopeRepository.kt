package com.app.examenmoviles.domain.repository

import com.app.examenmoviles.domain.model.Horoscope

interface HoroscopeRepository {
    suspend fun getHoroscope(zodiac: String): Horoscope
}
