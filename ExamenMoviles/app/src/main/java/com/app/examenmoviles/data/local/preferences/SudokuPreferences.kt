package com.app.examenmoviles.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.app.examenmoviles.domain.model.SudokuCache
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SudokuPreferences
    @Inject
    constructor(
        @ApplicationContext context: Context,
        private val gson: Gson,
    ) {
        private val prefs: SharedPreferences =
            context.getSharedPreferences(
                PreferencesConstants.PREF_NAME,
                Context.MODE_PRIVATE,
            )

        fun saveSudoku(cache: SudokuCache) {
            prefs
                .edit()
                .putString(PreferencesConstants.KEY_SUDOKU_JSON, gson.toJson(cache))
                .apply()
        }

        fun getSudokuCache(): SudokuCache? {
            val json = prefs.getString(PreferencesConstants.KEY_SUDOKU_JSON, null)
            return json?.let {
                gson.fromJson(it, SudokuCache::class.java)
            }
        }

        fun clearSudoku() {
            prefs.edit().clear().apply()
        }
    }
