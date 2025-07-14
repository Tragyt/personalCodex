package programmazionemobile.esercizi.personalcodex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import programmazionemobile.esercizi.personalcodex.Adapters.CampaignsAdapter;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.Database.MyDatabase;
import programmazionemobile.esercizi.personalcodex.Helpers.TemplatesHelper.TemplatesRoles;

public class MainActivity extends AppCompatActivity {

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
                    context.startActivity(i);
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

        MyDatabase db = MyDatabase.getInstance(context);
        CampaignsDAO dao = db.campaignsDAO();
        FutureTask<List<FD01_CAMPAIGNS>> task = new FutureTask<>(dao::getAll);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
        FD01_CAMPAIGNS[] array;
        try {
            array = task.get().toArray(new FD01_CAMPAIGNS[0]);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        CampaignsAdapter adapter = new CampaignsAdapter(array);
        RecyclerView recyclerView = findViewById(R.id.rcvCampaigns);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}