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

import java.util.Objects;

import programmazionemobile.esercizi.personalcodex.R;

public class DialogImage extends DialogFragment {

    private final View.OnClickListener selectImgClickListener;
    private final View.OnClickListener resetImgClickListener;

    public DialogImage(View.OnClickListener selectImgClickListener, View.OnClickListener resetImgClickListener) {
        this.selectImgClickListener = selectImgClickListener;
        this.resetImgClickListener = resetImgClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image, container, false);
        view.findViewById(R.id.btnSelectImg).setOnClickListener(selectImgClickListener);
        view.findViewById(R.id.btnResetImg).setOnClickListener(resetImgClickListener);
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
