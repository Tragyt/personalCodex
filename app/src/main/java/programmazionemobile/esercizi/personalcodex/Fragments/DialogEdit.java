package programmazionemobile.esercizi.personalcodex.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import programmazionemobile.esercizi.personalcodex.R;

public class DialogEdit extends DialogFragment {

    private final String text;
    private final View.OnClickListener clickListener;
    private final View.OnClickListener deleteClickListener;
    private View view;

    public DialogEdit(String text, View.OnClickListener clickListener) {
        this.text = text;
        this.clickListener = clickListener;
        this.deleteClickListener = null;
    }

    public DialogEdit(String text, View.OnClickListener clickListener, View.OnClickListener deleteClickListener) {
        this.text = text;
        this.clickListener = clickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_edit, container, false);
        EditText txt = view.findViewById(R.id.txtDialog);
        txt.setText(text);
        view.findViewById(R.id.btnConfirmDialog).setOnClickListener(clickListener);
        view.findViewById(R.id.btnCancelDialog).setOnClickListener(v -> dismiss());

        Button btnDelete = view.findViewById(R.id.btnDeleteDialog);
        if (deleteClickListener == null)
            view.findViewById(R.id.btnDeleteDialog).setVisibility(View.GONE);
        else btnDelete.setOnClickListener(deleteClickListener);
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

    public String getText() {
        return ((TextView) view.findViewById(R.id.txtDialog)).getText().toString();
    }
}
