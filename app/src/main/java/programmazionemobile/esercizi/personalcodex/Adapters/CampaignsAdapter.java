package programmazionemobile.esercizi.personalcodex.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.CampaignActivity;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignsAccess;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.R;
import programmazionemobile.esercizi.personalcodex.TemplateActivity;

public class CampaignsAdapter extends RecyclerView.Adapter<CampaignsAdapter.ViewHolder> {
    private final ArrayList<FD01_CAMPAIGNS> dataSet;
    private final CampaignsAccess campaignsAccess;
    private final ActivityResultLauncher<Intent> activityResultLauncher;

    public CampaignsAdapter(ArrayList<FD01_CAMPAIGNS> data, CampaignsAccess campaignsAccess, ActivityResultLauncher<Intent> activityResultLauncher) {
        dataSet = data;
        this.campaignsAccess = campaignsAccess;
        this.activityResultLauncher = activityResultLauncher;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_campaign, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView view = holder.getCardView();
        FD01_CAMPAIGNS campaign = dataSet.get(position);
        ((TextView) view.findViewById(R.id.txtCampaign)).setText(campaign.FD01_NAME);
        view.findViewById(R.id.btnInfoCampaign).setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.item_options, popupMenu.getMenu());
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(menuItem ->{
                int item = menuItem.getItemId();
                if(item == R.id.optDelete){
                    campaignsAccess.delete(campaign.ID);
                    dataSet.remove(campaign);
                    notifyItemRemoved(position);
                }
                return false;
            });
        });

        view.setOnClickListener(view1 -> {
            Context context = holder.getCardView().getContext();
            Intent i = new Intent(context, CampaignActivity.class);
            i.putExtra("campaign", campaign);
            activityResultLauncher.launch(i);
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.crdCampaign);
        }

        public CardView getCardView() {
            return cardView;
        }
    }
}
