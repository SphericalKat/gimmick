package at.sphericalk.gidget.di

import android.content.Context
import androidx.room.Room
import at.sphericalk.gidget.data.db.AppDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Provides
    @Singleton
    fun providesAppDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDb::class.java, "gimmick-db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providesEventDao(db: AppDb) = db.eventDao()
}