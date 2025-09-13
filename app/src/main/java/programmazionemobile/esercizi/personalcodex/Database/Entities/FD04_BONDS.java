package programmazionemobile.esercizi.personalcodex.Database.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
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
public class FD04_BONDS implements Parcelable{
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

    public FD04_BONDS(Parcel in){
        this.FD04_ENTITY1_FD03 = in.readLong();
        this.FD04_ENTITY2_FD03 = in.readLong();
    }

    public static final Creator<FD04_BONDS> CREATOR = new Creator<FD04_BONDS>() {
        @Override
        public FD04_BONDS createFromParcel(Parcel in) {
            return new FD04_BONDS(in);
        }

        @Override
        public FD04_BONDS[] newArray(int size) {
            return new FD04_BONDS[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(FD04_ENTITY1_FD03);
        dest.writeLong(FD04_ENTITY2_FD03);
    }
}