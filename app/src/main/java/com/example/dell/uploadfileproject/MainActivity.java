package com.example.dell.uploadfileproject;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final byte[] data = {66,66,66,66,66,12,65,67,68,69};
    String fileName = "cropped.jpg";

    String getFileUrl = "http://10.100.102.4:8080/api/files/download";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(this);



        File file = new File(getFilesDir(), "data1.txt");
        String string = "Hello world!";
        FileOutputStream outputStream;


        try {
            file.createNewFile();
//            outputStream = openFileOutput("data.txt", Context.MODE_PRIVATE);
            outputStream = new FileOutputStream(file);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        L.log("File exists in: " + file.getPath() + " ? " + file.exists());

        /*
        File file = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
        try {
            if (file.createNewFile()) {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(data);
                fileOutputStream.flush();
                fileOutputStream.close();
                Log.e("aa", "created new file");
            } else {
                Log.e("aa", "couldnt create new file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

    @Override
    public void onClick(final View view) {

        new FileDownloader().downloadFile(this, getFileUrl, Environment.getExternalStorageDirectory().getPath());
//        new AsyncTask<Void, Void, Void>(){
//            @Override
//            protected Void doInBackground(Void... voids) {
//                try {
//
//                    FileUploader fileUploader = new FileUploader("http://10.100.102.3:8080/placeholder/file");
//                    fileUploader.addFile("file", new File(Environment.getExternalStorageDirectory() + "/" + fileName));
//                    fileUploader.connect();
//
////                    MultipartEntity multipartEntity = new MultipartEntity("http://10.100.102.3:8080/placeholder/file", "utf-8");
//
////                    multipartEntity.addFilePart("file", new File(Environment.getExternalStorageDirectory() + "/" + fileName));
////                    multipartEntity.finish();
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                return null;
//            }
//        }.execute();
    }
}
