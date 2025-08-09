package programmazionemobile.esercizi.personalcodex.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.R;

public class EntitiesAdapter extends RecyclerView.Adapter<EntitiesAdapter.ItemViewHolder>{

    private final ArrayList<FD03_ENTITIES> entities;

    public EntitiesAdapter(ArrayList<FD03_ENTITIES> entities) {
        this.entities = entities;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_campaign_entity, parent, false);
        return new EntitiesAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        FD03_ENTITIES entity = entities.get(position);

        LinearLayout llEntity = holder.getLayout();
        TextView txtCampaignEntity = llEntity.findViewById(R.id.txtCampaignEntity);

        txtCampaignEntity.setText(entity.FD03_NAME);

        llEntity.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout llEntity;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            llEntity = itemView.findViewById(R.id.llEntity);
        }

        public LinearLayout getLayout(){
            return llEntity;
        }
    }
}
