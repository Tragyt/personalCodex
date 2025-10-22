package programmazionemobile.esercizi.personalcodex.Helpers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.R;

public class PermissionsHelper {

    public static void WifiDirectPermissions(Activity activity, ActivityResultLauncher<String[]> launcher, Runnable runnable) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED)
                permissionsToRequest.add(Manifest.permission.NEARBY_WIFI_DEVICES);
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionsToRequest.isEmpty()) {
            runnable.run();
            return;
        }
        if (permissionsToRequest.stream().anyMatch(perm -> ActivityCompat.shouldShowRequestPermissionRationale(activity, perm)))
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.txtPermissionsTitle)
                    .setMessage(R.string.txtPermissionsWifi)
                    .setPositiveButton(R.string.btnConfirm_dialog, (dialog, which) ->
                            launcher.launch(permissionsToRequest.toArray(new String[0]))
                    )
                    .setNegativeButton(R.string.btnCancel_dialog, null)
                    .show();
        else
            launcher.launch(permissionsToRequest.toArray(new String[0]));
    }

    public static void PermissionsDenied(Context context) {
        new androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle(R.string.txtPermissionsTitle)
                .setMessage(R.string.txtPermissionsWifi)
                .setPositiveButton(R.string.btnSettings_description, (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivity(intent);
                })
                .setNegativeButton(R.string.btnCancel_dialog, null)
                .show();
    }

}
