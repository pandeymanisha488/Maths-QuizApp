package com.quiz.application.model

import android.os.Parcel
import com.google.gson.annotations.SerializedName

data class QuestionDataItem(
    @SerializedName("options")
    val options: List<String>,
    @SerializedName("questionSplit")
    val questionSplit: QuestionSplit,
    @SerializedName("questionTxt")
    val questionTxt: String,
    @SerializedName("type")
    val type: String
) {
    constructor(parcel: Parcel) : this(
        parcel.createStringArrayList()!!,
        parcel.readParcelable(QuestionSplit::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString()!!,
    )

}