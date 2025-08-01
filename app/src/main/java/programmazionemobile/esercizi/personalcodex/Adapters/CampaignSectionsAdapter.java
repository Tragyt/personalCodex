package programmazionemobile.esercizi.personalcodex.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.R;

public class CampaignSectionsAdapter extends BaseExpandableListAdapter {

    private final Map<FD02_CAMPAIGNS_SECTIONS, ArrayList<FD03_ENTITIES>> sectionsEntities;
    private final ArrayList<FD02_CAMPAIGNS_SECTIONS> sections;

    public CampaignSectionsAdapter(Map<FD02_CAMPAIGNS_SECTIONS, ArrayList<FD03_ENTITIES>> sectionsEntities) {
        this.sectionsEntities = sectionsEntities;
        this.sections = new ArrayList<>(sectionsEntities.keySet());
    }

    @Override
    public int getGroupCount() {
        return sectionsEntities.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return Objects.requireNonNull(sectionsEntities.get(sections.get(i))).size();
    }

    @Override
    public Object getGroup(int i) {
        return sections.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return Objects.requireNonNull(sectionsEntities.get(sections.get(i))).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return sections.get(i).ID;
    }

    @Override
    public long getChildId(int i, int i1) {
        return Objects.requireNonNull(sectionsEntities.get(sections.get(i))).get(i1).ID;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_campaign_section, viewGroup, false);
        }

        FD02_CAMPAIGNS_SECTIONS section = sections.get(i);
        TextView txt = view.findViewById(R.id.txtCampaignSection);
        txt.setTypeface(null, Typeface.BOLD);
        txt.setText(section.FD02_NAME);

        ImageButton btn = view.findViewById(R.id.btnAddEntity);
        btn.setFocusable(false);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_campaign_entity, viewGroup, false);
        }

        TextView txt = view.findViewById(R.id.txtCampaignEntity);
        txt.setText(Objects.requireNonNull(sectionsEntities.get(sections.get(i))).get(i1).FD03_NAME);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
