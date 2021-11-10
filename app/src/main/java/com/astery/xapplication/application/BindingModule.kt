package com.astery.xapplication.application

import com.astery.xapplication.repository.FakeRepository
import com.astery.xapplication.repository.Repository
import com.astery.xapplication.repository.localDataStorage.LocalStorage
import com.astery.xapplication.repository.localDataStorage.RoomLocalStorage
import com.astery.xapplication.repository.remoteDataStorage.FbRemoteStorage
import com.astery.xapplication.repository.remoteDataStorage.RemoteStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class BindingModule{
        @Binds
        abstract fun bindLocalStorage(localStorage: RoomLocalStorage): LocalStorage
        @Binds
        abstract fun bindRemoteStorage(localStorage: FbRemoteStorage): RemoteStorage
        @Binds
        abstract fun bindRepository(repository: FakeRepository): Repository
}