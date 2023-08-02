package com.example.project001.models

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("states") var states: ArrayList<States> = arrayListOf()
)

data class Districts(

    @SerializedName("name") var name: String? = null,
    @SerializedName("constituency") var constituency: ArrayList<String> = arrayListOf()

)

data class States(

    @SerializedName("state_name") var stateName: String? = null,
    @SerializedName("districts") var districts: ArrayList<Districts> = arrayListOf()

)
