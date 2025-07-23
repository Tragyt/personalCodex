package programmazionemobile.esercizi.personalcodex.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CampaignSectionsAdapter extends BaseExpandableListAdapter {

    private final Map<String, ArrayList<String>> sectionsEntities;
    private ArrayList<String> sections;

    public CampaignSectionsAdapter(Map<String, ArrayList<String>> sectionsEntities) {
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
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
