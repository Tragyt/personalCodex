package programmazionemobile.esercizi.personalcodex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Adapters.CampaignsAdapter;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignsAccess;
import programmazionemobile.esercizi.personalcodex.Database.DAOs.CampaignsDAO;
import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.Database.MyDatabase;
import programmazionemobile.esercizi.personalcodex.Fragments.DialogDevicesServer;
import programmazionemobile.esercizi.personalcodex.Helpers.TemplatesHelper.TemplatesRoles;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private FragmentManager fragmentManager = getSupportFragmentManager();

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

        //scarica modello se non presente
        Boolean debug = true;
        File tflite = new File(getFilesDir(), "gemma3-1b-it-int4.task");
        if (!tflite.exists() && !debug) {
            File tempFile = new File(getFilesDir(), "gemma3-1b-it-int4.task.tmp");
            LinearLayout ll = findViewById(R.id.llDownload);
            ll.setVisibility(View.VISIBLE);
            ProgressBar pb = findViewById(R.id.pbMain);

            String url_str = "https://huggingface.co/obaf/gemma3-1b-it-int4/resolve/main/gemma3-1b-it-int4.task?download=true";
            new Thread(() -> {
                try (FileOutputStream out = new FileOutputStream(tempFile)) {
                    URL url = new URL(url_str);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int fileLength = connection.getContentLength();
                    InputStream input = connection.getInputStream();

                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    long total = 0;

                    while ((bytesRead = input.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                        total += bytesRead;

                        int progress = (int) (total * 100 / fileLength);
                        runOnUiThread(() -> pb.setProgress(progress));
                    }

                    if (!tempFile.renameTo(tflite))
                        throw new IOException();
                    runOnUiThread(() -> ll.setVisibility(View.GONE));
                } catch (IOException ignored) {
                }
            }).start();
        }

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
                } else if (item == R.id.itmReceive) {
                    DialogDevicesServer serverDialog = new DialogDevicesServer();
                    serverDialog.show(fragmentManager, "serverDialog");
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

    private void CaricaCampaigns() {
        MyDatabase db = MyDatabase.getInstance(this);
        CampaignsDAO dao = db.campaignsDAO();
        CampaignsAccess access = new CampaignsAccess(dao);
        ArrayList<FD01_CAMPAIGNS> array = access.getAll();
        CampaignsAdapter adapter = new CampaignsAdapter(array, access, activityResultLauncher, fragmentManager);
        RecyclerView recyclerView = findViewById(R.id.rcvCampaigns);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}