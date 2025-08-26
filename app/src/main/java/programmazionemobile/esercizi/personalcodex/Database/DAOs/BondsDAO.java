package programmazionemobile.esercizi.personalcodex.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import programmazionemobile.esercizi.personalcodex.Database.Entities.FD04_BONDS;

@Dao
public interface BondsDAO {
    @Insert
    void insert(FD04_BONDS bond);

    @Query("DELETE FROM FD04_BONDS WHERE FD04_ENTITY1_FD03 = :idEntity1 and FD04_ENTITY2_FD03 = :idEntity2")
    void delete(long idEntity1, long idEntity2);

    @Update
    void update(FD04_BONDS bond);

    @Query("SELECT * FROM FD04_BONDS WHERE FD04_ENTITY2_FD03 = :idEntity OR FD04_ENTITY1_FD03 = :idEntity")
    List<FD04_BONDS> getAll(long idEntity);
}
