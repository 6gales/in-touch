package tech.intouch.network

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import tech.intouch.network.data.User
import tech.intouch.network.tcp.communication_cases.ExchangeProfiles

class WiFiDirectBroadcastReceiver(
    val manager: WifiP2pManager?,
    val channel: WifiP2pManager.Channel?,
    val network: Network
) : BroadcastReceiver() {
    val TAG = this.javaClass.name
    val peers = mutableListOf<WifiP2pDevice>()
    private val peerListListener = PeerListListener(this);
    //var callback: Consumer<MutableList<User>>? = null;
    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // Determine if Wifi P2P mode is enabled or not, alert
                // the Activity.
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                //activity.isWifiP2pEnabled = state == WifiP2pManager.WIFI_P2P_STATE_ENABLED
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                while(ContextCompat.checkSelfPermission(
                        network.activity.applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED) {
                    //activity.textInfo.text = "no permissions ACCESS_FINE_LOCATION"
                    ActivityCompat.requestPermissions(network.activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        1);
                    //return;//TODO: нет permissions, запрос https://developer.android.com/training/permissions/requesting
                }
                //activity.textInfo.text = ""
                manager?.requestPeers(channel, peerListListener)
                Log.d(TAG, "P2P peers changed")

                // The peer list has changed! We should probably do something about
                // that.
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                manager!!.requestConnectionInfo(channel, connectionIntoListener)
                // Connection state changed! We should probably do something about
                // that.

            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                /*(activity.supportFragmentManager.findFragmentById(frag_list) as DeviceListFragment)
                    .apply {
                        updateThisDevice(
                            intent.getParcelableExtra<WifiP2pDevice>(
                                WifiP2pManager.EXTRA_WIFI_P2P_DEVICE)!!
                        )
                    }*/
            }
        }
    }

    private val connectionIntoListener = WifiP2pManager.ConnectionInfoListener {
        val groupOwnerAddress = it.groupOwnerAddress
        if (it.groupFormed && it.isGroupOwner) {
            Log.d(javaClass.name, "Host")
            //Ничего, колбек вызовется в другом потоке
        } else if (it.groupFormed) {
            Log.d(javaClass.name, "Client")
            val profile = ExchangeProfiles(network.profileInfo, groupOwnerAddress).exchange()
            network.updateUser(User(groupOwnerAddress, profile))
        }
    }
}