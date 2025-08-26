package programmazionemobile.esercizi.personalcodex.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
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
import programmazionemobile.esercizi.personalcodex.Fragments.DialogEdit;
import programmazionemobile.esercizi.personalcodex.Helpers.CampaignsHelper;
import programmazionemobile.esercizi.personalcodex.R;

public class CampaignSectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected ArrayList<CampaignsHelper.SectionEntities> sectionsEntities;
    private final EntitiesAccess entitiesAccess;
    private final CampaignSectionsAccess sectionAccess;
    private final FragmentManager fragmentManager;
    private final long campaignId;
    protected final ActivityResultLauncher<Intent> launcher;

    public CampaignSectionsAdapter(ArrayList<CampaignsHelper.SectionEntities> sectionsEntities, EntitiesAccess entitiesAccess, CampaignSectionsAccess sectionAccess, FragmentManager fragmentManager, long campaignId, ActivityResultLauncher<Intent> launcher) {
        this.sectionsEntities = sectionsEntities;
        this.entitiesAccess = entitiesAccess;
        this.sectionAccess = sectionAccess;
        this.fragmentManager = fragmentManager;
        this.campaignId = campaignId;
        this.launcher = launcher;
    }

    //potrebbe essere lento
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(ArrayList<CampaignsHelper.SectionEntities> sectionsEntities) {
        this.sectionsEntities = sectionsEntities;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_campaign_section, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_button_add, parent, false);
            return new FooterViewHolder(view);
        }
    }

    protected ConstraintLayout cvSectionEntity;
    private ConstraintLayout cvSection;

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            CampaignsHelper.SectionEntities sectionEntity = sectionsEntities.get(position);
            ArrayList<FD03_ENTITIES> entities = sectionEntity.getEntities();
            FD02_CAMPAIGNS_SECTIONS section = sectionEntity.getSection();

            EntitiesAdapter entitiesAdapter = new EntitiesAdapter(entities, launcher);
            bind(holder, section, sectionEntity, entitiesAdapter);

            Context context = cvSectionEntity.getContext();
            //creazione nuova entity
            ImageButton btnAddEntity = cvSectionEntity.findViewById(R.id.btnAddEntity);
            btnAddEntity.setOnClickListener(view -> {
                FD03_ENTITIES entity = new FD03_ENTITIES(section.ID, context.getString(R.string.btnNewCampaign_description));
                entity.ID = entitiesAccess.insert(entity);

                Intent intent = new Intent(context, EntityActivity.class);
                intent.putExtra("entity", entity);
                launcher.launch(intent);
            });

            //modifica sezione tenendo premuto
            cvSection.setOnLongClickListener(view -> {
                //click listener modifica titolo
                View.OnClickListener editClickListener = v -> {
                    DialogEdit fragment = (DialogEdit) fragmentManager.findFragmentByTag("editSectionDialog");
                    if (fragment != null) {
                        section.FD02_NAME = fragment.getText();
                        sectionAccess.update(section);
                        notifyItemChanged(holder.getAdapterPosition());
                        fragment.dismiss();
                    }
                };
                //click listener elimina
                View.OnClickListener deleteClickListener = v -> {
                    DialogEdit fragment = (DialogEdit) fragmentManager.findFragmentByTag("editSectionDialog");
                    if (fragment != null) {
                        if (sectionEntity.getEntities().isEmpty()) {
                            sectionAccess.delete(section.ID);
                            sectionsEntities.remove(sectionEntity);
                            notifyItemRemoved(holder.getAdapterPosition());
                        } else {
                            new AlertDialog.Builder(context).setTitle(context.getString(R.string.txtDialogWarning)).setMessage(context.getString(R.string.txtDialogWarningDeleteSection)).setPositiveButton(android.R.string.ok, null).show();
                        }

                        fragment.dismiss();
                    }
                };
                DialogEdit dialog = new DialogEdit(section.FD02_NAME, editClickListener, deleteClickListener);
                dialog.show(fragmentManager, "editSectionDialog");
                return true;
            });
        } else { //item footer
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            ImageButton btnAdd = footerHolder.getButton();
            btnAdd.setOnClickListener(view -> {
                FD02_CAMPAIGNS_SECTIONS new_section = new FD02_CAMPAIGNS_SECTIONS(view.getContext().getString(R.string.newSection_title), campaignId);
                new_section.ID = sectionAccess.insert(new_section);
                sectionsEntities.add(new CampaignsHelper.SectionEntities(new_section, new ArrayList<>()));
                notifyItemInserted(footerHolder.getAdapterPosition());
            });
        }

    }

    protected void bind(RecyclerView.ViewHolder holder, FD02_CAMPAIGNS_SECTIONS section, CampaignsHelper.SectionEntities sectionEntity, EntitiesAdapter entitiesAdapter) {
        cvSectionEntity = ((ItemViewHolder)holder).getLayout();
        cvSection = cvSectionEntity.findViewById(R.id.cvSection);

        Context context = cvSectionEntity.getContext();
        ImageView imgArrow = cvSectionEntity.findViewById(R.id.imgArrow);
        RecyclerView rcvEntities = cvSectionEntity.findViewById(R.id.rcvEntities);
        ((TextView)cvSectionEntity.findViewById(R.id.txtCampaignSection)).setText(section.FD02_NAME);

        //gestione espansione
        if (sectionEntity.isExpanded()) {
            imgArrow.setImageResource(R.drawable.expandable_list_up);

            //setup reciclerview figli
            rcvEntities.setVisibility(View.VISIBLE);

            rcvEntities.setLayoutManager(new LinearLayoutManager(context));
            rcvEntities.setHasFixedSize(true);
            rcvEntities.setAdapter(entitiesAdapter);
        } else {
            imgArrow.setImageResource(R.drawable.expandable_list_down);
            rcvEntities.setVisibility(View.GONE);
        }

        //espandi o riduci al tap
        cvSection.setOnClickListener(view -> {
            sectionEntity.expand_reduce();
            notifyItemChanged(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        int size = sectionsEntities.size();
        return size + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == sectionsEntities.size()) return 0;
        else return 1;
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

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton btnAdd;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.btnAdd = itemView.findViewById(R.id.btnAddListElement);
        }

        public ImageButton getButton() {
            return btnAdd;
        }
    }
}
