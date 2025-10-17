package programmazionemobile.esercizi.personalcodex.Helpers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import programmazionemobile.esercizi.personalcodex.R;

public class PermissionsHelper {

    public static int NEARBY_WIFI_DEVICES_CODE = 1;

    public static void ManageWifiDirectPermissions(Activity activity, ActivityResultLauncher<String> launcher) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
//                && ContextCompat.checkSelfPermission(activity, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.NEARBY_WIFI_DEVICES)) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//                builder.setMessage(R.string.txtPermissionsWifi);
//                builder.setTitle(R.string.txtPermissionsTitle)
//                        .setCancelable(false)
//                        .setPositiveButton(R.string.btnConfirm_dialog, (dialog, which) -> {
//                            ActivityCompat.requestPermissions(activity,
//                                    new String[]{Manifest.permission.NEARBY_WIFI_DEVICES},
//                                    NEARBY_WIFI_DEVICES_CODE);
//                            dialog.dismiss();
//                        });
//                builder.setNegativeButton(R.string.btnCancel_dialog, ((dialog, which) -> {
//                    dialog.dismiss();
//                }));
//                builder.show();
//
//            } else {
//                ActivityCompat.requestPermissions(activity,
//                        new String[]{Manifest.permission.NEARBY_WIFI_DEVICES},
//                        NEARBY_WIFI_DEVICES_CODE);
//            }
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_GRANTED)
                Log.d("PERMISSIONS", "Permission granted");
            else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.NEARBY_WIFI_DEVICES)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage(R.string.txtPermissionsWifi);
                builder.setTitle(R.string.txtPermissionsTitle)
                        .setCancelable(false)
                        .setPositiveButton(R.string.btnConfirm_dialog, (dialog, which) -> {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.NEARBY_WIFI_DEVICES},
                                    NEARBY_WIFI_DEVICES_CODE);
                            dialog.dismiss();
                            Log.d("PERMISSIONS", "Permission granted");
                        });
                builder.setNegativeButton(R.string.btnCancel_dialog, ((dialog, which) -> {
                    dialog.dismiss();
                    Log.d("PERMISSIONS", "Permission not granted");
                }));
                builder.show();
            } else{
                Log.d("PERMISSIONS", "Permission granted");
                launcher.launch(Manifest.permission.NEARBY_WIFI_DEVICES);
            }
        }
    }

}
