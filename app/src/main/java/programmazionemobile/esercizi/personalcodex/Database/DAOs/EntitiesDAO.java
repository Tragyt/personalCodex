package programmazionemobile.esercizi.personalcodex.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;


@Dao
public interface EntitiesDAO {
    @Insert
    Long insert(FD03_ENTITIES entity);

    @Query("DELETE FROM fd03_entities WHERE id = :idEntity")
    void delete(long idEntity);

    @Update
    void update(FD03_ENTITIES entity);

    @Query("SELECT ENTITY.* " +
            "FROM FD03_ENTITIES AS ENTITY " +
            "INNER JOIN FD02_CAMPAIGNS_SECTIONS AS SECTION ON SECTION.ID = FD03_SECTION_FD02 " +
            "WHERE SECTION.ID = :idSection AND FD03_NAME LIKE '%' || :name || '%'" +
            "ORDER BY ENTITY.ID")
    List<FD03_ENTITIES> getAll(long idSection, String name);

    @Query("SELECT * FROM FD03_ENTITIES WHERE ID = :idEntity LIMIT 1")
    FD03_ENTITIES get(long idEntity);

    @Query("SELECT DISTINCT FD03_ENTITIES.* " +
            "FROM FD03_ENTITIES INNER JOIN FD02_CAMPAIGNS_SECTIONS ON FD03_SECTION_FD02 = FD02_CAMPAIGNS_SECTIONS.ID " +
            "WHERE FD02_CAMPAIGN_FD01 = :idCampaign AND Lower(Trim(FD03_NAME)) LIKE '%' || :name || '%'")
    List<FD03_ENTITIES> getAllFromCampaign(long  idCampaign, String name);
}
