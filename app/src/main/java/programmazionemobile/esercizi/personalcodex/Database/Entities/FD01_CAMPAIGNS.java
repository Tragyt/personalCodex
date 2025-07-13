package programmazionemobile.esercizi.personalcodex.Database.Entities;

import androidx.room.*;

@Entity
public class FD01_CAMPAIGNS {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public int ID;

    @ColumnInfo(name = "FD01_NAME")
    public String FD01_NAME;

    @ColumnInfo(name = "FD01_IMAGE")
    public String FD01_IMAGE;

    public FD01_CAMPAIGNS(String FD01_IMAGE, String FD01_NAME) {
        this.FD01_IMAGE = FD01_IMAGE;
        this.FD01_NAME = FD01_NAME;
    }
}
