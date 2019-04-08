package com.example.httpserver.server;

import android.os.Environment;
import android.support.v4.content.MimeTypeFilter;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
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



    @Override
    public Response serve(IHTTPSession session) {
        String answer = "";
        String uri = session.getUri();
        try {

            File rootDir = Environment.getExternalStorageDirectory();
            File[] filesList = null;
            String filepath = "";
            if (uri.trim().equals("/")) {
                filepath = rootDir + uri.trim();
            } else {
                filepath = uri.trim();
            }

            filesList = new File(filepath).listFiles();
            answer = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>Http Server-File Transfer</title>";

            /**
             * if filepath is a File then return InputStream
             */

            if (new File(filepath).isFile()) {
                FileInputStream fis = new FileInputStream(filepath);
                String mimeType = getMimeType(filepath);
                return newChunkedResponse(Response.Status.OK, mimeType, fis);
            }

            if (new File(filepath).isDirectory()) {
                for (File detailsOfFiles : filesList) {
                    String name = detailsOfFiles.getName();
                    if (detailsOfFiles.isDirectory()){
                        name += "/";
                    }
                    answer += "<a href=\"" + detailsOfFiles.getAbsolutePath()
                            + "\" alt = \"\">"
                            + name + "</a><br>";
                }
            }

            if (filesList.length==0){
                answer = "<h3>No Files or Folders in this directory</h3>";
            }
            answer += "</head></html>";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(answer);
    }

    private static String getMimeType(String fileUrl) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(fileUrl);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }


}