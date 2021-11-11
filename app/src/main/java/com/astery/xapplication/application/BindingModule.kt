package com.astery.xapplication.application

import com.astery.xapplication.repository.AppRepository
import com.astery.xapplication.repository.localDataStorage.FakeLocalStorage
import com.astery.xapplication.repository.Repository
import com.astery.xapplication.repository.localDataStorage.LocalStorage
import com.astery.xapplication.repository.remoteDataStorage.AppRemoteStorage
import com.astery.xapplication.repository.remoteDataStorage.RemoteStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class BindingModule{
        @Binds
        abstract fun bindLocalStorage(localStorage: FakeLocalStorage): LocalStorage
        @Binds
        abstract fun bindRemoteStorage(localStorage: AppRemoteStorage): RemoteStorage
        @Binds
        abstract fun bindRepository(repository: AppRepository): Repository
}