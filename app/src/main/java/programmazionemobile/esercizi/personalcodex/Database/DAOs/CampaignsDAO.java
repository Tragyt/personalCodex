package programmazionemobile.esercizi.personalcodex.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD04_BONDS;

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

    @Query("SELECT * FROM FD02_CAMPAIGNS_SECTIONS WHERE FD02_CAMPAIGN_FD01 =:idCampaign")
    List<FD02_CAMPAIGNS_SECTIONS> getAllSections(long idCampaign);

    @Query("SELECT FD03_ENTITIES.* FROM FD03_ENTITIES " +
            "INNER JOIN FD02_CAMPAIGNS_SECTIONS ON FD03_SECTION_FD02 = FD02_CAMPAIGNS_SECTIONS.ID " +
            "WHERE FD02_CAMPAIGN_FD01 = :idCampaign")
    List<FD03_ENTITIES> getAllEntities(long idCampaign);

    @Query("SELECT FD04_BONDS.* FROM FD04_BONDS " +
            "INNER JOIN FD03_ENTITIES ON FD04_ENTITY1_FD03 = FD03_ENTITIES.ID " +
            "INNER JOIN FD02_CAMPAIGNS_SECTIONS ON FD03_SECTION_FD02 = FD02_CAMPAIGNS_SECTIONS.ID " +
            "WHERE FD02_CAMPAIGN_FD01 = :idCampaign")
    List<FD04_BONDS> getAllBonds(long idCampaign);
}
