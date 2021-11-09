package com.astery.xapplication.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.astery.xapplication.model.appValues.ArrayConverter
import com.astery.xapplication.model.entities.values.Appeared
import java.sql.Date

/*
@Entity
@TypeConverters(ArrayConverter::class)
data class Warning(@PrimaryKey var id: String,
    @ColumnInfo(name = "template_id")  var templateId: String?,
    var date: Date? = null,
    var appeared: Appeared = Appeared.PREPARED)

 */