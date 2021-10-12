package com.auth0.androidexercise.services.auth0

import com.auth0.androidexercise.security.auth.oauth.OAuthTokenResponse
import com.auth0.androidexercise.services.auth0.model.JWKeys
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Auth0Services {

    @GET(".well-known/jwks.json")
    fun getJWKS(): Call<JWKeys?>?

    @FormUrlEncoded
    @POST("oauth/token")
    fun getAccessToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("code_verifier") codeVerifier: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String
    ): Call<OAuthTokenResponse?>?

    @FormUrlEncoded
    @POST("oauth/token")
    fun renewAccessToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("refresh_token") refreshToken: String
    ): Call<OAuthTokenResponse?>?

    @FormUrlEncoded
    @POST("oauth/revoke")
    fun revoke(
        @Field("client_id") clientId: String,
        @Field("token_type_hint") tokenType: String,
        @Field("token") token: String
    ): Call<Void>?

}