package programmazionemobile.esercizi.personalcodex.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;

@Dao
public interface CampaignsDAO {
    @Insert
    long insert(FD01_CAMPAIGNS campaign);

    @Delete
    void delete(FD01_CAMPAIGNS campaign);

    @Update
    void update(FD01_CAMPAIGNS campaign);

    @Query("SELECT * FROM FD01_CAMPAIGNS")
    List<FD01_CAMPAIGNS> getAll();
}
