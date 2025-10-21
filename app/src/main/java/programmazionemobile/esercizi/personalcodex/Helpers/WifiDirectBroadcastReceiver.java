package programmazionemobile.esercizi.personalcodex.Helpers;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import androidx.annotation.RequiresPermission;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
    private final WifiP2pManager.PeerListListener peerListListener;
    private final WifiP2pManager manager;
    private final WifiP2pManager.Channel channel;

    public WifiDirectBroadcastReceiver(WifiP2pManager.PeerListListener peerListListener, WifiP2pManager manager, WifiP2pManager.Channel channel) {
        this.peerListListener = peerListListener;
        this.manager = manager;
        this.channel = channel;
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES})
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (manager != null && peerListListener != null){
                Log.i("WIFIDIRECT", "PEERS CHANGED");
                manager.requestPeers(channel, peerListListener);
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

        }
    }
}
