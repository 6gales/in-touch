package tech.intouch.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class User(
    @PrimaryKey val uid: String,
    val name: String,
    val profileImageUrl: String?
) : Parcelable {
    constructor() : this("", "", "")
}