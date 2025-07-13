package programmazionemobile.esercizi.personalcodex.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;

@Dao
public interface CampaignsSectionsDAO {
    @Insert
    void insert(FD02_CAMPAIGNS_SECTIONS section);

    @Delete
    void delete(FD02_CAMPAIGNS_SECTIONS section);

    @Update
    void update(FD02_CAMPAIGNS_SECTIONS section);

    @Query("SELECT * FROM FD02_CAMPAIGNS_SECTIONS WHERE FD02_CAMPAIGN_FD01 = :idCampaign")
    List<FD02_CAMPAIGNS_SECTIONS> getAll(int idCampaign);
}
