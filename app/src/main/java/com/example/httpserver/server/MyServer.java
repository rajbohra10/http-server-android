package com.example.httpserver.server;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import javax.net.ssl.SSLEngineResult;

import fi.iki.elonen.NanoHTTPD;

public class MyServer extends NanoHTTPD {
    private final static int PORT = 8080;

    public MyServer() throws IOException {
        super(PORT);
        start();
        Log.d("Server Started", "Listen at port "+PORT  );
    }

//    @Override
//    public Response serve(IHTTPSession session) {
//
//        String msg = "<html><body><h1>Hello server</h1>\n";
//        String uri = session.getUri().substring(1);
//        if (uri.equals("raj"))
//            msg += "<p>We serve " + session.getUri() + " !</p>";
//        else
//            msg += "<p> Hello Stranger</p>";
//
//        String path = Environment.getExternalStorageDirectory().toString()+"/";
//        Log.d("Files", "Path: " + path);
//        File directory = new File(path);
//        File[] files = directory.listFiles();
//        Log.d("Files", "Size: "+ files.length);
//        for (int i = 0; i < files.length; i++)
//        {
//            Log.d("Files", "FileName:" + files[i].getName());
//        }
//        return newFixedLengthResponse( msg + "</body></html>\n" );
//
//
//    }
//
//    @Override
//    public Response serve(String uri, Method method,
//                          Map<String, String> header, Map<String, String> parameters,
//                          Map<String, String> files) {
//        String answer = "";
//
//        FileInputStream fis = null;
//        try {
//            fis = new FileInputStream(Environment.getExternalStorageDirectory()
//                    + "/UCDownloads/Witness.pdf");
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return newChunkedResponse(Response.Status.OK, "application/pdf", fis);
//    }

    @Override
    public Response serve(String uri, Method method,
                          Map<String, String> header, Map<String, String> parameters,
                          Map<String, String> files) {
        File rootDir = Environment.getExternalStorageDirectory();
        File[] filesList = null;
        String filepath = "";
        if (uri.trim().equals("/")) {
            filepath = rootDir + uri.trim();
        } else {
            filepath = uri.trim();
        }
        filesList = new File(filepath).listFiles();
        String answer = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>sdcard0 - TECNO P5 - WiFi File Transfer Pro</title>";
        if (new File(filepath).isDirectory()) {
            for (File detailsOfFiles : filesList) {
                answer += "<a href=\"" + detailsOfFiles.getAbsolutePath()
                        + "\" alt = \"\">"
                        + detailsOfFiles.getAbsolutePath() + "</a><br>";
            }
        } else {
        }
        answer += "</head></html>";
        return newFixedLengthResponse(answer);
    }



}