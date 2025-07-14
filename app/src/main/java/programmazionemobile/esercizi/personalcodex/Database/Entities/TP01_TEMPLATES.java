package programmazionemobile.esercizi.personalcodex.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class TP01_TEMPLATES implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public long ID;

    @ColumnInfo(name = "TP01_NAME")
    public String TP01_NAME;

    public TP01_TEMPLATES(String TP01_NAME) {
        this.TP01_NAME = TP01_NAME;
    }
    public TP01_TEMPLATES(String TP01_NAME, int id) {
        this.TP01_NAME = TP01_NAME;
        this.ID = id;
    }
}
