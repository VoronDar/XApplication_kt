package com.astery.xapplication.model.remote

import com.astery.xapplication.model.entities.Item
import com.astery.xapplication.repository.RemoteEntity

class ItemRemoteEntity: Item(), RemoteEntity<Item> {
    override var id: Int = 0
    override var lastUpdated: Int = 0
    override fun convertFromRemote(): Item = this
}