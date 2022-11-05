package com.ihm.dronegpsmobileapp

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import android.os.Parcel
import android.os.Parcelable.Creator

/**
 * Class automatically created from the following json sample
 * with the help of http://www.jsonschema2pojo.org/
 *
 * {
 * "page":1,
 * "total_results":17687,
 * "total_pages":885,
 * "results":[
 * {
 * "popularity":23.936,
 * "id":17832,
 * "profile_path":"\/72fz51l5P8HTnp1eHZCEwfBDQVa.jpg",
 * "name":"Carla Gugino",
 * "adult":false
 * }
 * ]
 * }
 */
class PersonPopularResponse() : Parcelable {
    @SerializedName("page")
    @Expose
    var page = 0

    @SerializedName("total_results")
    @Expose
    var totalResults = 0

    @SerializedName("total_pages")
    @Expose
    var totalPages = 0

    @SerializedName("results")
    @Expose
    var results: List<PersonData?>? = null

    constructor(parcel: Parcel) : this() {
        page = parcel.readInt()
        totalResults = parcel.readInt()
        totalPages = parcel.readInt()
        results = parcel.createTypedArrayList(PersonData)
    }


    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(page)
        dest.writeValue(totalResults)
        dest.writeValue(totalPages)
        dest.writeList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<PersonPopularResponse> {
        override fun createFromParcel(parcel: Parcel): PersonPopularResponse {
            return PersonPopularResponse(parcel)
        }

        override fun newArray(size: Int): Array<PersonPopularResponse?> {
            return Array<PersonPopularResponse?>(size){null}
        }
    }


}