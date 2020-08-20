package com.kine.test.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class CreateUserResponse {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("job")
    @Expose
    var job: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null

}