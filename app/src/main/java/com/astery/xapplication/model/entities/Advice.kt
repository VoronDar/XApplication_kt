package com.astery.xapplication.model.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.astery.xapplication.model.entities.converters.AdviceTypeConverter
import com.astery.xapplication.model.entities.converters.FeedbackStateConverter
import com.astery.xapplication.model.entities.values.AdviceType
import kotlinx.parcelize.Parcelize

/** simple item
 * just text like "don't forget to ues lubricant, count of people who agree or disagree with this advice"
 * use AdviceType for type field
 *
 * Some items have advices
 */
@TypeConverters(FeedbackStateConverter::class, AdviceTypeConverter::class)
@Parcelize
@Entity
open class Advice(@PrimaryKey var id: Int, var likes:Int,
                  var dislikes:Int,
                  val type:AdviceType = AdviceType.Alert,
                  val body: String,
                  val ItemId: Int,
                  var feedback:FeedBackState = FeedBackState.None
):Parcelable{
    @Ignore
    constructor() : this(0, 0, 0, AdviceType.Alert, "", 0)
}