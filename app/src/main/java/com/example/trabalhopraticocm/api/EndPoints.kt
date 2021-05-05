package com.example.trabalhopraticocm.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {
    @GET("/myslim/api/users")
    fun getUsers(): Call<List<User>>

    @GET ("/myslim/api/users/{id}")
    fun getUserById(@Path("id") id:Int): Call<User>

    @GET ("/myslim/api/reports")
    fun getReports(): Call<List<Report>>

    @GET ("/myslim/api/reports/{id}")
    fun getReportById(@Path("id") id: String?): Call<Report>

    @FormUrlEncoded
    @POST("/myslim/api/user_login")
    fun login(@Field("username") username: String?, @Field("password") password: String?): Call<OutputLogin>
}