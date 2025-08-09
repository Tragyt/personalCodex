package programmazionemobile.esercizi.personalcodex.Adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.SectionAccess;
import programmazionemobile.esercizi.personalcodex.Database.Entities.TP02_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Helpers.CampaignsHelper;
import programmazionemobile.esercizi.personalcodex.R;

public class TemplateSectionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<TP02_SECTIONS> sections;
    private final SectionAccess sectionAccess;
    private final Long idTemplate;

    public TemplateSectionsAdapter(ArrayList<TP02_SECTIONS> sections, SectionAccess sectionAccess, Long idTemplate) {
        this.sections = sections;
        this.sectionAccess = sectionAccess;
        this.idTemplate = idTemplate;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_templates_section, parent, false);
            return new TemplateSectionsAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_button_add, parent, false);
            return new TemplateSectionsAdapter.FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TemplateSectionsAdapter.ItemViewHolder) {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            RelativeLayout layout = itemHolder.getLayout();

            EditText txt = layout.findViewById(R.id.txtTemplateSection);
            TP02_SECTIONS section = sections.get(position);
            txt.setText(section.TP02_NAME);
            txt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    section.TP02_NAME = editable.toString();
                    sectionAccess.update(section);
                }
            });

            ImageView btn = layout.findViewById(R.id.btnRemoveTemplateSection);
            btn.setOnClickListener(view -> {
                sectionAccess.delete(section.ID);
                sections.removeIf(s -> s.ID == section.ID);
                notifyItemRemoved(itemHolder.getAdapterPosition());
            });
        } else {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            ImageButton btnAdd = footerHolder.getButton();
            btnAdd.setOnClickListener(view -> {
                TP02_SECTIONS new_section = new TP02_SECTIONS(view.getContext().getString(R.string.new_section), idTemplate);
                new_section.ID = sectionAccess.insert(new_section);
                sections.add(new_section);
                notifyItemInserted(footerHolder.getAdapterPosition());
            });
        }
    }

    @Override
    public int getItemCount() {
        return sections.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == sections.size()) return 0;
        else return 1;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout rlItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            rlItem = itemView.findViewById(R.id.rlItem);
        }

        public RelativeLayout getLayout() {
            return rlItem;
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
