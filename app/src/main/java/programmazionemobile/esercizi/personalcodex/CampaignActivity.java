package programmazionemobile.esercizi.personalcodex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignSectionsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.EntitiesAccess;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsSectionsDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.EntitiesDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.MyDatabase;

public class CampaignActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_campaign);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clCampaign), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent i = getIntent();
        FD01_CAMPAIGNS campaign = (FD01_CAMPAIGNS) i.getSerializableExtra("campaign");
        if (campaign != null) {
            Map<String, ArrayList<String>> lstItems = new HashMap<>();

            MyDatabase db = MyDatabase.getInstance(this);
            CampaignsSectionsDAO sectionsDao = db.campaignsSectionsDAO();
            CampaignSectionsAccess sectionAccess = new CampaignSectionsAccess(sectionsDao);
            EntitiesDAO entitiesDao = db.entitiesDAO();
            EntitiesAccess entitiesAccess = new EntitiesAccess(entitiesDao);

            for (FD02_CAMPAIGNS_SECTIONS section : sectionAccess.getAll(campaign.ID))
                lstItems.put(section.FD02_NAME,
                        entitiesAccess.getAll(section.ID).stream()
                                .map(entity -> entity.FD03_NAME)
                                .collect(Collectors.toCollection(ArrayList::new)));

            ExpandableListView expandableListView = findViewById(R.id.lvCampaign);
        }
    }
}
