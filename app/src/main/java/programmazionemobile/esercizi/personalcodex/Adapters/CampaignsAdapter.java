package programmazionemobile.esercizi.personalcodex.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.R;

public class CampaignsAdapter extends RecyclerView.Adapter<CampaignsAdapter.ViewHolder> {
    private FD01_CAMPAIGNS[] dataSet;

    public CampaignsAdapter(FD01_CAMPAIGNS[] data) {
        dataSet = data;
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
        ((TextView) view.findViewById(R.id.txtCampaign)).setText(dataSet[position].FD01_NAME);
        ((ImageButton) view.findViewById(R.id.btnInfoCampaign)).setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), v);
            popupMenu.getMenuInflater().inflate(R.menu.item_options, popupMenu.getMenu());
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
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
