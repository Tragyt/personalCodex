package programmazionemobile.esercizi.personalcodex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import programmazionemobile.esercizi.personalcodex.Adapters.TemplatesAdapter;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.TemplateAccess;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.TemplatesDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.TP01_TEMPLATES;
import programmazionemobile.esercizi.personalcodex.Database.MyDatabase;
import programmazionemobile.esercizi.personalcodex.Helpers.TemplatesHelper.TemplatesRoles;

public class TemplatesActivity extends AppCompatActivity {

    private TemplateAccess templateAccess;
    private TemplatesRoles role;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_templates);

        Intent i = getIntent();
        role = (TemplatesRoles) i.getSerializableExtra("Role");
        if (role == TemplatesRoles.NEW_CAMPAIGN)
            SetupNewCampaign();
        if (role == TemplatesRoles.OPTIONS)
            SetupOptions();

        CaricaTemplates();

        findViewById(R.id.btnBackTemplates).setOnClickListener(view -> super.finish());
    }

    private void CaricaTemplates() {
        MyDatabase db = MyDatabase.getInstance(this);
        TemplatesDAO dao = db.templatesDAO();
        templateAccess = new TemplateAccess(dao);
        List<TP01_TEMPLATES> lst = templateAccess.getAll();
        if (role == TemplatesRoles.NEW_CAMPAIGN)
            lst.add(new TP01_TEMPLATES("Empty", -1));
        TP01_TEMPLATES[] array = lst.toArray(new TP01_TEMPLATES[0]);

        TemplatesAdapter adapter = new TemplatesAdapter(array, activityResultLauncher, role);
        RecyclerView recyclerView = findViewById(R.id.rcvTemplates);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void SetupNewCampaign() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> finish()
        );

        findViewById(R.id.txtTitleTemplates).setVisibility(TextView.VISIBLE);
        findViewById(R.id.btnNewTemplate).setVisibility(Button.INVISIBLE);
    }

    private void SetupOptions() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        CaricaTemplates();
                    }
                }
        );

        findViewById(R.id.txtTitleTemplates).setVisibility(TextView.INVISIBLE);

        Context context = this;
        ImageButton btnNew = findViewById(R.id.btnNewTemplate);
        btnNew.setVisibility(Button.VISIBLE);
        btnNew.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.new_element, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                int item = menuItem.getItemId();
                if (item == R.id.itmNew) {
                    TP01_TEMPLATES template = new TP01_TEMPLATES(getResources().getString(R.string.title_template));
                    template.ID = templateAccess.insert(template);
                    Intent i = new Intent(context, TemplateActivity.class);
                    i.putExtra("template", template);
                    activityResultLauncher.launch(i);
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });
    }

}
