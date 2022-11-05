package com.ihm.dronegpsmobileapp

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import android.os.Parcel
import android.os.Parcelable.Creator

class PersonData : Parcelable {
    @SerializedName("popularity")
    @Expose
    var popularity = 0.0

    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("profile_path")
    @Expose
    var profilePath: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("adult")
    @Expose
    var isAdult = false

    protected constructor(`in`: Parcel) {
        popularity = `in`.readValue(Double::class.javaPrimitiveType!!.classLoader) as Double
        id = `in`.readValue(Int::class.javaPrimitiveType!!.classLoader) as Int
        profilePath = `in`.readValue(String::class.java.classLoader) as String?
        name = `in`.readValue(String::class.java.classLoader) as String?
        isAdult = `in`.readValue(Boolean::class.javaPrimitiveType!!.classLoader) as Boolean
    }

    constructor() {}

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(popularity)
        dest.writeValue(id)
        dest.writeValue(profilePath)
        dest.writeValue(name)
        dest.writeValue(isAdult)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<PersonData> {
        override fun createFromParcel(parcel: Parcel): PersonData {
            return PersonData(parcel)
        }

        override fun newArray(size: Int): Array<PersonData?> {
            return Array<PersonData?>(size){null}
        }
    }


}