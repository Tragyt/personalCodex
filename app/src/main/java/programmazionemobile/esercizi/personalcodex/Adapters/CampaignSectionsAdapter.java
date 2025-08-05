package programmazionemobile.esercizi.personalcodex.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignSectionsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.EntitiesAccess;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.EntityActivity;
import programmazionemobile.esercizi.personalcodex.Fragments.EditTitleDialog;
import programmazionemobile.esercizi.personalcodex.Helpers.CampaignsHelper;
import programmazionemobile.esercizi.personalcodex.R;

public class CampaignSectionsAdapter extends RecyclerView.Adapter<CampaignSectionsAdapter.ItemViewHolder> {

    private final ArrayList<CampaignsHelper.SectionEntities> sectionsEntities;
    private final EntitiesAccess entitiesAccess;
    private final CampaignSectionsAccess sectionAccess;
    private final FragmentManager fragmentManager;

    public CampaignSectionsAdapter(ArrayList<CampaignsHelper.SectionEntities> sectionsEntities, EntitiesAccess entitiesAccess, CampaignSectionsAccess sectionAccess, FragmentManager fragmentManager) {
        this.sectionsEntities = sectionsEntities;
        this.entitiesAccess = entitiesAccess;
        this.sectionAccess = sectionAccess;
        this.fragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_campaign_section, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        CampaignsHelper.SectionEntities sectionEntity = sectionsEntities.get(position);
        FD02_CAMPAIGNS_SECTIONS section = sectionEntity.getSection();
        ArrayList<FD03_ENTITIES> entities = sectionEntity.getEntities();

        ConstraintLayout cvSectionEntity = holder.getLayout();
        TextView txtCampaignSection = cvSectionEntity.findViewById(R.id.txtCampaignSection);
        ImageView imgArrow = cvSectionEntity.findViewById(R.id.imgArrow);
        RecyclerView rcvEntities = cvSectionEntity.findViewById(R.id.rcvEntities);
        ConstraintLayout cvSection = cvSectionEntity.findViewById(R.id.cvSection);
        Context context = cvSectionEntity.getContext();

        txtCampaignSection.setText(section.FD02_NAME);
        txtCampaignSection.setTypeface(null, Typeface.BOLD);

        if (sectionEntity.isExpanded()) {
            imgArrow.setImageResource(R.drawable.expandable_list_up);
            rcvEntities.setVisibility(View.VISIBLE);

            EntitiesAdapter entitiesAdapter = new EntitiesAdapter(entities);
            rcvEntities.setLayoutManager(new LinearLayoutManager(context));
            rcvEntities.setHasFixedSize(true);
            rcvEntities.setAdapter(entitiesAdapter);
        } else {
            imgArrow.setImageResource(R.drawable.expandable_list_down);
            rcvEntities.setVisibility(View.GONE);
        }

        cvSection.setOnClickListener(view -> {
            sectionEntity.expand_reduce();
            notifyItemChanged(position);
        });

        cvSectionEntity.findViewById(R.id.btnAddEntity).setOnClickListener(view -> {
            FD03_ENTITIES entity = new FD03_ENTITIES(section.ID, context.getString(R.string.btnNewCampaign_description));
            entity.ID = entitiesAccess.insert(entity);
            sectionEntity.add(entity);
            notifyItemChanged(position);

            Intent intent = new Intent(context, EntityActivity.class);
            intent.putExtra("entity", entity);
            context.startActivity(intent);
        });
        //        btn.setFocusable(false);

        cvSection.setOnLongClickListener(view -> {
            View.OnClickListener clickListener = v -> {
                EditTitleDialog fragment = (EditTitleDialog) fragmentManager.findFragmentByTag("editSectionDialog");
                if (fragment != null) {
                    section.FD02_NAME = fragment.getText();
                    sectionAccess.update(section);
                    txtCampaignSection.setText(section.FD02_NAME);
                    fragment.dismiss();
                }
            };
            EditTitleDialog dialog = new EditTitleDialog(section.FD02_NAME, clickListener);
            dialog.show(fragmentManager, "editSectionDialog");
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return sectionsEntities.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout cvSectionEntity;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cvSectionEntity = itemView.findViewById(R.id.cvSectionEntity);
        }

        public ConstraintLayout getLayout() {
            return cvSectionEntity;
        }
    }
}
