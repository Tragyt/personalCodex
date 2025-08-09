package programmazionemobile.esercizi.personalcodex.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import programmazionemobile.esercizi.personalcodex.Database.Entities.TP01_TEMPLATES;

@Dao
public interface TemplatesDAO {
    @Insert
    long insert(TP01_TEMPLATES template);

    @Query("DELETE FROM tp01_templates WHERE id = :idTemplate")
    void delete(long idTemplate);

    @Update
    void update(TP01_TEMPLATES template);

    @Query("SELECT * FROM TP01_TEMPLATES ORDER BY ID")
    List<TP01_TEMPLATES> getAll();
}
