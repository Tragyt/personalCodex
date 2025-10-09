package programmazionemobile.esercizi.personalcodex.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.function.Consumer;

import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.BondsAccess;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD04_BONDS;
import programmazionemobile.esercizi.personalcodex.R;

public class ProposedLinksAdapter extends RecyclerView.Adapter<ProposedLinksAdapter.ItemViewHolder> {


    protected final ArrayList<FD03_ENTITIES> entities;
    private final long idEntity;
    private final BondsAccess bondsAccess;

    private final Consumer<FD04_BONDS> onItemClick;

    public ProposedLinksAdapter(ArrayList<FD03_ENTITIES> entities, long idEntity, BondsAccess bondsAccess, Consumer<FD04_BONDS> onItemClick) {
        this.entities = entities;
        this.idEntity = idEntity;
        this.bondsAccess = bondsAccess;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_campaign_entity, parent, false);
        return new ProposedLinksAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        TextView textView = holder.getTextView();
        LinearLayout linearLayout = holder.getLinearLayout();
        FD03_ENTITIES entity = entities.get(position);

        textView.setText(entity.FD03_NAME);
        linearLayout.setOnClickListener(v -> {
            FD04_BONDS bond = new FD04_BONDS(idEntity, entity.ID);
            bondsAccess.insert(bond);
            onItemClick.accept(bond);
        });
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final LinearLayout linearLayout;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtCampaignEntity);
            linearLayout = itemView.findViewById(R.id.llEntity);
        }

        public TextView getTextView() {
            return textView;
        }

        public LinearLayout getLinearLayout(){
            return linearLayout;
        }
    }
}
