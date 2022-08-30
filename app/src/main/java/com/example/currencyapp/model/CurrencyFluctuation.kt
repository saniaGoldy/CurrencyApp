package com.example.currencyapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class CurrencyFluctuation(
    @PrimaryKey
    val iso4217Alpha: String,
    @ColumnInfo(name = "rate") val rate: Double,
    @ColumnInfo(name = "rateDifference") val rateDifference: Double,
) : Parcelable {
    @Ignore
    val fullName: String = Currencies.valueOf(iso4217Alpha).fullName

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(iso4217Alpha)
        parcel.writeDouble(rate)
        parcel.writeDouble(rateDifference)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CurrencyFluctuation> {
        override fun createFromParcel(parcel: Parcel): CurrencyFluctuation {
            return CurrencyFluctuation(parcel)
        }

        override fun newArray(size: Int): Array<CurrencyFluctuation?> {
            return arrayOfNulls(size)
        }
    }
}