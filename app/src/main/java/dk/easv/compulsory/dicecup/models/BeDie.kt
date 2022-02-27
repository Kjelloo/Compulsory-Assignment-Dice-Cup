package dk.easv.compulsory.dicecup.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


data class BeDie(val pips: Int, val img: Int): Parcelable, Serializable {

    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readInt())

    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeInt(pips)
        parcel.writeInt(img)
    }

    companion object CREATOR : Parcelable.Creator<BeDie> {
        override fun createFromParcel(parcel: Parcel): BeDie {
            return BeDie(parcel)
        }

        override fun newArray(size: Int): Array<BeDie?> {
            return arrayOfNulls(size)
        }
    }
}
