package programmazionemobile.esercizi.personalcodex.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.DialogFragment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

import programmazionemobile.esercizi.personalcodex.Database.Entities.FD01_CAMPAIGNS;
import programmazionemobile.esercizi.personalcodex.Helpers.WifiDirectBroadcastReceiver;
import programmazionemobile.esercizi.personalcodex.R;

public class DialogDevicesServer extends DialogFragment {
    private IntentFilter intentFilter;
    private WifiDirectBroadcastReceiver receiver;
    private final Activity activity;

    public DialogDevicesServer(Activity activity) {
        this.activity = activity;
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup UI
        View view = inflater.inflate(R.layout.dialog_devices_server, container, false);

        //setup IntentFilter
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        //cancel button
        view.findViewById(R.id.btnAnnullaServer).setOnClickListener(v -> this.dismiss());

        WifiP2pManager.ConnectionInfoListener connectionInfoListener = info -> {
            if (info.groupFormed) {
                new Thread() {
                    @Override
                    public void run() {
                        try (ServerSocket serverSocket = new ServerSocket(8888)) {
                            Socket socket = serverSocket.accept();
                            InputStream stream = socket.getInputStream();

                            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                            byte[] data = new byte[1024];
                            int bytes;
                            while ((bytes = stream.read(data)) != -1)
                                buffer.write(data, 0, bytes);

                            byte[] received = buffer.toByteArray();
                            ByteArrayInputStream bais = new ByteArrayInputStream(received);
                            ObjectInputStream ois = new ObjectInputStream(bais);
                            FD01_CAMPAIGNS campaign = (FD01_CAMPAIGNS) ois.readObject();

                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
            }
        };

        //setup wifidirect classes
        WifiP2pManager manager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
        WifiP2pManager.Channel channel = manager.initialize(activity, activity.getMainLooper(), null);
        receiver = new WifiDirectBroadcastReceiver(null, connectionInfoListener, manager, channel);

        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                view.findViewById(R.id.txtErrorMessageServer).setVisibility(View.GONE);
                view.findViewById(R.id.pbpReceive).setVisibility(View.VISIBLE);
                view.findViewById(R.id.txtReceive).setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int reason) {
                view.findViewById(R.id.txtErrorMessageServer).setVisibility(View.VISIBLE);
                view.findViewById(R.id.pbpReceive).setVisibility(View.GONE);
                view.findViewById(R.id.txtReceive).setVisibility(View.GONE);
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
