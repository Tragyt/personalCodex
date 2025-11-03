package programmazionemobile.esercizi.personalcodex.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;

@Dao
public interface CampaignsSectionsDAO {
    @Insert
    long insert(FD02_CAMPAIGNS_SECTIONS section);

    @Query("DELETE FROM fd02_campaigns_sections WHERE ID = :idSection")
    void delete(long idSection);

    @Update
    void update(FD02_CAMPAIGNS_SECTIONS section);

    @Query("SELECT * FROM FD02_CAMPAIGNS_SECTIONS WHERE FD02_CAMPAIGN_FD01 = :idCampaign ORDER BY ID")
    List<FD02_CAMPAIGNS_SECTIONS> getAll(long idCampaign);

    @Query("SELECT * FROM FD02_CAMPAIGNS_SECTIONS WHERE ID = :id LIMIT 1")
    FD02_CAMPAIGNS_SECTIONS get(long id);
}
