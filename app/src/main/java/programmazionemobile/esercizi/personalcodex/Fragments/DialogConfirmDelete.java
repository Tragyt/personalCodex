package programmazionemobile.esercizi.personalcodex.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import programmazionemobile.esercizi.personalcodex.R;

public class DialogConfirmDelete extends DialogFragment {

    private final String text;
    private final View.OnClickListener confirmClickListener;
    private final View.OnClickListener cancelClickListener;

    public DialogConfirmDelete(String text, View.OnClickListener confirmClickListener) {
        this.text = text;
        this.confirmClickListener = confirmClickListener;
        cancelClickListener = null;
    }

    public DialogConfirmDelete(String text, View.OnClickListener confirmClickListener, View.OnClickListener cancelClickListener) {
        this.text = text;
        this.confirmClickListener = confirmClickListener;
        this.cancelClickListener = cancelClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_delete, container, false);
        TextView txt = view.findViewById(R.id.txtDialog);
        txt.setText(text);
        view.findViewById(R.id.btnConfirmDialog).setOnClickListener(confirmClickListener);

        Button btnCancel = view.findViewById(R.id.btnCancelDialog);
        if (cancelClickListener == null)
            btnCancel.setOnClickListener(v -> dismiss());
        else
            btnCancel.setOnClickListener(cancelClickListener);

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
