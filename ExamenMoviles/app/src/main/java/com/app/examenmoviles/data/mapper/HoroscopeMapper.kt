package com.app.examenmoviles.data.mapper

import com.app.examenmoviles.data.remote.dto.HoroscopeDto
import com.app.examenmoviles.domain.model.Horoscope

fun HoroscopeDto.toDomain(requestedSign: String): Horoscope =
    Horoscope(
        date = date ?: "",
        zodiac = requestedSign,
        horoscope = horoscope ?: "",
    )
