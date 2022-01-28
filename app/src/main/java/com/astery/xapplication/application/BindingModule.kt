package com.astery.xapplication.application

import com.astery.xapplication.repository.localDataStorage.AppLocalStorage
import com.astery.xapplication.repository.localDataStorage.FakeLocalStorage
import com.astery.xapplication.repository.localDataStorage.LocalStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * provide repository and dataStorages
 * */
@Module
@InstallIn(SingletonComponent::class)
abstract class BindingModule{
        @Binds
        @Singleton
        //abstract fun bindLocalStorage(localStorage: FakeLocalStorage): LocalStorage
        abstract fun bindLocalStorage(localStorage: AppLocalStorage):LocalStorage
}