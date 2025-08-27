package programmazionemobile.esercizi.personalcodex.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(primaryKeys = {"FD04_ENTITY1_FD03", "FD04_ENTITY2_FD03"},
        foreignKeys = {
                @ForeignKey(entity = FD03_ENTITIES.class, parentColumns = "ID",
                        childColumns = "FD04_ENTITY1_FD03", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = FD03_ENTITIES.class, parentColumns = "ID",
                        childColumns = "FD04_ENTITY2_FD03", onDelete = ForeignKey.CASCADE)},
        indices = @Index("FD04_ENTITY2_FD03"))
public class FD04_BONDS {
    @ColumnInfo(name = "FD04_ENTITY1_FD03")
    public long FD04_ENTITY1_FD03;

    @ColumnInfo(name = "FD04_ENTITY2_FD03")
    public long FD04_ENTITY2_FD03;

    @ColumnInfo(name = "FD04_DESCRIPTION")
    public String FD04_DESCRIPTION;

    public FD04_BONDS(long FD04_ENTITY1_FD03, long FD04_ENTITY2_FD03) {
        this.FD04_ENTITY1_FD03 = FD04_ENTITY1_FD03;
        this.FD04_ENTITY2_FD03 = FD04_ENTITY2_FD03;
    }
}
