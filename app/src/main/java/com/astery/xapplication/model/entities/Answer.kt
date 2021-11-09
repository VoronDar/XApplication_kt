package com.astery.xapplication.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.astery.xapplication.repository.remoteDataStorage.FbUsable

/**
 * sets of answers to questions. If the user choose this answer and if it has an item - it will be shown to the user.
 */
@Entity
data class Answer(@PrimaryKey var id: String, var text: String,
    var item: String, @ColumnInfo(name = "parent_id") var parentId: String) : FbUsable {

    @Ignore var itemOb: Item? = null


    @Ignore
    constructor():this("", "", "", "") {
    }

    override fun setVariableId(id: String) {
        this.id = id
    }


    override fun toString(): String {
        return "Answer{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", item='" + item + '\'' +
                ", itemOb=" + itemOb +
                ", parentId='" + parentId + '\'' +
                '}'
    }

}