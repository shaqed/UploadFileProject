package com.example.dell.uploadfileproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader {

    /**
     * This method will download a file from a URL to the local storage of the Android device
     * @param context use getApplicationContext() here
     * @param httpUrl The URL of the servlet that serves the files
     * @param destinationUrl The URL of the destination FOLDER in which you want to download this file to.<br>
     *                       WARNING !!<br> You cannot write to /root in regular devices. <br>
     *                       Use this to access sd-card: Environment.getExternalStorageDirectory().getPath()
     * */
    public void downloadFile(Context context, final String httpUrl, final String destinationUrl) {
        // First, we check if this application has the permission to write files to the storage
        if (hasPermissions(context)) {
            // Starts the whole process in a different thread
            new AsyncTask<Void, Void, Void>() {
                // Whatever is inside this method will run on a different thread
                @Override
                protected Void doInBackground(Void... voids) {
                    getFileFromInternet(httpUrl, destinationUrl); // Call the function from this thread
                    return null;
                }
            }.execute();
        } else {
            L.toast(context, "Sorry, Application does not have permissions to use Internet / Writing to SDCard, Please enable this");
        }
    }

    private void getFileFromInternet(String httpUrl, String destinationUrl) {
        try {
            // Open the connection to the server
            URL url1 = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

            // Get the InputStream (We're getting data TO the RAM. FROM the connection)
            InputStream inputStream = connection.getInputStream();

            // Get the filename from the header given in the response
            String fileName = connection.getHeaderField("filename");
            // Create a new file in the destination url
            File file = new File(destinationUrl + "/" + fileName);


            if (file.createNewFile()) { // Create a new file, if successful - this will be true

                // Create a stream to the file so we can write stuff to it
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                // This code transfers all bytes from InputStream to OutputStream
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
                // End of messy code

                // Clean up
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();

                // If everything went ok, you should get here and a new file will be in your system now
                L.log("Everything went ok... check : " + file.getPath() + " for your new file");
            } else {
                // Creating new file was not successful, and returned false - probably file already exists
                L.log("Could not create a new file at: " + file.getPath());
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the given application has permissions to use the Internet and Writing to SDCARD
     * */
    private boolean hasPermissions(Context context) {
        if (context != null) {
            PackageManager packageManager = context.getPackageManager();

            // Checks for Writing to external storage permission
            int writingExternalPermission = packageManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, context.getPackageName());
            if (writingExternalPermission == PackageManager.PERMISSION_DENIED) {
                // If no such permission was granted - return false
                return false;
            }

            // Same here
            int internetPermission = packageManager.checkPermission(Manifest.permission.INTERNET, context.getPackageName());
            if (internetPermission == PackageManager.PERMISSION_DENIED) {
                return false;
            }

            // If got to this point - everything is good - so return true
            return true;
        } else {
            return false;
        }
    }
}
