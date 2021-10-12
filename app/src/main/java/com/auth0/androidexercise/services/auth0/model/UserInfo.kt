package com.auth0.androidexercise.services.auth0.model

import com.auth0.jwt.JWT
import retrofit2.http.Field

data class UserInfo(
    @Field("sub") val userId: String,
    @Field("username") val username: String?,
    @Field("nickname") val nickname: String?,
    @Field("name") val name: String?,
    @Field("given_name") val givenName: String?,
    @Field("family_name") val familyName: String?,
    @Field("email") val email: String?,
    @Field("email_verified") val emailVerified: Boolean = false,
    @Field("picture") val picture: String?,
    @Field("updated_at") val updatedAt: String?
) {

    fun formattedUserName(): String {
        return username ?: name ?: email ?: "(user not available)"
    }

    companion object {
        fun from(idToken: String): UserInfo? {
            try {
                val decodedJWT = JWT.decode(idToken)

                val sub = decodedJWT.subject
                val username =
                    if (!decodedJWT.getClaim("username").isNull) decodedJWT.getClaim("username")
                        .asString() else null
                val nickname =
                    if (!decodedJWT.getClaim("nickname").isNull) decodedJWT.getClaim("nickname")
                        .asString() else null
                val name = if (!decodedJWT.getClaim("name").isNull) decodedJWT.getClaim("name")
                    .asString() else null
                val givenName =
                    if (!decodedJWT.getClaim("given_name").isNull) decodedJWT.getClaim("given_name")
                        .asString() else null
                val familyName =
                    if (!decodedJWT.getClaim("family_name").isNull) decodedJWT.getClaim("family_name")
                        .asString() else null
                val email = if (!decodedJWT.getClaim("email").isNull) decodedJWT.getClaim("email")
                    .asString() else null
                val emailVerified =
                    if (!decodedJWT.getClaim("email_verified").isNull) decodedJWT.getClaim("email_verified")
                        .asBoolean() else false
                val picture =
                    if (!decodedJWT.getClaim("picture").isNull) decodedJWT.getClaim("picture")
                        .asString() else null
                val updatedAt =
                    if (!decodedJWT.getClaim("updated_at").isNull) decodedJWT.getClaim("updated_at")
                        .asString() else null

                return UserInfo(
                    sub,
                    username,
                    nickname,
                    name,
                    givenName,
                    familyName,
                    email,
                    emailVerified,
                    picture,
                    updatedAt
                )
            } catch (ignored: Exception) {
                ignored.printStackTrace()
            }
            return null
        }
    }
}

