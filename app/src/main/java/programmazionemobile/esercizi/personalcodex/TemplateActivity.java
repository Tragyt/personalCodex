package programmazionemobile.esercizi.personalcodex;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Adapters.TemplateSectionsAdapter;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.SectionAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.TemplateAccess;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.SectionsDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.TP01_TEMPLATES;
import programmazionemobile.esercizi.personalcodex.Database.Entities.TP02_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.MyDatabase;

public class TemplateActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        TP01_TEMPLATES template = (TP01_TEMPLATES) i.getSerializableExtra("template");
        if (template != null) {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_template);

            MyDatabase db = MyDatabase.getInstance(this);
            SectionsDAO dao = db.sectionsDAO();
            TemplateAccess templateAccess = new TemplateAccess(db.templatesDAO());

            SectionAccess sectionAccess = new SectionAccess(dao);
            ArrayList<TP02_SECTIONS> sections = sectionAccess.getAll(template.ID);
            TemplateSectionsAdapter adapter = new TemplateSectionsAdapter(sections, sectionAccess, template.ID);
            RecyclerView lstTemplateSections = findViewById(R.id.lstTemplateSections);
            lstTemplateSections.setLayoutManager(new LinearLayoutManager(this));
            lstTemplateSections.setAdapter(adapter);

            findViewById(R.id.btnDeleteTemplate).setOnClickListener(view -> {
                templateAccess.delete(template.ID);
                setResult(RESULT_OK);
                finish();
            });
            findViewById(R.id.btnBackTemplate).setOnClickListener(view -> {
                setResult(RESULT_OK);
                finish();
            });

            EditText editTemplatename = findViewById(R.id.editTemplatename);
            editTemplatename.setText(template.TP01_NAME);
            editTemplatename.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    template.TP01_NAME = editable.toString();
                    templateAccess.update(template);
                }
            });
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
