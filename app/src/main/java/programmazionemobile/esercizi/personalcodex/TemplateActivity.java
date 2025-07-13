package programmazionemobile.esercizi.personalcodex;

import android.content.Intent;
import android.os.Bundle;
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

    private static SectionAccess sectionAccess;
    private static TemplateSectionsAdapter adapter;
    private static ArrayList<TP02_SECTIONS> sections;
    private static ListView lstTemplateSections;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_template);

        MyDatabase db = MyDatabase.getInstance(this);
        SectionsDAO dao = db.sectionsDAO();
        Intent i = getIntent();
        TP01_TEMPLATES template = (TP01_TEMPLATES) i.getSerializableExtra("template");
        if(template != null){
            sectionAccess = new SectionAccess(dao);
            sections = sectionAccess.getAll(template.ID);
            adapter = new TemplateSectionsAdapter(this, sections);
            lstTemplateSections = findViewById(R.id.lstTemplateSections);
            lstTemplateSections.setAdapter(adapter);

            findViewById(R.id.btnDeleteTemplate).setOnClickListener(view -> {
                TemplateAccess templateAccess = new TemplateAccess(db.templatesDAO());
                templateAccess.delete(template);
                setResult(RESULT_OK);
                finish();
            });

            findViewById(R.id.btnBackTemplate).setOnClickListener(view -> {
                setResult(RESULT_OK);
                finish();
            });
        }
        else{
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    public static void deleteSection(TP02_SECTIONS section) {
        sectionAccess.delete(section);
        sections = sectionAccess.getAll(section.TP02_TEMPLATE_TP01);
        lstTemplateSections.setAdapter(adapter);
    }
}
