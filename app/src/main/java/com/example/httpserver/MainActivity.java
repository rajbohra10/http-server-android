package com.example.httpserver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.example.httpserver.server.MyServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    private MyServer server;
    private Button startBtn, stopBtn;
    private TextView ipTextView;
    private String address;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = findViewById(R.id.startBtn);
        stopBtn = findViewById(R.id.stopBtn);
        ipTextView = findViewById(R.id.ipTextView);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (server==null){
                    try {
                        server = new MyServer();
                        Toast.makeText(getApplicationContext(), "Server Started", Toast.LENGTH_SHORT).show();
                        getMyIp();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (server!=null){
                    server.stop();
                    server = null;
                    Toast.makeText(getApplicationContext(), "Server Stopped", Toast.LENGTH_SHORT).show();
                    ipTextView.setText("");
                }
            }
        });




    }



    public void getMyIp(){
        String info = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()){
                        info = inetAddress.getHostAddress().toString();
                        if (info.length()<=16){
                            ipTextView.setText("Connect to https://"+info+":"+server.getListeningPort());
                            address = "https://"+info+":"+server.getListeningPort();
                            return;
                        }


                    }

                }
            }

        }
        catch (SocketException ex)
        {
            Log.e("ServerActivity", ex.toString());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (server!=null){
            server.stop();
            ipTextView.setText("");
        }
    }
}
