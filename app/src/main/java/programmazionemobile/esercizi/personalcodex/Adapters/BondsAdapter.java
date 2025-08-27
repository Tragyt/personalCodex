package programmazionemobile.esercizi.personalcodex.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.BondsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.EntitiesAccess;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD04_BONDS;
import programmazionemobile.esercizi.personalcodex.R;

public class BondsAdapter extends RecyclerView.Adapter<BondsAdapter.ViewHolder> {

    private ArrayList<FD04_BONDS> bonds;
    private final BondsAccess bondsAccess;
    private final EntitiesAccess entitiesAccess;
    private final long idEntity;

    public BondsAdapter(BondsAccess bondsAccess, ArrayList<FD04_BONDS> bonds, EntitiesAccess entitiesAccess, long idEntity) {
        this.bondsAccess = bondsAccess;
        this.bonds = bonds;
        this.entitiesAccess = entitiesAccess;
        this.idEntity = idEntity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bond, parent, false);
        return new BondsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FD04_BONDS bond = bonds.get(position);
        FD03_ENTITIES entity = bond.FD04_ENTITY1_FD03 == idEntity ? entitiesAccess.get(bond.FD04_ENTITY2_FD03) : entitiesAccess.get(bond.FD04_ENTITY1_FD03);

        holder.getTxtEntityBond().setText(entity.FD03_NAME);

        TextView txtBondDescription = holder.getTxtBondDescription();
        txtBondDescription.setText(bond.FD04_DESCRIPTION);
        txtBondDescription.setVisibility(View.VISIBLE);

        EditText txtBondDescriptionEdit = holder.getTxtBondDescriptionEdit();
        txtBondDescriptionEdit.setText(bond.FD04_DESCRIPTION);
        txtBondDescriptionEdit.setVisibility(View.GONE);

        ImageButton btnSaveBond = holder.getBtnSaveBond();
        btnSaveBond.setVisibility(View.GONE);
        btnSaveBond.setOnClickListener(v -> {
            bond.FD04_DESCRIPTION = txtBondDescriptionEdit.getText().toString();
            bondsAccess.update(bond);
            notifyItemChanged(holder.getBindingAdapterPosition());
        });

        ImageButton btnDeleteBond = holder.getBtnDeleteBond();
        btnDeleteBond.setVisibility(View.GONE);
        btnDeleteBond.setOnClickListener(v -> {
            bondsAccess.delete(bond.FD04_ENTITY1_FD03,bond.FD04_ENTITY2_FD03);
            bonds.removeIf(b -> b.FD04_ENTITY1_FD03 == bond.FD04_ENTITY1_FD03 && b.FD04_ENTITY2_FD03 == bond.FD04_ENTITY2_FD03);
            notifyItemRemoved(holder.getBindingAdapterPosition());
        });

        ImageButton btnEditBond = holder.getBtnEditBond();
        btnEditBond.setVisibility(View.VISIBLE);
        btnEditBond.setOnClickListener(v -> {
            btnEditBond.setVisibility(View.GONE);
            txtBondDescription.setVisibility(View.GONE);
            btnSaveBond.setVisibility(View.VISIBLE);
            txtBondDescriptionEdit.setVisibility(View.VISIBLE);
            btnDeleteBond.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return bonds.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtEntityBond;
        private final ImageButton btnEditBond;
        private final ImageButton btnSaveBond;
        private final ImageButton btnDeleteBond;
        private final TextView txtBondDescription;
        private final EditText txtBondDescriptionEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtEntityBond = itemView.findViewById(R.id.txtEntityBond);
            this.btnEditBond = itemView.findViewById(R.id.btnEditBond);
            this.btnSaveBond = itemView.findViewById(R.id.btnSaveBond);
            this.btnDeleteBond = itemView.findViewById(R.id.btnDeleteBond);
            this.txtBondDescription = itemView.findViewById(R.id.txtBondDescription);
            this.txtBondDescriptionEdit = itemView.findViewById(R.id.txtBondDescriptionEdit);
        }

        public TextView getTxtEntityBond() {
            return txtEntityBond;
        }

        public ImageButton getBtnEditBond() {
            return btnEditBond;
        }

        public ImageButton getBtnSaveBond() {
            return btnSaveBond;
        }

        public ImageButton getBtnDeleteBond() {
            return btnDeleteBond;
        }

        public TextView getTxtBondDescription() {
            return txtBondDescription;
        }

        public EditText getTxtBondDescriptionEdit() {
            return txtBondDescriptionEdit;
        }
    }
}
