package programmazionemobile.esercizi.personalcodex.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = FD01_CAMPAIGNS.class, parentColumns = "ID",
        childColumns = "FD02_CAMPAIGN_FD01", onDelete = ForeignKey.CASCADE),
        indices = @Index("FD02_CAMPAIGN_FD01"))
public class FD02_CAMPAIGNS_SECTIONS {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    public long ID;

    @ColumnInfo(name = "FD02_NAME")
    public String FD02_NAME;

    @ColumnInfo(name = "FD02_CAMPAIGN_FD01")
    public int FD02_CAMPAIGN_FD01;

    public FD02_CAMPAIGNS_SECTIONS(String FD02_NAME, int FD02_CAMPAIGN_FD01) {
        this.FD02_NAME = FD02_NAME;
        this.FD02_CAMPAIGN_FD01 = FD02_CAMPAIGN_FD01;
    }
}
