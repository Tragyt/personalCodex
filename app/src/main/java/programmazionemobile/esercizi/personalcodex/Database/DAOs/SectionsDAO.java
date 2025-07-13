package programmazionemobile.esercizi.personalcodex.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import programmazionemobile.esercizi.personalcodex.Database.Entities.TP02_SECTIONS;

@Dao
public interface SectionsDAO {
    @Insert
    void insert(TP02_SECTIONS section);

    @Delete
    void delete(TP02_SECTIONS section);

    @Update
    void update(TP02_SECTIONS section);

    @Query("SELECT * FROM TP02_SECTIONS WHERE TP02_TEMPLATE_TP01 = :idTemplate")
    List<TP02_SECTIONS> getAll(int idTemplate);
}
