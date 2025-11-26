package com.app.examenmoviles.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ExampleApi {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
    ): PokemonListDto
}
