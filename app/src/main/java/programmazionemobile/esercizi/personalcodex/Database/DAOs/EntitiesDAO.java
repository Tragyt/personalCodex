package programmazionemobile.esercizi.personalcodex.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;


@Dao
public interface EntitiesDAO {
    @Insert
    Long insert(FD03_ENTITIES entity);

    @Delete
    void delete(FD03_ENTITIES entity);

    @Update
    void update(FD03_ENTITIES entity);

    @Query("SELECT ENTITY.* " +
            "FROM FD03_ENTITIES AS ENTITY " +
            "INNER JOIN FD02_CAMPAIGNS_SECTIONS AS SECTION ON SECTION.ID = FD03_SECTION_FD02 " +
            "WHERE SECTION.ID = :idSection AND FD03_NAME LIKE '%' || :name || '%'" +
            "ORDER BY ENTITY.ID")
    List<FD03_ENTITIES> get(long idSection, String name);
}
