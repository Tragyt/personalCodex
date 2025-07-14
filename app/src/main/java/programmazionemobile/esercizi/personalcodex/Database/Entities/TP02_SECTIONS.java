package programmazionemobile.esercizi.personalcodex.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = TP01_TEMPLATES.class, parentColumns = "ID",
        childColumns = "TP02_TEMPLATE_TP01", onDelete = ForeignKey.CASCADE),
        indices = @Index("TP02_TEMPLATE_TP01"))
public class TP02_SECTIONS {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public int ID;

    @ColumnInfo(name = "TP02_NAME")
    public String TP02_NAME;

    @ColumnInfo(name = "TP02_TEMPLATE_TP01")
    public long TP02_TEMPLATE_TP01;

    public TP02_SECTIONS(String TP02_NAME, long TP02_TEMPLATE_TP01) {
        this.TP02_NAME = TP02_NAME;
        this.TP02_TEMPLATE_TP01 = TP02_TEMPLATE_TP01;
    }
}
