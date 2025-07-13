package programmazionemobile.esercizi.personalcodex.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import programmazionemobile.esercizi.personalcodex.Database.Entities.FD04_BONDS;

@Dao
public interface BondsDAO {
    @Insert
    void insert(FD04_BONDS bond);

    @Delete
    void delete(FD04_BONDS bond);

    @Update
    void update(FD04_BONDS bond);
}
