package programmazionemobile.esercizi.personalcodex.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import programmazionemobile.esercizi.personalcodex.R;

public class DialogConfirmDelete extends DialogFragment {

    private final String text;
    private final View.OnClickListener clickListener;
    private Object element;
    private View view;

    public DialogConfirmDelete(String text, View.OnClickListener clickListener, Object element) {
        this.text = text;
        this.clickListener = clickListener;
        this.element = element;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_edit_text, container, false);
        EditText txt = view.findViewById(R.id.txtDialog);
        txt.setText(text);
        view.findViewById(R.id.btnConfirmDialog).setOnClickListener(clickListener);
        view.findViewById(R.id.btnCancelDialog).setOnClickListener(v -> dismiss());

        if(!editable)
        {
            txt.setFocusable(false);
            txt.setFocusableInTouchMode(false);
            txt.setClickable(false);
            txt.setLongClickable(false);
            txt.setCursorVisible(false);
            txt.setKeyListener(null);
            txt.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null){
            Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

    }

    public String getText() {
        return ((TextView) view.findViewById(R.id.txtDialog)).getText().toString();
    }
}
