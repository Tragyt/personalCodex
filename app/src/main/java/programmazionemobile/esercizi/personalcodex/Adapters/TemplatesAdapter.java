package programmazionemobile.esercizi.personalcodex.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import programmazionemobile.esercizi.personalcodex.CampaignActivity;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignSectionsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.SectionAccess;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsSectionsDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.SectionsDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.TP01_TEMPLATES;
import programmazionemobile.esercizi.personalcodex.Database.Entities.TP02_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.MyDatabase;
import programmazionemobile.esercizi.personalcodex.Helpers.TemplatesHelper;
import programmazionemobile.esercizi.personalcodex.R;
import programmazionemobile.esercizi.personalcodex.TemplateActivity;

public class TemplatesAdapter extends RecyclerView.Adapter<TemplatesAdapter.ViewHolder> {

    private final TP01_TEMPLATES[] templates;
    private final ActivityResultLauncher<Intent> activityResultLauncher;
    private final TemplatesHelper.TemplatesRoles role;

    public TemplatesAdapter(TP01_TEMPLATES[] templates, ActivityResultLauncher<Intent> activityResultLauncher, TemplatesHelper.TemplatesRoles role) {
        this.templates = templates;
        this.activityResultLauncher = activityResultLauncher;
        this.role = role;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_template, parent, false);

        return new TemplatesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView view = holder.getCardView();
        ((TextView) view.findViewById(R.id.txtTemplate)).setText(templates[position].TP01_NAME);

        view.setOnClickListener(view1 -> {
            Context context = holder.getCardView().getContext();

            if (role == TemplatesHelper.TemplatesRoles.OPTIONS) {
                Intent i = new Intent(context, TemplateActivity.class);
                i.putExtra("template", templates[position]);
                this.activityResultLauncher.launch(i);
            } else if (role == TemplatesHelper.TemplatesRoles.NEW_CAMPAIGN) {
                MyDatabase db = MyDatabase.getInstance(context);
                CampaignsDAO dao = db.campaignsDAO();
                CampaignsAccess campaignsAccess = new CampaignsAccess(dao);
                SectionsDAO sectionsDAO = db.sectionsDAO();
                SectionAccess sectionAccess = new SectionAccess(sectionsDAO);
                CampaignsSectionsDAO campaignsSectionsDAO = db.campaignsSectionsDAO();
                CampaignSectionsAccess campaignSectionsAccess = new CampaignSectionsAccess(campaignsSectionsDAO);

                FD01_CAMPAIGNS newCampaign = new FD01_CAMPAIGNS(context.getString(R.string.menu_new));
                newCampaign.ID = campaignsAccess.insert(newCampaign);

                for (TP02_SECTIONS section : sectionAccess.getAll(templates[position].ID)) {
                    FD02_CAMPAIGNS_SECTIONS campaignsSection = new FD02_CAMPAIGNS_SECTIONS(section.TP02_NAME, newCampaign.ID);
                    campaignSectionsAccess.insert(campaignsSection);
                }

                Intent i = new Intent(context, CampaignActivity.class);
                i.putExtra("campaign", newCampaign);
                this.activityResultLauncher.launch(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return templates.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.crdTemplate);
        }

        public CardView getCardView() {
            return cardView;
        }
    }
}
