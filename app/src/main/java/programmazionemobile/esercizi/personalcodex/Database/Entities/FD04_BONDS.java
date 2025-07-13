package programmazionemobile.esercizi.personalcodex.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"FD04_ENTITY1_FD03", "FD04_ENTITY2_FD03"},
        foreignKeys = {
                @ForeignKey(entity = FD03_ENTITIES.class, parentColumns = "ID",
                        childColumns = "FD04_ENTITY1_FD03", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = FD03_ENTITIES.class, parentColumns = "ID",
                        childColumns = "FD04_ENTITY2_FD03", onDelete = ForeignKey.CASCADE)})
public class FD04_BONDS {
    @ColumnInfo(name = "FD04_ENTITY1_FD03")
    public long FD04_ENTITY1_FD03;

    @ColumnInfo(name = "FD04_ENTITY2_FD03")
    public long FD04_ENTITY2_FD03;

    public FD04_BONDS(long FD04_ENTITY1_FD03, long FD04_ENTITY2_FD03) {
        this.FD04_ENTITY1_FD03 = FD04_ENTITY1_FD03;
        this.FD04_ENTITY2_FD03 = FD04_ENTITY2_FD03;
    }
}
