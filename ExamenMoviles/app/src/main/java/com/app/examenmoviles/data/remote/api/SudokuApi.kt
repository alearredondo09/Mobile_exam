package com.app.examenmoviles.data.remote.api

import com.app.examenmoviles.data.remote.dto.HoroscopeDto
import com.app.examenmoviles.data.remote.dto.SudokuDto
import com.app.examenmoviles.domain.model.Horoscope
import retrofit2.http.GET
import retrofit2.http.Query

interface SudokuApi {
    @GET("sudokugenerate")
    suspend fun getSudoku(
        @Query("width") width: Int,
        @Query("height") height: Int,
        @Query("difficulty") difficulty: String,
    ): SudokuDto

    @GET("horoscope")
    suspend fun getHoroscope(
        @Query("zodiac") zodiac: String,
    ): HoroscopeDto
}
