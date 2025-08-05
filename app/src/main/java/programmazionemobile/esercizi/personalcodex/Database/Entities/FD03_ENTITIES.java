package programmazionemobile.esercizi.personalcodex.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(foreignKeys = @ForeignKey(entity = FD02_CAMPAIGNS_SECTIONS.class, parentColumns = "ID",
        childColumns = "FD03_SECTION_FD02", onDelete = ForeignKey.CASCADE),
        indices = @Index("FD03_SECTION_FD02"))
public class FD03_ENTITIES implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public long ID;

    @ColumnInfo(name = "FD03_NAME")
    public String FD03_NAME;

    @ColumnInfo(name = "FD03_FILE")
    public String FD03_FILE;

    @ColumnInfo(name = "FD03_IMAGE")
    public String FD03_IMAGE;

    @ColumnInfo(name = "FD03_SECTION_FD02")
    public long FD03_SECTION_FD02;

    public FD03_ENTITIES(long FD03_SECTION_FD02, String FD03_NAME) {
        this.FD03_SECTION_FD02 = FD03_SECTION_FD02;
        this.FD03_NAME = FD03_NAME;
    }
}
