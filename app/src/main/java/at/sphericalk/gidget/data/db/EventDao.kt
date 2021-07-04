package at.sphericalk.gidget.data.db

import androidx.room.Dao
import androidx.room.Query
import at.sphericalk.gidget.model.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM event")
    fun getAllEvents(): Flow<List<Event>>
}