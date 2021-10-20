package com.auth0.androidexercise.security

import android.content.Context
import com.auth0.androidexercise.R
import com.auth0.androidexercise.services.auth0.Auth0Services
import com.auth0.androidexercise.services.auth0.model.JWKey
import com.auth0.androidexercise.services.impl.RetrofitClient
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.RSAKeyProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.ByteString.Companion.decodeBase64
import java.math.BigInteger
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPublicKeySpec
import javax.inject.Inject
import javax.inject.Named


class SecurityRepository @Inject constructor(
    private val appContext: Context,
    private val retrofit: RetrofitClient,
    @Named("credentialsRepository") private val credentialsRepository: CredentialsRepository
) {

    private val keysCache = mutableMapOf<String, JWKey>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            fetchKeys()
        }
    }

    @Throws(Exception::class)
    fun verify(idToken: String) {
        val jwt = JWT.decode(idToken)
        val key = getKey(jwt.keyId)

        val algorithm = getAlgorithm(key)

        JWT.require(algorithm)
            .acceptLeeway(5)
            .build()
            .verify(idToken)
    }

    fun logout(accessToken: String): Boolean {
        val baseUrl =
            "${appContext.getString(R.string.AUTH0_SCHEME)}://${appContext.getString(R.string.AUTH0_DOMAIN)}"
        val logout = retrofit
            .getClient(baseUrl)
            .create(Auth0Services::class.java)
            .revoke(appContext.getString(R.string.AUTH0_CLIENT_ID), "access_token", accessToken)
            ?.execute()

        credentialsRepository.clearCredentials()
        return true
    }

    private fun fetchKeys() {
        val baseUrl =
            "${appContext.getString(R.string.AUTH0_SCHEME)}://${appContext.getString(R.string.AUTH0_DOMAIN)}"
        val keys = retrofit
            .getClient(baseUrl)
            .create(Auth0Services::class.java)
            .getJWKS()
            ?.execute()


        keys?.body()?.keys?.forEach {
            keysCache[it.kid] = it
        }
    }

    private fun getKey(keyId: String?): JWKey {
        if (keyId == null) throw IllegalArgumentException("keyId could not be null")

        return if (!keysCache.containsKey(keyId)) {
            fetchKeys()
            val key = keysCache[keyId] ?: throw  SecurityException("Key with id: $keyId not found.")
            key
        } else keysCache[keyId]!!
    }

    /**
     * If future algorithms should be supported to verify JWT signature, add them here (i.e.: RS512)
     */
    private fun getAlgorithm(key: JWKey): Algorithm {
        return when (key.alg) {
            "RS256" -> Algorithm.RSA256(rsaProvider)
            else -> throw SecurityException("algorithm ${key.alg} not implemented")
        }
    }

    private val rsaProvider = object : RSAKeyProvider {
        override fun getPublicKeyById(keyId: String?): RSAPublicKey {
            keyId?.let { keyId ->
                keysCache[keyId]?.let { key ->
                    val modulus = BigInteger(1, key.n.decodeBase64()!!.toByteArray())
                    val exponent = BigInteger(1, key.e.decodeBase64()!!.toByteArray())
                    val publicKey = KeyFactory.getInstance("RSA")
                        .generatePublic(RSAPublicKeySpec(modulus, exponent))
                    return publicKey as RSAPublicKey
                }
            }
            throw IllegalArgumentException("KeyId is null")
        }

        override fun getPrivateKey(): RSAPrivateKey {
            throw IllegalAccessException("method not implemented")
        }

        override fun getPrivateKeyId(): String {
            throw IllegalAccessException("method not implemented")
        }

    }

}