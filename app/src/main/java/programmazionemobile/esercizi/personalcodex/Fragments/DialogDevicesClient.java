package programmazionemobile.esercizi.personalcodex.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import programmazionemobile.esercizi.personalcodex.Adapters.DevicesAdapter;
import programmazionemobile.esercizi.personalcodex.Helpers.WifiDirectBroadcastReceiver;
import programmazionemobile.esercizi.personalcodex.R;

public class DialogDevicesClient extends DialogFragment {
    private IntentFilter intentFilter;
    private WifiDirectBroadcastReceiver receiver;
    private final ArrayList<WifiP2pDevice> devices = new ArrayList<>();
    private DevicesAdapter adapter;

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup UI
        View view = inflater.inflate(R.layout.dialog_devices_client, container, false);
        RecyclerView rcv = view.findViewById(R.id.rcvDevices);
        adapter = new DevicesAdapter(devices);
        rcv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rcv.setAdapter(adapter);

        //cancel button
        view.findViewById(R.id.btnAnnullaClient).setOnClickListener(v -> this.dismiss());

        //setup IntentFilter
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        //setup wifidirect classes
        Context context = requireContext();
        WifiP2pManager manager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        WifiP2pManager.Channel channel = manager.initialize(context, context.getMainLooper(), null);

        //listener chiamato da broadcastreceiver quando cambiano i peers
        WifiP2pManager.PeerListListener peerListListener = peers -> {
            Collection<WifiP2pDevice> peerList = peers.getDeviceList();
            if (!peerList.equals(devices)) {
                devices.clear();
                devices.addAll(peerList);
                adapter.updateData(devices);
            }
        };
        receiver = new WifiDirectBroadcastReceiver(peerListListener, manager, channel);

        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.i("WIFIDIRECT", "Client Discover Start");
                view.findViewById(R.id.txtErrorMessageClient).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int reason) {
                Log.e("WIFIDIRECT","Client Discover failed " + reason);
                view.findViewById(R.id.txtErrorMessageClient).setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requireContext().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(receiver);
    }
}
