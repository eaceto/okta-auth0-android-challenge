package com.auth0.androidexercise.services.auth0.model

data class JWKey(
    val alg: String,
    val kty: String,
    val use: String,
    val n: String,
    val e: String,
    val kid: String,
    val x5t: String,
    val x5c: List<String>
)

