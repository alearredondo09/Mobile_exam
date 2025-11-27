package com.app.examenmoviles.data.remote.dto

import com.google.gson.annotations.SerializedName

data class HoroscopeDto(
    @SerializedName("date") val date: String,
    @SerializedName("zodiac") val zodiac: String,
    @SerializedName("horoscope") val horoscope: String,
)
