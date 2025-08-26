package programmazionemobile.esercizi.personalcodex;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Adapters.CampaignSectionsAdapter;
import programmazionemobile.esercizi.personalcodex.Adapters.Links.CampaingsSectionsNewLinkAdapter;
import programmazionemobile.esercizi.personalcodex.Adapters.Links.EntitiesNewLinkAdapter;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignSectionsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.EntitiesAccess;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsSectionsDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.EntitiesDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.Database.MyDatabase;
import programmazionemobile.esercizi.personalcodex.Fragments.DialogEdit;
import programmazionemobile.esercizi.personalcodex.Helpers.CampaignsHelper;

public class CampaignActivity extends AppCompatActivity {
    private CampaignSectionsAdapter campaignSectionsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //dati intent
        Intent i = getIntent();
        FD01_CAMPAIGNS campaign = (FD01_CAMPAIGNS) i.getSerializableExtra("campaign");
        CampaignsHelper.CampaignRole role = (CampaignsHelper.CampaignRole) i.getSerializableExtra("role");
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
            MyDatabase db = MyDatabase.getInstance(this);
            CampaignsSectionsDAO sectionsDao = db.campaignsSectionsDAO();
            CampaignSectionsAccess sectionAccess = new CampaignSectionsAccess(sectionsDao);
            EntitiesDAO entitiesDao = db.entitiesDAO();
            EntitiesAccess entitiesAccess = new EntitiesAccess(entitiesDao);
            CampaignsDAO campaignsDAO = db.campaignsDAO();
            CampaignsAccess campaignsAccess = new CampaignsAccess(campaignsDAO);

            //lista sezioni
            ArrayList<CampaignsHelper.SectionEntities> lstItems = getSections(sectionAccess, campaign, entitiesAccess);

            //intent launcher da passare all'adapter
            ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> updateAdapter(getSections(sectionAccess, campaign, entitiesAccess)));

            RecyclerView expandableListView = findViewById(R.id.lvCampaign);
            TextView txtTile = findViewById(R.id.txtCampaignTitle);
            if (role == CampaignsHelper.CampaignRole.VIEW) {
                //setup recyclerview
                campaignSectionsAdapter = new CampaignSectionsAdapter(lstItems, entitiesAccess, sectionAccess, getSupportFragmentManager(), campaign.ID, launcher);

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
            } else if (role == CampaignsHelper.CampaignRole.NEW_LINK){
                txtTile.setText(getString(R.string.txtNewBond));

                EntitiesNewLinkAdapter.OnEntityClickListener listener = entity -> {
                    Intent intent = new Intent();
                    intent.putExtra("entity", entity);
                    setResult(CampaignActivity.RESULT_OK, intent);
                    finish();
                };

                FD03_ENTITIES entity = (FD03_ENTITIES) i.getSerializableExtra("entity");
                campaignSectionsAdapter = new CampaingsSectionsNewLinkAdapter(lstItems, entitiesAccess, sectionAccess, getSupportFragmentManager(), campaign.ID,
                        launcher, listener, entity);
            }

            expandableListView.setLayoutManager(new LinearLayoutManager(this));
            expandableListView.setAdapter(campaignSectionsAdapter);

            findViewById(R.id.btnBackCampaign).setOnClickListener(view -> finish());
        } else {
            finish();
        }

    }

    private ArrayList<CampaignsHelper.SectionEntities> getSections(CampaignSectionsAccess sectionAccess, FD01_CAMPAIGNS campaign, EntitiesAccess entitiesAccess) {
        ArrayList<CampaignsHelper.SectionEntities> ret = new ArrayList<>();
        ArrayList<FD02_CAMPAIGNS_SECTIONS> sections = sectionAccess.getAll(campaign.ID);
        for (FD02_CAMPAIGNS_SECTIONS section : sections)
            ret.add(new CampaignsHelper.SectionEntities(section, new ArrayList<>(entitiesAccess.getAll(section.ID))));
        return ret;
    }

    private void updateAdapter(ArrayList<CampaignsHelper.SectionEntities> sectionEntities) {
        campaignSectionsAdapter.updateData(sectionEntities);
    }
}
