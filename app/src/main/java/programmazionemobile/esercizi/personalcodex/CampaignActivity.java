package programmazionemobile.esercizi.personalcodex;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Adapters.CampaignSectionsAdapter;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignSectionsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.EntitiesAccess;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsSectionsDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.EntitiesDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.MyDatabase;
import programmazionemobile.esercizi.personalcodex.Fragments.DialogEdit;
import programmazionemobile.esercizi.personalcodex.Helpers.CampaignsHelper;

public class CampaignActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //dati intent
        Intent i = getIntent();
        FD01_CAMPAIGNS campaign = (FD01_CAMPAIGNS) i.getSerializableExtra("campaign");
        if (campaign != null) {

            //setup activity schermo
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_campaign);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clCampaign), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            //setup dati
            ArrayList<CampaignsHelper.SectionEntities> lstItems = new ArrayList<>();
            MyDatabase db = MyDatabase.getInstance(this);
            CampaignsSectionsDAO sectionsDao = db.campaignsSectionsDAO();
            CampaignSectionsAccess sectionAccess = new CampaignSectionsAccess(sectionsDao);
            EntitiesDAO entitiesDao = db.entitiesDAO();
            EntitiesAccess entitiesAccess = new EntitiesAccess(entitiesDao);
            CampaignsDAO campaignsDAO = db.campaignsDAO();
            CampaignsAccess campaignsAccess = new CampaignsAccess(campaignsDAO);

            //lista sezioni
            ArrayList<FD02_CAMPAIGNS_SECTIONS> sections = sectionAccess.getAll(campaign.ID);
            for (FD02_CAMPAIGNS_SECTIONS section : sections)
                lstItems.add(new CampaignsHelper.SectionEntities(section, new ArrayList<>(entitiesAccess.getAll(section.ID))));

            //setup recyclerview
            RecyclerView expandableListView = findViewById(R.id.lvCampaign);
            expandableListView.setLayoutManager(new LinearLayoutManager(this));
            CampaignSectionsAdapter campaignSectionsAdapter = new CampaignSectionsAdapter(lstItems, entitiesAccess, sectionAccess, getSupportFragmentManager(), campaign.ID);
            expandableListView.setAdapter(campaignSectionsAdapter);

            //setup titolo
            TextView txtTile = findViewById(R.id.txtCampaignTitle);
            txtTile.setText(campaign.FD01_NAME);

            //setup dialog modifica titolo
            View.OnClickListener clickListener = v -> { //modifica titolo alla conferma
                DialogEdit fragment = (DialogEdit) getSupportFragmentManager().findFragmentByTag("EditTitleDialog");
                if (fragment != null) {
                    campaign.FD01_NAME = fragment.getText();
                    campaignsAccess.update(campaign);
                    txtTile.setText(campaign.FD01_NAME);
                    fragment.dismiss();
                }
            };
            txtTile.setOnLongClickListener(view -> { //apertura dialog tenendo premuto
                DialogEdit dialog = new DialogEdit(campaign.FD01_NAME, clickListener);
                dialog.show(getSupportFragmentManager(), "EditTitleDialog");
                return true;
            });

            findViewById(R.id.btnBackCampaign).setOnClickListener(view -> finish());
        } else {
            finish();
        }

    }
}
