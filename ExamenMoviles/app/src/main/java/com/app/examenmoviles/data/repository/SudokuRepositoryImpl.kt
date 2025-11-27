package com.app.examenmoviles.data.repository

import com.app.examenmoviles.data.mapper.toDomain
import com.app.examenmoviles.data.remote.api.SudokuApi
import com.app.examenmoviles.domain.model.Sudoku
import com.app.examenmoviles.domain.repository.SudokuRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SudokuRepositoryImpl
    @Inject
    constructor(
        private val api: SudokuApi,
    ) : SudokuRepository {
        override suspend fun getSudoku(
            width: Int,
            height: Int,
            difficulty: String,
        ): Sudoku = api.getSudoku(width, height, difficulty).toDomain()
    }
