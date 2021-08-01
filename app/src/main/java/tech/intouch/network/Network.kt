package tech.intouch.network

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pManager
import androidx.core.content.ContextCompat
import tech.intouch.network.data.User
import tech.intouch.network.tcp.communication_cases.SendMessage

//TODO: оповещение когда класс проинициализирован окончательно
class Network(
    val activity: Activity,
    var listUpdateCallback: (List<User>) -> Unit,
    var messageCallback: (ByteArray) -> Unit,
    var profileInfo: ByteArray = ByteArray(0)
) {
    private val intentFilter = IntentFilter();
    private var channel: WifiP2pManager.Channel
    private var manager: WifiP2pManager
    private var receiver: WiFiDirectBroadcastReceiver
    private var users = mutableListOf<User>()

    fun sendMessage(user: User, bytes: ByteArray) {
        SendMessage(user.address, bytes).run()
    }

    fun updateUser(user: User) {
        synchronized(users) {
            var isFound = false
            for (it in users) {
                if (it.address == user.address) {
                    it.profile = user.profile
                    isFound = true
                    break
                }
            }
            if (!isFound) {
                users.add(user)
            }
            listUpdateCallback(users)
        }
    }

    init {
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

        manager = activity.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(activity, activity.mainLooper, null)

        receiver = WiFiDirectBroadcastReceiver(manager, channel, this)
        activity.registerReceiver(receiver, intentFilter)

        while (ContextCompat.checkSelfPermission(
                activity.applicationContext,
                Manifest.permission.ACCESS_WIFI_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //TODO: нет permissions, запрос https://developer.android.com/training/permissions/requesting
        }
        manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                //textInfo.text = "P2P search..."
            }

            override fun onFailure(reasonCode: Int) {
                //textInfo.text = "P2P search failed with code $reasonCode"
            }
        })
    }
}