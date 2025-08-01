package programmazionemobile.esercizi.personalcodex;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import programmazionemobile.esercizi.personalcodex.Adapters.CampaignSectionsAdapter;
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
import programmazionemobile.esercizi.personalcodex.Fragments.EditTitleDialog;

public class CampaignActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        FD01_CAMPAIGNS campaign = (FD01_CAMPAIGNS) i.getSerializableExtra("campaign");
        if (campaign != null) {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_campaign);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clCampaign), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            Map<FD02_CAMPAIGNS_SECTIONS, ArrayList<FD03_ENTITIES>> lstItems = new HashMap<>();
            MyDatabase db = MyDatabase.getInstance(this);
            CampaignsSectionsDAO sectionsDao = db.campaignsSectionsDAO();
            CampaignSectionsAccess sectionAccess = new CampaignSectionsAccess(sectionsDao);
            EntitiesDAO entitiesDao = db.entitiesDAO();
            EntitiesAccess entitiesAccess = new EntitiesAccess(entitiesDao);

            for (FD02_CAMPAIGNS_SECTIONS section : sectionAccess.getAll(campaign.ID))
                lstItems.put(section, new ArrayList<>(entitiesAccess.getAll(section.ID)));

            ExpandableListView expandableListView = findViewById(R.id.lvCampaign);
            CampaignSectionsAdapter campaignSectionsAdapter = new CampaignSectionsAdapter(lstItems);
            expandableListView.setAdapter(campaignSectionsAdapter);
            expandableListView.setOnItemLongClickListener((adapterView, view, i1, l) -> {
                if (ExpandableListView.getPackedPositionType(l) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    long packedPosition = expandableListView.getExpandableListPosition(i1);
                    int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                    FD02_CAMPAIGNS_SECTIONS section = (FD02_CAMPAIGNS_SECTIONS) campaignSectionsAdapter.getGroup(groupPosition);

                    OnClickListener clickListener = v -> {
                        EditTitleDialog fragment = (EditTitleDialog) getSupportFragmentManager().findFragmentByTag("editSectionDialog");
                        if (fragment != null) {
                            section.FD02_NAME = fragment.getText();
                            sectionAccess.update(section);
                            ((TextView) view.findViewById(R.id.txtCampaignSection)).setText(section.FD02_NAME);
                            fragment.dismiss();
                        }
                    };
                    EditTitleDialog dialog = new EditTitleDialog(section.FD02_NAME, clickListener);
                    dialog.show(getSupportFragmentManager(), "editSectionDialog");
                    return true;
                }

                return false;
            });

            CampaignsDAO campaignsDAO = db.campaignsDAO();
            CampaignsAccess campaignsAccess = new CampaignsAccess(campaignsDAO);

            TextView txtTile = findViewById(R.id.txtCampaignTitle);
            txtTile.setText(campaign.FD01_NAME);
            View.OnClickListener clickListener = v -> {
                EditTitleDialog fragment = (EditTitleDialog) getSupportFragmentManager().findFragmentByTag("EditTitleDialog");
                if (fragment != null) {
                    campaign.FD01_NAME = fragment.getText();
                    campaignsAccess.update(campaign);
                    txtTile.setText(campaign.FD01_NAME);
                    fragment.dismiss();
                }
            };
            txtTile.setOnLongClickListener(view -> {
                EditTitleDialog dialog = new EditTitleDialog(campaign.FD01_NAME, clickListener);
                dialog.show(getSupportFragmentManager(), "EditTitleDialog");
                return true;
            });

            findViewById(R.id.btnBackCampaign).setOnClickListener(view -> finish());
        } else {
            finish();
        }

    }
}
