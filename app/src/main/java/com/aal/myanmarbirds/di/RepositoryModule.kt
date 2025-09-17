package com.aal.myanmarbirds.di// di/AppModule.kt
import com.aal.myanmarbirds.data.repository.home.HomeRepository
import com.aal.myanmarbirds.data.repository.home.HomeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideHomeRepository(): HomeRepository {
        return HomeRepositoryImpl()
    }
}
