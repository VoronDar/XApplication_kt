package com.astery.xapplication.model.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/** simple item
 * just text like "don't forget to ues lubricant, count of people who agree or disagree with this advice"
 * use AdviceType for type field
 *
 * Some items have advices
 */
@Parcelize
@Entity
data class Advice(@PrimaryKey var id: Int, var likes:Int,
                  var dislikes:Int,
                  val type:Int,
                  val body: String,
                  val ItemId: Int
):Parcelable{
    @Ignore
    constructor() : this(0, 0, 0, 0, "", 0)
}