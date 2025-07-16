package programmazionemobile.esercizi.personalcodex.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.Map;

public class CampaignSectionsAdapter extends BaseExpandableListAdapter {

    private final Map<String, ArrayList<String>> sectionsEntities;

    public CampaignSectionsAdapter(Map<String, ArrayList<String>> sectionsEntities) {
        this.sectionsEntities = sectionsEntities;
    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int i) {
        return 0;
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
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
