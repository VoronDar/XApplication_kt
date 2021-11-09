package com.astery.xapplication.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.astery.xapplication.repository.remoteDataStorage.FbUsable

/** simple item
 * just text like "don't forget to ues lubricant, count of people who agree or disagree with this advice"
 */
@Entity
data class Advise(@PrimaryKey var id: String, @ColumnInfo(name = "agree_count") var agreeCount:Int,
                  @ColumnInfo(name = "disagree_count")
                  var disagreeCount:Int,
                  var type:Int,
                  var text: String,
                  @ColumnInfo(name = "parent_id")
                  var parentId: String
) : FbUsable {

    @Ignore
    constructor() : this("", 0, 0, 0, "", "")

    override fun setVariableId(id: String) { this.id = id}

    override fun toString(): String {
        return "Advise{" +
                "id='" + id + '\'' +
                ", agreeCount=" + agreeCount +
                ", disagreeCount=" + disagreeCount +
                ", type=" + type +
                ", text='" + text + '\'' +
                ", parentId='" + parentId + '\'' +
                '}'
    }
}