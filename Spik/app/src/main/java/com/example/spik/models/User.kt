package com.example.spik.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//Class d'objet User pour les utilisateurs
@Parcelize
class User(val uid: String?, val username: String?, val lang: Int?, val online: Boolean): Parcelable {
    constructor() : this("","", 0, false)
}