package programmazionemobile.esercizi.personalcodex.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
    private final Activity activity;
    private final byte[] data;

    public DialogDevicesClient(Activity activity, byte[] data) {
        this.activity = activity;
        this.data = data;
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup UI
        View view = inflater.inflate(R.layout.dialog_devices_client, container, false);
        ProgressBar pb = view.findViewById(R.id.pbpSend);
        TextView txt = view.findViewById(R.id.txtSend);

        //cancel button
        view.findViewById(R.id.btnAnnullaClient).setOnClickListener(v -> this.dismiss());

        //setup IntentFilter
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        //setup wifidirect classes
        WifiP2pManager manager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
        WifiP2pManager.Channel channel = manager.initialize(activity, activity.getMainLooper(), null);

        //set data
        RecyclerView rcv = view.findViewById(R.id.rcvDevices);
        DevicesAdapter adapter = new DevicesAdapter(devices, manager, channel, activity);
        rcv.setLayoutManager(new LinearLayoutManager(activity));
        rcv.setAdapter(adapter);

        //listener chiamato da broadcastreceiver quando cambiano i peers
        WifiP2pManager.PeerListListener peerListListener = peers -> {
            Collection<WifiP2pDevice> peerList = peers.getDeviceList();
            if (!peerList.equals(devices)) {
                if (peerList.isEmpty()) {
                    rcv.setVisibility(View.GONE);
                    pb.setVisibility(View.VISIBLE);
                    txt.setVisibility(View.VISIBLE);
                } else {
                    rcv.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);
                    txt.setVisibility(View.GONE);
                }
                devices.clear();
                devices.addAll(peerList);
                adapter.updateData(new ArrayList<>(peerList));
            }
        };

        WifiP2pManager.ConnectionInfoListener connectionInfoListener = info -> {
            if (info.groupFormed) {
                Log.d("WIFIDIRECT", "Connessione client riuscita");
                new Thread(){
                    @Override
                    public void run() {
                        try (ServerSocket serverSocket = new ServerSocket(8888)) {
                            Socket socket = serverSocket.accept();
                            OutputStream outputStream = socket.getOutputStream();
                            outputStream.write(data);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.start();
            }
        };

        receiver = new WifiDirectBroadcastReceiver(peerListListener, connectionInfoListener, manager, channel);

        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                view.findViewById(R.id.txtErrorMessageClient).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int reason) {
                view.findViewById(R.id.txtErrorMessageClient).setVisibility(View.VISIBLE);
                rcv.setVisibility(View.GONE);
                pb.setVisibility(View.GONE);
                txt.setVisibility(View.GONE);
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

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                        ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_GRANTED)) {

            requireContext().registerReceiver(receiver, intentFilter);
        } else {
            Log.e("WIFIDIRECT", "Receiver non registrato: permessi mancanti");
        }
        requireContext().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(receiver);
    }
}
