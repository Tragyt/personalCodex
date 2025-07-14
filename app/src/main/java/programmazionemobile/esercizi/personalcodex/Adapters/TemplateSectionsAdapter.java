package programmazionemobile.esercizi.personalcodex.Adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.SectionAccess;
import programmazionemobile.esercizi.personalcodex.Database.Entities.TP02_SECTIONS;
import programmazionemobile.esercizi.personalcodex.R;

public class TemplateSectionsAdapter extends ArrayAdapter<TP02_SECTIONS> {
    private final ArrayList<TP02_SECTIONS> sections;
    private final Context context;
    private final SectionAccess sectionAccess;

    public TemplateSectionsAdapter(Context context, ArrayList<TP02_SECTIONS> sections, SectionAccess sectionAccess) {
        super(context, R.layout.item_templates_section, sections);
        this.sections = sections;
        this.context = context;
        this.sectionAccess = sectionAccess;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_templates_section, parent, false);

            EditText txt = convertView.findViewById(R.id.txtTemplateSection);
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

            ImageView btn = convertView.findViewById(R.id.btnRemoveTemplateSection);
            btn.setOnClickListener(view -> {
                sectionAccess.delete(section);
                sections.remove(section);
                notifyDataSetChanged();
            });
        }
        return convertView;
    }
}
