package programmazionemobile.esercizi.personalcodex.Adapters;

import android.net.wifi.p2p.WifiP2pDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import programmazionemobile.esercizi.personalcodex.R;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {

    private final ArrayList<WifiP2pDevice> devices;

    public DevicesAdapter(ArrayList<WifiP2pDevice> devices) {
        this.devices = devices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new DevicesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

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
