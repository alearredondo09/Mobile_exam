package com.app.examenmoviles.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.app.examenmoviles.domain.model.Horoscope
import com.app.examenmoviles.domain.model.HoroscopeCache
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HoroscopePreferences
    @Inject
    constructor(
        @ApplicationContext context: Context,
        private val gson: Gson,
    ) {
        /**private val prefs: SharedPreferences =
         context.getSharedPreferences(
         PreferencesConstants.PREF_NAME,
         Context.MODE_PRIVATE,
         )

         // Guarda un horóscopo en JSON
         fun saveHoroscope(horoscope: Horoscope) {
         prefs
         .edit()
         .putString(
         PreferencesConstants.KEY_HOROSCOPE_JSON,
         gson.toJson(horoscope),
         ).putLong(
         PreferencesConstants.KEY_LAST_UPDATE,
         System.currentTimeMillis(),
         ).apply()
         }

         // Lee el horóscopo guardado
         fun getHoroscopeCache(): HoroscopeCache? {
         val json = prefs.getString(PreferencesConstants.KEY_HOROSCOPE_JSON, null)
         val lastUpdate = prefs.getLong(PreferencesConstants.KEY_LAST_UPDATE, 0)

         if (json == null) return null

         val horoscope = gson.fromJson(json, Horoscope::class.java)

         return HoroscopeCache(
         horoscope = horoscope,
         lastUpdate = lastUpdate,
         )
         }

         // Revisa si sigue vigente el cache
         fun isCacheValid(): Boolean {
         val lastUpdate = prefs.getLong(PreferencesConstants.KEY_LAST_UPDATE, 0)
         val now = System.currentTimeMillis()
         return (now - lastUpdate) < PreferencesConstants.CACHE_DURATION
         }**/
    }
