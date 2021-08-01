package tech.intouch.network

import android.Manifest
import android.content.pm.PackageManager
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class PeerListListener(private val receiver: WiFiDirectBroadcastReceiver) : WifiP2pManager.PeerListListener {
    val TAG = this.javaClass.name;
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onPeersAvailable(peerList: WifiP2pDeviceList) {
        val peers = receiver.peers
        val refreshedPeers = peerList.deviceList
        val newDevices = newAvailableDevices(prevState = peers, currState = peerList.deviceList)
        connectToEachDevice(newDevices);
        if (newDevices.isNotEmpty()) { //
            peers.clear()
            peers.addAll(refreshedPeers)
            // If an AdapterView is backed by this data, notify it
            // of the change. For instance, if you have a ListView of
            // available peers, trigger an update.
            //(listAdapter as WiFiPeerListAdapter).notifyDataSetChanged()

            // Perform any other updates needed based on the new list of
            // peers connected to the Wi-Fi P2P network.
        }
        /*if (peers.isEmpty()) {
            Log.d(TAG, "No devices found")
            return
        }*/
        Log.d(TAG, "list: $peers")
    }

    private fun newAvailableDevices(prevState: Collection<WifiP2pDevice>, currState: Collection<WifiP2pDevice>): List<WifiP2pDevice> {
        return currState.filter { it.deviceAddress !in prevState.map { item -> item.deviceAddress } }
    }

    private fun nowUnavailableDevices(prevState: Collection<WifiP2pDevice>, currState: Collection<WifiP2pDevice>): List<WifiP2pDevice> {
        return prevState.filter { it.deviceAddress !in currState.map { item -> item.deviceAddress } }
    }

    private fun connectToEachDevice(newDevices: List<WifiP2pDevice>) {
        //TODO:
        for(device in newDevices) {
            val config = WifiP2pConfig().apply {
                deviceAddress = device.deviceAddress
                wps.setup = WpsInfo.PBC
            }
            if(ContextCompat.checkSelfPermission(
                    receiver.network.activity.applicationContext,
                    Manifest.permission.CHANGE_WIFI_STATE//WIFI_P2P_CONNECTION_CHANGED_ACTION//???: какие именно permissions
                ) != PackageManager.PERMISSION_GRANTED) {
                //receiver.network.activity.textInfo.text = "no permissions CHANGE_WIFI_STATE"
                //return;//TODO: нет permissions, запрос https://developer.android.com/training/permissions/requesting
            }
            receiver.manager?.connect(receiver.channel, config, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    //TODO:
                    Log.d(TAG, "connected: ${device.deviceAddress}")
                    //Thread(BroadcastListener(receiver.activity)).start();
                    //Thread(BroadcastSender(receiver.activity)).start();
                }

                override fun onFailure(reason: Int) {
                    /*Toast.makeText(
                                this@WiFiDirectActivity,
                                "Connect failed. Retry.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }*/
                    Log.d(TAG, "fail connect: ${device.deviceAddress}")
                }
            })
        }
    }
}