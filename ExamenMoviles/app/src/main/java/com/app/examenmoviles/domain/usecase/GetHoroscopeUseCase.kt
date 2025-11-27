package com.app.examenmoviles.domain.usecase

import com.app.examenmoviles.domain.common.Result
import com.app.examenmoviles.domain.model.Horoscope
import com.app.examenmoviles.domain.repository.HoroscopeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetHoroscopeUseCase
    @Inject
    constructor(
        private val repository: HoroscopeRepository,
    ) {
        operator fun invoke(zodiac: String): Flow<Result<Horoscope>> =
            flow {
                try {
                    emit(Result.Loading)
                    val horoscope = repository.getHoroscope(zodiac)
                    emit(Result.Success(horoscope))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
