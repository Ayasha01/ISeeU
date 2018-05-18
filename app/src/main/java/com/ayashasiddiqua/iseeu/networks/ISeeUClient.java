package com.ayashasiddiqua.iseeu.networks;

import android.content.Context;

import com.ayashasiddiqua.iseeu.models.Settings;
import com.ayashasiddiqua.iseeu.settings.SettingsProvider;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ISeeUClient {
    private DatagramSocket datagramSocket;
    private DatagramPacket packet;
    private InetAddress inetAddress;

    private Settings settings;

    public ISeeUClient(Context context) {
        SettingsProvider settingsProvider = new SettingsProvider(context);
        settings = settingsProvider.getSettings();
    }

    public void init() {

        try {
            datagramSocket = new DatagramSocket();
            inetAddress = InetAddress.getByName(settings.getHost());
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void send(final byte[] data) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                packet = new DatagramPacket(data, data.length, inetAddress, settings.getPort());
                try {
                    datagramSocket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
