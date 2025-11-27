package com.app.examenmoviles.domain.usecase

import android.icu.number.IntegerWidth
import com.app.examenmoviles.domain.common.Result
import com.app.examenmoviles.domain.model.Horoscope
import com.app.examenmoviles.domain.model.Sudoku
import com.app.examenmoviles.domain.repository.HoroscopeRepository
import com.app.examenmoviles.domain.repository.SudokuRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSudokuUseCase
    @Inject
    constructor(
        private val repository: SudokuRepository,
    ) {
        operator fun invoke(
            width: Int,
            height: Int,
            difficulty: String,
        ): Flow<com.app.examenmoviles.domain.common.Result<Sudoku>> =
            flow {
                try {
                    emit(com.app.examenmoviles.domain.common.Result.Loading)
                    val sudoku = repository.getSudoku(width, height, difficulty)
                    emit(
                        com.app.examenmoviles.domain.common.Result
                            .Success(sudoku),
                    )
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
