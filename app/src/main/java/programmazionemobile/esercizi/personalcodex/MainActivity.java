package programmazionemobile.esercizi.personalcodex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Adapters.CampaignsAdapter;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignsAccess;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.Database.MyDatabase;
import programmazionemobile.esercizi.personalcodex.Helpers.TemplatesHelper.TemplatesRoles;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> CaricaCampaigns()
        );

        Context context = this;
        ImageButton btnNewCampaign = findViewById(R.id.btnNewCampaign);
        btnNewCampaign.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.new_element, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                int item = menuItem.getItemId();
                if (item == R.id.itmNew) {
                    Intent i = new Intent(context, TemplatesActivity.class);
                    i.putExtra("Role", TemplatesRoles.NEW_CAMPAIGN);
                    activityResultLauncher.launch(i);
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });

        findViewById(R.id.btnSettings).setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.getMenuInflater().inflate(R.menu.settings, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                int item = menuItem.getItemId();
                if (item == R.id.optTemplates) {
                    Intent i = new Intent(context, TemplatesActivity.class);
                    i.putExtra("Role", TemplatesRoles.OPTIONS);
                    context.startActivity(i);
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });

        CaricaCampaigns();
    }

    private void CaricaCampaigns(){
        MyDatabase db = MyDatabase.getInstance(this);
        CampaignsDAO dao = db.campaignsDAO();
        CampaignsAccess access = new CampaignsAccess(dao);
        ArrayList<FD01_CAMPAIGNS> array = access.getAll();
        CampaignsAdapter adapter = new CampaignsAdapter(array, access, activityResultLauncher);
        RecyclerView recyclerView = findViewById(R.id.rcvCampaigns);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}