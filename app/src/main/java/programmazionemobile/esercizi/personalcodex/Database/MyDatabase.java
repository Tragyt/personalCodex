package programmazionemobile.esercizi.personalcodex.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import programmazionemobile.esercizi.personalcodex.Database.DAOs.BondsDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsSectionsDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.EntitiesDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.SectionsDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.TemplatesDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD04_BONDS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.TP01_TEMPLATES;
import programmazionemobile.esercizi.personalcodex.Database.Entities.TP02_SECTIONS;

@androidx.room.Database(
        entities = {FD01_CAMPAIGNS.class, FD02_CAMPAIGNS_SECTIONS.class,
                FD03_ENTITIES.class, FD04_BONDS.class, TP01_TEMPLATES.class, TP02_SECTIONS.class},
        version = 1)
public abstract class MyDatabase extends RoomDatabase {
    private volatile static MyDatabase instance = null;

    public static MyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, MyDatabase.class, "mydatabase")
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            db.execSQL("INSERT INTO TP01_TEMPLATES (ID,TP01_NAME) VALUES (1,'dnd') ");
                            db.execSQL("INSERT INTO TP01_TEMPLATES (ID,TP01_NAME) VALUES (2,'narrative')");

                            db.execSQL("INSERT INTO TP02_SECTIONS (TP02_NAME,TP02_TEMPLATE_TP01) VALUES ('party',1)");
                            db.execSQL("INSERT INTO TP02_SECTIONS (TP02_NAME,TP02_TEMPLATE_TP01) VALUES ('npc',1)");
                            db.execSQL("INSERT INTO TP02_SECTIONS (TP02_NAME,TP02_TEMPLATE_TP01) VALUES ('places',1)");
                            db.execSQL("INSERT INTO TP02_SECTIONS (TP02_NAME,TP02_TEMPLATE_TP01) VALUES ('events',1)");
                            db.execSQL("INSERT INTO TP02_SECTIONS (TP02_NAME,TP02_TEMPLATE_TP01) VALUES ('quests',1)");

                            db.execSQL("INSERT INTO TP02_SECTIONS (TP02_NAME,TP02_TEMPLATE_TP01) VALUES ('characters',2)");
                            db.execSQL("INSERT INTO TP02_SECTIONS (TP02_NAME,TP02_TEMPLATE_TP01) VALUES ('places',2)");
                            db.execSQL("INSERT INTO TP02_SECTIONS (TP02_NAME,TP02_TEMPLATE_TP01) VALUES ('organizations',2)");
                        }
                    })
                    .build();
        }
        return instance;
    }

    public abstract CampaignsDAO campaignsDAO();
    public abstract BondsDAO bondsDAO();
    public abstract CampaignsSectionsDAO campaignsSectionsDAO();
    public abstract EntitiesDAO entitiesDAO();
    public abstract SectionsDAO sectionsDAO();
    public abstract TemplatesDAO templatesDAO();
}