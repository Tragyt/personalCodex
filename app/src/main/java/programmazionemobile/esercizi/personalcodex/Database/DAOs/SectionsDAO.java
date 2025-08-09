package programmazionemobile.esercizi.personalcodex.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import programmazionemobile.esercizi.personalcodex.Database.Entities.TP02_SECTIONS;

@Dao
public interface SectionsDAO {
    @Insert
    long insert(TP02_SECTIONS section);

    @Query("DELETE FROM TP02_SECTIONS WHERE id = :idSection")
    void delete(long idSection);

    @Update
    void update(TP02_SECTIONS section);

    @Query("SELECT * FROM TP02_SECTIONS WHERE TP02_TEMPLATE_TP01 = :idTemplate ORDER BY ID")
    List<TP02_SECTIONS> getAll(long idTemplate);
}
