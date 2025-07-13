package programmazionemobile.esercizi.personalcodex.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import programmazionemobile.esercizi.personalcodex.Database.Entities.TP01_TEMPLATES;

@Dao
public interface TemplatesDAO {
    @Insert
    void insert(TP01_TEMPLATES template);

    @Delete
    void delete(TP01_TEMPLATES template);

    @Update
    void update(TP01_TEMPLATES template);

    @Query("SELECT * FROM TP01_TEMPLATES")
    List<TP01_TEMPLATES> getAll();
}
