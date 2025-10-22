package programmazionemobile.esercizi.personalcodex.Helpers;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import androidx.annotation.RequiresPermission;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
    private final WifiP2pManager.PeerListListener peerListListener;
    private final WifiP2pManager.ConnectionInfoListener connectionInfoListener;
    private final WifiP2pManager manager;
    private final WifiP2pManager.Channel channel;

    public WifiDirectBroadcastReceiver(WifiP2pManager.PeerListListener peerListListener, WifiP2pManager.ConnectionInfoListener connectionInfoListener, WifiP2pManager manager, WifiP2pManager.Channel channel) {
        this.peerListListener = peerListListener;
        this.connectionInfoListener = connectionInfoListener;
        this.manager = manager;
        this.channel = channel;
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (manager != null && peerListListener != null)
                manager.requestPeers(channel, peerListListener);


        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if (manager != null) {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null && networkInfo.isConnected())
                    manager.requestConnectionInfo(channel, connectionInfoListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

        }
    }
}
