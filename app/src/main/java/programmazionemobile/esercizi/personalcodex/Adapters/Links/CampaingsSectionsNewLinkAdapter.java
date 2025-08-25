package programmazionemobile.esercizi.personalcodex.Adapters.Links;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Adapters.CampaignSectionsAdapter;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignSectionsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.EntitiesAccess;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.Helpers.CampaignsHelper;
import programmazionemobile.esercizi.personalcodex.R;

public class CampaingsSectionsNewLinkAdapter extends CampaignSectionsAdapter {
    private final EntitiesNewLinkAdapter.OnEntityClickListener listener;

    public CampaingsSectionsNewLinkAdapter(ArrayList<CampaignsHelper.SectionEntities> sectionsEntities, EntitiesAccess entitiesAccess, CampaignSectionsAccess sectionAccess, FragmentManager fragmentManager, long campaignId, ActivityResultLauncher<Intent> launcher, EntitiesNewLinkAdapter.OnEntityClickListener listener) {
        super(sectionsEntities, entitiesAccess, sectionAccess, fragmentManager, campaignId, launcher);
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CampaignsHelper.SectionEntities sectionEntity = sectionsEntities.get(position);
        ArrayList<FD03_ENTITIES> entities = sectionEntity.getEntities();
        FD02_CAMPAIGNS_SECTIONS section = sectionEntity.getSection();

        EntitiesNewLinkAdapter entitiesAdapter = new EntitiesNewLinkAdapter(entities, launcher, listener);
        bind(holder, section, sectionEntity, entitiesAdapter);

        ImageButton btnAddEntity = cvSectionEntity.findViewById(R.id.btnAddEntity);
        btnAddEntity.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() - 1;
    }
}
