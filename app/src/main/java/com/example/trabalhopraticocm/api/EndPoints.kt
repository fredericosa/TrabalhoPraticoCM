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
    fun getReportById(@Path("id") id: String?): Call<List<Report>>

    @FormUrlEncoded
    @POST("/myslim/api/user_login")
    fun login(@Field("username") username: String?, @Field("password") password: String?): Call<OutputLogin>

    @FormUrlEncoded
    @POST("/myslim/api/report_post")
    fun report(@Field("latitude") latitude: String?,
                 @Field("longitude") longitude: String?,
                 @Field("titulo") titulo: String?,
                 @Field("descr") descr: String?,
                 @Field("img") img: String?,
                 @Field("user_id") user_id: Int?): Call<OutputReport>

    @Headers("Content-Type: application/json")
    @FormUrlEncoded
    @POST("/myslim/api/report_put/{id}")
    fun edit(@Path("id") id: String?,
               @Field("latitude") latitude: String?,
               @Field("longitude") longitude: String?,
               @Field("titulo") titulo: String?,
               @Field("descr") descr: String?,
               @Field("img") img: String?,
               @Field("user_id") user_id: Int?): Call<OutputEdit>


    @POST("/myslim/api/reports_delete/{id}")
    fun delete(@Path("id") id: String?): Call<OutputDelete>
}