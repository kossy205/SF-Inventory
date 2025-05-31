package com.kosiso.sfinventory.di

import android.content.Context
import androidx.room.Room
import com.kosiso.sfinventory.data.api.ApiService
import com.kosiso.sfinventory.data.repository.MainRepo
import com.kosiso.sfinventory.data.repository.MainRepoImpl
import com.kosiso.sfinventory.database.RoomDatabase
import com.kosiso.sfinventory.utils.Constants.ROOM_DATABASE_NAME
import com.kosiso.sfinventory.database.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        RoomDatabase::class.java,
        ROOM_DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideProductDao(db: RoomDatabase) = db.productDao()


    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://172.20.10.3:3000") // pls use your own pc ip address
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideMainRepo(
        productDao: ProductDao,
        apiService: ApiService
    ): MainRepo{
        return MainRepoImpl(
            productDao,
            apiService
        )
    }
}