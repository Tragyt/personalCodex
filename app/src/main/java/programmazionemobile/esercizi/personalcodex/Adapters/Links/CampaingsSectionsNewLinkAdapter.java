package programmazionemobile.esercizi.personalcodex.Adapters.Links;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.stream.Collectors;

import programmazionemobile.esercizi.personalcodex.Adapters.CampaignSectionsAdapter;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignSectionsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.EntitiesAccess;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD04_BONDS;
import programmazionemobile.esercizi.personalcodex.Helpers.CampaignsHelper;
import programmazionemobile.esercizi.personalcodex.R;

public class CampaingsSectionsNewLinkAdapter extends CampaignSectionsAdapter {
    private final EntitiesNewLinkAdapter.OnEntityClickListener listener;
    private final FD03_ENTITIES entity;
    private final ArrayList<FD04_BONDS> lnkExcluded;

    public CampaingsSectionsNewLinkAdapter(ArrayList<CampaignsHelper.SectionEntities> sectionsEntities, EntitiesAccess entitiesAccess,
                                           CampaignSectionsAccess sectionAccess, FragmentManager fragmentManager, long campaignId,
                                           ActivityResultLauncher<Intent> launcher, EntitiesNewLinkAdapter.OnEntityClickListener listener,
                                           FD03_ENTITIES entity, ArrayList<FD04_BONDS> links) {
        super(sectionsEntities, entitiesAccess, sectionAccess, fragmentManager, campaignId, launcher);
        this.listener = listener;
        this.entity = entity;
        this.lnkExcluded = links;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CampaignsHelper.SectionEntities sectionEntity = filteredList.get(position);

        if (entity.FD03_SECTION_FD02 == sectionEntity.getSection().ID)
            sectionEntity.removeEntity(entity.ID);

        ArrayList<FD03_ENTITIES> entitiesToRemove = sectionEntity.getEntities().stream().filter(x -> lnkExcluded.stream().anyMatch(y -> y.FD04_ENTITY1_FD03 == x.ID || y.FD04_ENTITY2_FD03 == x.ID))
                .collect(Collectors.toCollection(ArrayList::new));
        for(FD03_ENTITIES e: entitiesToRemove)
            sectionEntity.removeEntity(e.ID);

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
