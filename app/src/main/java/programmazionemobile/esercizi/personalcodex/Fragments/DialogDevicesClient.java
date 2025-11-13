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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.DialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.BondsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignSectionsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.CampaignsAccess;
import programmazionemobile.esercizi.personalcodex.Database.AsyncAccess.EntitiesAccess;
import programmazionemobile.esercizi.personalcodex.Helpers.SendReceiveHelper;
import programmazionemobile.esercizi.personalcodex.Helpers.WifiDirectBroadcastReceiver;
import programmazionemobile.esercizi.personalcodex.R;

public class DialogDevicesClient extends DialogFragment {
    private IntentFilter intentFilter;
    private WifiDirectBroadcastReceiver receiver;
    private final Activity activity;

    private final CampaignsAccess campaignsAccess;
    private final CampaignSectionsAccess campaignSectionsAccess;
    private final EntitiesAccess entitiesAccess;
    private final BondsAccess bondsAccess;

    public DialogDevicesClient(Activity activity, CampaignsAccess campaignsAccess, CampaignSectionsAccess campaignSectionsAccess, EntitiesAccess entitiesAccess, BondsAccess bondsAccess) {
        this.activity = activity;
        this.campaignsAccess = campaignsAccess;
        this.campaignSectionsAccess = campaignSectionsAccess;
        this.entitiesAccess = entitiesAccess;
        this.bondsAccess = bondsAccess;
    }

    View view;
    TextView txtError;
    TextView txt;
    ProgressBar pb;

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup UI
        view = inflater.inflate(R.layout.dialog_devices_client, container, false);
        txtError = view.findViewById(R.id.txtErrorMessageServer);
        pb = view.findViewById(R.id.pbpReceive);
        txt = view.findViewById(R.id.txtReceive);

        txtError.setVisibility(View.GONE);

        //setup IntentFilter
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        //cancel button
        view.findViewById(R.id.btnAnnullaServer).setOnClickListener(v -> this.dismiss());

        //setup wifidirect classes
        WifiP2pManager manager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
        WifiP2pManager.Channel channel = manager.initialize(activity, activity.getMainLooper(), null);

        //callback a connessione instaurata
        WifiP2pManager.ConnectionInfoListener connectionInfoListener = info -> {
            if (info.groupFormed) {
                new Thread() {
                    @Override
                    public void run() {
                        if (!info.isGroupOwner) {
                            String address = info.groupOwnerAddress.getHostAddress();
                            Socket socket = null;

                            try {
                                socket = new Socket();
                                socket.connect(new InetSocketAddress(address, 8888), 10000);

                                InputStream stream = socket.getInputStream();
                                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                                byte[] data = new byte[1024];
                                int bytes;
                                while ((bytes = stream.read(data)) != -1)
                                    buffer.write(data, 0, bytes);

                                byte[] received = buffer.toByteArray();
                                SendReceiveHelper.ReceiveCampaign(received, campaignsAccess, campaignSectionsAccess, entitiesAccess, bondsAccess);
                            } catch (ClassNotFoundException | IOException e) {
                                activity.runOnUiThread(() -> {
                                    txtError.setText(R.string.txtTransferError);
                                    txtError.setVisibility(View.VISIBLE);
                                    txt.setVisibility(View.GONE);
                                    pb.setVisibility(View.GONE);
                                });
                                SendReceiveHelper.removeGroup(manager, channel);
                            }
                        }
                        SendReceiveHelper.removeGroup(manager, channel);
                    }
                }.start();
            }
        };

        //creazione receiver
        receiver = new WifiDirectBroadcastReceiver(null, connectionInfoListener, manager, channel);

        manager.stopPeerDiscovery(channel, new WifiP2pManager.ActionListener() {
            @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
            @Override
            public void onSuccess() {
                startDiscovery(manager, channel);
            }

            @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
            @Override
            public void onFailure(int reason) {
                startDiscovery(manager, channel);
            }
        });

        return view;
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
    private void startDiscovery(WifiP2pManager manager, WifiP2pManager.Channel channel) {
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                txtError.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                txt.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int reason) {
                txtError.setText(R.string.txtConnectionError);
                txtError.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                txt.setVisibility(View.GONE);
            }
        });
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
