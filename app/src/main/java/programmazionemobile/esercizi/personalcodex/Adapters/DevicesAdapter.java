package programmazionemobile.esercizi.personalcodex.Adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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
        devices.clear();
        devices.addAll(lst);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DevicesAdapter.ViewHolder(view);
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WifiP2pDevice device = devices.get(position);
        TextView txt = holder.getTxtDevice();
        txt.setText(device.deviceName);

        LinearLayout ll = holder.getLlDevice();
        ll.setOnClickListener(v -> {
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;
            config.wps.setup = WpsInfo.PBC;

            manager.connect(channel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.d("WIFIDIRECT","successo");
                }

                @Override
                public void onFailure(int reason) {
                    new androidx.appcompat.app.AlertDialog.Builder(activity)
                            .setTitle(R.string.icError_description)
                            .setMessage(R.string.txtConnectionError)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                            .show();
                }
            });
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
