package com.example.myapplication

data class UserTreeInfo(
    var uid: String? = null,
    var name: String? = null,
    var surname: String? = null,
    var patronymic: String? = null,
    var birthLocation: String? = null,
    var actualLocation: String? = null,
    var birthDate: String? = null,
    var sex: String? = null,
    var photoUri: String? = null,
    var treeId: String? = null,
    var numOfPhoto: Int = 0,
)