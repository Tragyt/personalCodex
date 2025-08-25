package programmazionemobile.esercizi.personalcodex.Adapters.Links;

import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Adapters.EntitiesAdapter;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.R;

public class EntitiesNewLinkAdapter extends EntitiesAdapter {
    public interface OnEntityClickListener {
        void onEntityClicked(FD03_ENTITIES entity);
    }

    private final OnEntityClickListener listener;

    public EntitiesNewLinkAdapter(ArrayList<FD03_ENTITIES> entities, ActivityResultLauncher<Intent> launcher, OnEntityClickListener listener) {
        super(entities, launcher);
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        FD03_ENTITIES entity = entities.get(position);

        LinearLayout llEntity = holder.getLayout();
        TextView txtCampaignEntity = llEntity.findViewById(R.id.txtCampaignEntity);
        txtCampaignEntity.setText(entity.FD03_NAME);

        llEntity.setOnClickListener(view -> listener.onEntityClicked(entity));
    }
}
