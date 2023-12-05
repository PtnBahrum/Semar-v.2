package com.example.semarv2.data.source.remote.model

data class User(
    var id : String = "",
    var email : String = "",
    var name : String = "",
    var image: String = "",
    var score_quiz_1 : Int = 0,
    var score_quiz_2: Int = 0,
    var score_quiz_3: Int = 0,
    var score_quiz_4: Int = 0,
)
