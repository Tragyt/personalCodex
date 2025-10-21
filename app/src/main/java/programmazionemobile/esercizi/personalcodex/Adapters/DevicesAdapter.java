package programmazionemobile.esercizi.personalcodex.Adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.Fragments.DialogDevicesServer;
import programmazionemobile.esercizi.personalcodex.Helpers.PermissionsHelper;
import programmazionemobile.esercizi.personalcodex.R;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {

    private final ArrayList<WifiP2pDevice> devices;
    private final WifiP2pManager manager;
    private final WifiP2pManager.Channel channel;
    private final Activity activity;

    public DevicesAdapter(ArrayList<WifiP2pDevice> devices, WifiP2pManager manager, WifiP2pManager.Channel channel, Activity activity) {
        this.devices = devices;
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(ArrayList<WifiP2pDevice> lst){
        Log.d("ADAPTER", "updateData chiamato con " + lst.size() + " elementi");
        for (WifiP2pDevice d : lst) Log.d("ADAPTER", " - " + d.deviceName);

        devices.clear();
        devices.addAll(lst);
        Log.d("ADAPTER", "Lista interna aggiornata: " + devices.size() + " elementi");
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DevicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WifiP2pDevice device = devices.get(position);
        TextView txt = holder.getTxtDevice();
        txt.setText(device.deviceName);

        LinearLayout ll = holder.getLlDevice();
        ll.setOnClickListener(v -> {
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;

//            manager.connect(channel, config, new WifiP2pManager.ActionListener() {
//                @Override
//                public void onSuccess() {
//
//                }
//
//                @Override
//                public void onFailure(int reason) {
//
//                }
//            });
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout llDevice;
        private final TextView txtDevice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llDevice = itemView.findViewById(R.id.llDevice);
            txtDevice = itemView.findViewById(R.id.txtDevice);
        }

        public LinearLayout getLlDevice() {
            return llDevice;
        }

        public TextView getTxtDevice() {
            return txtDevice;
        }
    }
}
