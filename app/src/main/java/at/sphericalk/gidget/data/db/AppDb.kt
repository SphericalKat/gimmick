package at.sphericalk.gidget.data.db

import androidx.room.Database
import at.sphericalk.gidget.model.Event

@Database(entities = [Event::class], version = 1)
abstract class AppDb {
    abstract fun eventDao(): EventDao
}