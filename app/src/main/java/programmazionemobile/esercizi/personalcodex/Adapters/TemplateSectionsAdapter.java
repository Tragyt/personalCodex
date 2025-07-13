package programmazionemobile.esercizi.personalcodex.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import programmazionemobile.esercizi.personalcodex.Database.DAOs.SectionsDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.TP02_SECTIONS;
import programmazionemobile.esercizi.personalcodex.R;
import programmazionemobile.esercizi.personalcodex.TemplateActivity;

public class TemplateSectionsAdapter extends ArrayAdapter {
    ArrayList<TP02_SECTIONS> sections;
    Context context;
    public TemplateSectionsAdapter(Context context, ArrayList<TP02_SECTIONS> sections) {
        super(context, R.layout.item_templates_section, sections);
        this.sections = sections;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_templates_section,null);

            TextView txt = convertView.findViewById(R.id.txtTemplateSection);
            txt.setText(sections.get(position).TP02_NAME);

            ImageView btn = convertView.findViewById(R.id.btnRemoveTemplateSection);
            btn.setOnClickListener(view -> {
                TemplateActivity.deleteSection(sections.get(position));
            });
        }
        return convertView;
    }
}
