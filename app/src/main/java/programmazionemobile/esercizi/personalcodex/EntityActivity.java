package programmazionemobile.esercizi.personalcodex;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.EntitiesAccess;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.EntitiesDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD03_ENTITIES;
import programmazionemobile.esercizi.personalcodex.Database.MyDatabase;
import programmazionemobile.esercizi.personalcodex.Fragments.DialogEditText;

public class EntityActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        FD03_ENTITIES entity = (FD03_ENTITIES) i.getSerializableExtra("entity");
        if (entity != null) {
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

            View.OnClickListener titleDialogClick = v -> {
                DialogEditText dialog = (DialogEditText) getSupportFragmentManager().findFragmentByTag("titleDialog");
                if (dialog != null) {
                    entity.FD03_NAME = dialog.getText();
                    entitiesAccess.update(entity);
                    txtTitle.setText(entity.FD03_NAME);
                    dialog.dismiss();
                }
            };
            DialogEditText titleDialog = new DialogEditText(entity.FD03_NAME, titleDialogClick);
            txtTitle.setOnLongClickListener(view -> {
                titleDialog.show(getSupportFragmentManager(),"titleDialog");
                return true;
            });
        }
    }
}
