package programmazionemobile.esercizi.personalcodex.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;

@Dao
public interface CampaignsDAO {
    @Insert
    long insert(FD01_CAMPAIGNS campaign);

    @Query("DELETE FROM fd01_campaigns WHERE ID = :idCampaign")
    void delete(long idCampaign);

    @Update
    void update(FD01_CAMPAIGNS campaign);

    @Query("SELECT * FROM FD01_CAMPAIGNS ORDER BY ID")
    List<FD01_CAMPAIGNS> getAll();

    @Query("SELECT * FROM FD01_CAMPAIGNS WHERE ID = :id LIMIT 1")
    FD01_CAMPAIGNS get(long id);
}
