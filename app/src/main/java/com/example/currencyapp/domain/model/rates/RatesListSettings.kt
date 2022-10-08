package com.example.currencyapp.domain.model.rates

import android.os.Parcel
import android.os.Parcelable

data class RatesListSettings(
    val currencyCode: String = Currencies.UAH.name,
    val precision: Int = 3
) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "Oopsie",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(currencyCode)
        parcel.writeInt(precision)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RatesListSettings> {
        override fun createFromParcel(parcel: Parcel): RatesListSettings {
            return RatesListSettings(parcel)
        }

        override fun newArray(size: Int): Array<RatesListSettings?> {
            return arrayOfNulls(size)
        }
    }
}
