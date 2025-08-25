package programmazionemobile.esercizi.personalcodex;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.EntitiesAccess;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.EntitiesDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.Database.MyDatabase;
import programmazionemobile.esercizi.personalcodex.Fragments.DialogEdit;
import programmazionemobile.esercizi.personalcodex.Fragments.DialogImage;

public class EntityActivity extends AppCompatActivity {
    private FD03_ENTITIES entity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        entity = (FD03_ENTITIES) i.getSerializableExtra("entity");
        if (entity != null) {
            //setup screen
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_entity);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clEntity), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            MyDatabase db = MyDatabase.getInstance(this);
            EntitiesDAO entitiesDAO = db.entitiesDAO();
            EntitiesAccess entitiesAccess = new EntitiesAccess(entitiesDAO);

            TextView txtTitle = findViewById(R.id.txtEntityTitle);
            txtTitle.setText(entity.FD03_NAME);

            //change title dialog
            View.OnClickListener titleDialogClick = v -> {
                DialogEdit dialog = (DialogEdit) getSupportFragmentManager().findFragmentByTag("titleDialog");
                if (dialog != null) {
                    entity.FD03_NAME = dialog.getText();
                    entitiesAccess.update(entity);
                    txtTitle.setText(entity.FD03_NAME);
                    dialog.dismiss();
                }
            };
            DialogEdit titleDialog = new DialogEdit(entity.FD03_NAME, titleDialogClick);
            txtTitle.setOnLongClickListener(view -> {
                titleDialog.show(getSupportFragmentManager(), "titleDialog");
                return true;
            });

            //image
            ImageView img = findViewById(R.id.imgEntity);
            if (entity.FD03_IMAGE == null)  //se nessuna immagine inserita creo immagine di default
                img.post(() -> setDefaultImage(img));
            else {
                Bitmap bitmap = BitmapFactory.decodeFile(entity.FD03_IMAGE);
                img.setImageBitmap(bitmap);
            }

            //gestione cambio immagine
            ActivityResultLauncher<String> selectImgLauncher = registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            try {
                                InputStream inStream = getContentResolver().openInputStream(uri);
                                File file = new File(getFilesDir(), entity.ID + ".jpg");
                                FileOutputStream outStream = new FileOutputStream(file);
                                if (inStream != null) {
                                    byte[] buffer = new byte[1024];
                                    int len;
                                    while ((len = inStream.read(buffer)) > 0) {
                                        outStream.write(buffer, 0, len);
                                    }
                                    inStream.close();

                                    entity.FD03_IMAGE = file.getAbsolutePath();
                                    entitiesAccess.update(entity);

                                    Bitmap bitmap = BitmapFactory.decodeFile(entity.FD03_IMAGE);
                                    img.setImageBitmap(bitmap);
                                }
                                outStream.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
            );

            View.OnClickListener selectImgClickListener = view -> {
                DialogImage fragment = (DialogImage) getSupportFragmentManager().findFragmentByTag("imgDialog");
                if (fragment != null) {
                    selectImgLauncher.launch("image/*");
                    fragment.dismiss();
                }
            };
            View.OnClickListener resetImgClickListener = view -> {
                DialogImage fragment = (DialogImage) getSupportFragmentManager().findFragmentByTag("imgDialog");
                if (fragment != null) {
                    entity.FD03_IMAGE = null;
                    entitiesAccess.update(entity);
                    setDefaultImage(img);
                    fragment.dismiss();
                }
            };
            img.setOnClickListener(view -> {
                DialogImage dialog = new DialogImage(selectImgClickListener, resetImgClickListener);
                dialog.show(getSupportFragmentManager(), "imgDialog");
            });

            //testo non editabile
            TextView txtDescription = findViewById(R.id.txtEntityDescription);
            txtDescription.setText(entity.FD03_DESCRIPTION);

            //testo editabile nascosto
            EditText txtDescriptionEdit = findViewById(R.id.txtEntityDescriptionEdit);
            txtDescriptionEdit.setText(entity.FD03_DESCRIPTION);
            txtDescriptionEdit.setVisibility(View.GONE);

            ImageButton btnSaveDescription = findViewById(R.id.btnSaveDescription);
            btnSaveDescription.setVisibility(View.GONE);
            ImageButton btnEditDescription = findViewById(R.id.btnEditDescription);

            //premendo modifica abilito testo editanbile
            btnEditDescription.setOnClickListener(view -> {
                txtDescription.setVisibility(View.GONE);
                txtDescriptionEdit.setVisibility(View.VISIBLE);
                btnEditDescription.setVisibility(View.GONE);
                btnSaveDescription.setVisibility(View.VISIBLE);
            });

            btnSaveDescription.setOnClickListener(view -> {
                entity.FD03_DESCRIPTION = txtDescriptionEdit.getText().toString();
                entitiesAccess.update(entity);
                txtDescription.setText(entity.FD03_DESCRIPTION);

                txtDescriptionEdit.setVisibility(View.GONE);
                btnSaveDescription.setVisibility(View.GONE);
                txtDescription.setVisibility(View.VISIBLE);
                btnEditDescription.setVisibility(View.VISIBLE);
            });

            findViewById(R.id.btnBackEntity).setOnClickListener(view -> finish());
        }

    }

    private void setDefaultImage(ImageView view) {
        int width = view.getWidth();
        int height = view.getHeight();

        //background giallo
        Paint background = new Paint();
        background.setAntiAlias(true);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.setColor(getColor(R.color.yellow));
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), background);

        //disegno lettera
        Paint pLetter = new Paint();
        pLetter.setColor(getColor(R.color.black));
        pLetter.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        pLetter.setTextSize(120);
        pLetter.setTextAlign(Paint.Align.CENTER);

        /*
         * ascent → distanza (negativa) dalla baseline al punto più alto del carattere.
         *
         * descent → distanza (positiva) dalla baseline al punto più basso.
         */
        Paint.FontMetrics fontMetrics = pLetter.getFontMetrics();
        float x = width / 2f;
        float y = height / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2f;

        String letter = entity.FD03_NAME.substring(0, 1).toUpperCase();
        canvas.drawText(letter, x, y, pLetter);
        view.setImageBitmap(bitmap);
    }
}
