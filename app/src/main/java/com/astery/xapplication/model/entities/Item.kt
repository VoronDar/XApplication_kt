package com.astery.xapplication.model.entities

import android.graphics.Bitmap
import androidx.room.*
import com.astery.xapplication.repository.remoteDataStorage.FbUsable

/**
 * one item in article
 * may has text, picture, and sets of advises.
 */
@Entity
data class Item(@PrimaryKey var id: String, var text: String,
                var name: String,
                @ColumnInfo(name = "parent_id") var parentId: String,
                @ColumnInfo(name = "category_id") var categoryId: String
                ) : FbUsable {

    @Ignore
    private var image: Bitmap? = null

    @Ignore
    var advises: List<Advise>? = null

    @Ignore
    constructor():this("", "", "", "", "")

    override fun setVariableId(id: String) {
        this.id = id
    }

    override fun toString(): String {
        return "Item{" +
                "id='" + id + '\'' +
                ", image=" + image +
                ", text='" + text + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", advises=" + advises +
                '}'
    }
}