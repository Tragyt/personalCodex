package programmazionemobile.esercizi.personalcodex;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Adapters.TemplateSectionsAdapter;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.SectionAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.TemplateAccess;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.SectionsDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.TP01_TEMPLATES;
import programmazionemobile.esercizi.personalcodex.Database.Entities.TP02_SECTIONS;
import programmazionemobile.esercizi.personalcodex.Database.MyDatabase;

public class TemplateActivity extends AppCompatActivity {

    private SectionAccess sectionAccess;
    private TemplateSectionsAdapter adapter;
    private ArrayList<TP02_SECTIONS> sections;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_template);

        MyDatabase db = MyDatabase.getInstance(this);
        SectionsDAO dao = db.sectionsDAO();
        Intent i = getIntent();
        TP01_TEMPLATES template = (TP01_TEMPLATES) i.getSerializableExtra("template");
        if (template != null) {
            TemplateAccess templateAccess = new TemplateAccess(db.templatesDAO());

            sectionAccess = new SectionAccess(dao);
            sections = sectionAccess.getAll(template.ID);
            adapter = new TemplateSectionsAdapter(this, sections, sectionAccess);
            ListView lstTemplateSections = findViewById(R.id.lstTemplateSections);
            lstTemplateSections.setAdapter(adapter);

            View footerButton = getLayoutInflater().inflate(R.layout.footer_button_add, null);
            lstTemplateSections.addFooterView(footerButton);
            footerButton.findViewById(R.id.btnAddSection).setOnClickListener(view -> {
                TP02_SECTIONS section = new TP02_SECTIONS(getString(R.string.new_section), template.ID);
                runOnUiThread(() -> {
                    sections.clear();
                    sections.addAll(sectionAccess.insertAndGetAll(section));
                    adapter.notifyDataSetChanged();
                });
            });

            findViewById(R.id.btnDeleteTemplate).setOnClickListener(view -> {
                templateAccess.delete(template);
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
