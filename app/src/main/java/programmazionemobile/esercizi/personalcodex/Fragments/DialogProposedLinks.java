package programmazionemobile.esercizi.personalcodex.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

import programmazionemobile.esercizi.personalcodex.Adapters.ProposedLinksAdapter;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.BondsAccess;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD04_BONDS;
import programmazionemobile.esercizi.personalcodex.R;

public class DialogProposedLinks extends DialogFragment {

    private final ArrayList<FD03_ENTITIES> entities;
    private final long idEntity;
    private final BondsAccess bondsAccess;
    private final Consumer<FD04_BONDS> onClickedItem;

    public DialogProposedLinks(ArrayList<FD03_ENTITIES> entities, long idEntity, BondsAccess bondsAccess, Consumer<FD04_BONDS> onClickedItem) {
        this.entities = entities;
        this.idEntity = idEntity;
        this.bondsAccess = bondsAccess;
        this.onClickedItem = onClickedItem;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_proposed_links, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rcvProposedLinks);
        ProposedLinksAdapter adapter = new ProposedLinksAdapter(entities,idEntity,bondsAccess,onClickedItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

    }
}
