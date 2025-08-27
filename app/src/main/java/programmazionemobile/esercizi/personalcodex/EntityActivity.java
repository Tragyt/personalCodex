package programmazionemobile.esercizi.personalcodex;

import android.app.Activity;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Adapters.BondsAdapter;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.BondsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignSectionsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.EntitiesAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.SectionAccess;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.BondsDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsSectionsDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.EntitiesDAO;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.SectionsDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD02_CAMPAIGNS_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD04_BONDS;
import programmazionemobile.esercizi.personalcodex.Database.MyDatabase;
import programmazionemobile.esercizi.personalcodex.Fragments.DialogEdit;
import programmazionemobile.esercizi.personalcodex.Fragments.DialogImage;
import programmazionemobile.esercizi.personalcodex.Helpers.CampaignsHelper;

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

            //LINKS
            BondsDAO bondsDAO = db.bondsDAO();
            BondsAccess bondsAccess = new BondsAccess(bondsDAO);

            ArrayList<FD04_BONDS> bonds = bondsAccess.getAll(entity.ID);
            RecyclerView rcvBonds = findViewById(R.id.rcvBonds);
            BondsAdapter bondsAdapter = new BondsAdapter(bondsAccess,bonds,entitiesAccess,entity.ID);
            rcvBonds.setLayoutManager(new LinearLayoutManager(this));
            rcvBonds.setAdapter(bondsAdapter);

            //creazione links
            ActivityResultLauncher<Intent> newLinkLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if(data!=null){
                                FD03_ENTITIES lnkEntity = (FD03_ENTITIES) data.getSerializableExtra("entity");
                                if(lnkEntity!=null){
                                    FD04_BONDS lnk = new FD04_BONDS(entity.ID, lnkEntity.ID);
                                    bondsAccess.insert(lnk);
                                    //aggiornare adapter
                                }
                            }
                        }
                    }
            );

            CampaignsSectionsDAO sectionsDAO = db.campaignsSectionsDAO();
            CampaignSectionsAccess sectionAccess = new CampaignSectionsAccess(sectionsDAO);
            FD02_CAMPAIGNS_SECTIONS section = sectionAccess.get(entity.FD03_SECTION_FD02);

            CampaignsDAO campaignsDAO = db.campaignsDAO();
            CampaignsAccess campaignsAccess = new CampaignsAccess(campaignsDAO);
            FD01_CAMPAIGNS campaign = campaignsAccess.get(section.FD02_CAMPAIGN_FD01);

            findViewById(R.id.btnAddLink).setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), CampaignActivity.class);
                intent.putExtra("campaign", campaign);
                intent.putExtra("role", CampaignsHelper.CampaignRole.NEW_LINK);
                intent.putExtra("entity", entity);
                newLinkLauncher.launch(intent);
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
