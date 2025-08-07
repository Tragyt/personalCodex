package programmazionemobile.esercizi.personalcodex.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignSectionsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.EntitiesAccess;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.EntityActivity;
import programmazionemobile.esercizi.personalcodex.Fragments.DialogEditText;
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
        //selezione dati
        CampaignsHelper.SectionEntities sectionEntity = sectionsEntities.get(position);
        FD02_CAMPAIGNS_SECTIONS section = sectionEntity.getSection();
        ArrayList<FD03_ENTITIES> entities = sectionEntity.getEntities();

        //selezione views
        ConstraintLayout cvSectionEntity = holder.getLayout();
        TextView txtCampaignSection = cvSectionEntity.findViewById(R.id.txtCampaignSection);
        ImageView imgArrow = cvSectionEntity.findViewById(R.id.imgArrow);
        RecyclerView rcvEntities = cvSectionEntity.findViewById(R.id.rcvEntities);
        ConstraintLayout cvSection = cvSectionEntity.findViewById(R.id.cvSection);
        Context context = cvSectionEntity.getContext();

        txtCampaignSection.setText(section.FD02_NAME);

        //gestione espansione
        if (sectionEntity.isExpanded()) {
            imgArrow.setImageResource(R.drawable.expandable_list_up);

            //setup reciclerview figli
            rcvEntities.setVisibility(View.VISIBLE);
            EntitiesAdapter entitiesAdapter = new EntitiesAdapter(entities);
            rcvEntities.setLayoutManager(new LinearLayoutManager(context));
            rcvEntities.setHasFixedSize(true);
            rcvEntities.setAdapter(entitiesAdapter);

            //eliminazione entity
            ItemTouchHelper swipeDelete = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) { //swipe a sinistra per eliminare
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    FD03_ENTITIES entity = entities.get(position);
                    entities.remove(entity);
                    entitiesAccess.delete(entity);
                    entitiesAdapter.notifyItemRemoved(position);
                    notifyItemChanged(holder.getAdapterPosition());
                }

                //per disegnare cestino con sfondo rosso durante lo swipe
                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    View itemView = viewHolder.itemView;

                    if (dX == 0 && !isCurrentlyActive) {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, false);
                        return;
                    }

                    Paint paint = new Paint();
                    paint.setColor(Color.RED);

                    // disegna un rettangolo nella posizione vuota lasciata dallo swipe verso sinistra
                    c.drawRect((float) itemView.getRight() + dX,
                            (float) itemView.getTop(),
                            (float) itemView.getRight(),
                            (float) itemView.getBottom(),
                            paint);

                    // disegna l'icona
                    Drawable icon = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.ic_delete_black);
                    if (icon != null) {
                        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                        int iconTop = itemView.getTop() + iconMargin;
                        int iconBottom = iconTop + icon.getIntrinsicHeight();

                        int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                        int iconRight = itemView.getRight() - iconMargin;

                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        icon.draw(c);
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            });
            swipeDelete.attachToRecyclerView(rcvEntities);

        } else {
            imgArrow.setImageResource(R.drawable.expandable_list_down);
            rcvEntities.setVisibility(View.GONE);
        }

        //espandi o riduci al tap
        cvSection.setOnClickListener(view -> {
            sectionEntity.expand_reduce();
            notifyItemChanged(holder.getAdapterPosition());
        });

        //creazione nuova entity
        cvSectionEntity.findViewById(R.id.btnAddEntity).setOnClickListener(view -> {
            FD03_ENTITIES entity = new FD03_ENTITIES(section.ID, context.getString(R.string.btnNewCampaign_description));
            entity.ID = entitiesAccess.insert(entity);
            sectionEntity.add(entity);
            notifyItemChanged(holder.getAdapterPosition());

            Intent intent = new Intent(context, EntityActivity.class);
            intent.putExtra("entity", entity);
            context.startActivity(intent);
        });

        //modifica sezione tenendo premuto
        cvSection.setOnLongClickListener(view -> {
            View.OnClickListener clickListener = v -> {
                DialogEditText fragment = (DialogEditText) fragmentManager.findFragmentByTag("editSectionDialog");
                if (fragment != null) {
                    section.FD02_NAME = fragment.getText();
                    sectionAccess.update(section);
                    txtCampaignSection.setText(section.FD02_NAME);
                    fragment.dismiss();
                }
            };
            DialogEditText dialog = new DialogEditText(section.FD02_NAME, clickListener);
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
