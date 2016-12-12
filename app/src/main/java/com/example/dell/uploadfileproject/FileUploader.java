package com.example.dell.uploadfileproject;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


/**
 * FileUploader helps you create a multipart entity to be send as a multipart/form-data to a server
 * exactly as the server would expect to get a file from a regular HTML form
 *
 * How to use:
 *  1) Call the constructor with the desired URL
 *  2) Call the addFile() for each file you add
 *  3) Once done selecting files, call the connect() method to perform the the HTTP request and get the result code
 *
 *  ** MAKE SURE YOU CHECK THE RESPONSE CODE **
 * */
public class FileUploader {
    private static final String CRLF = "\r\n"; // Carriage Return New Line - Used to indicate new line in HTTP Requests
    private static final String HYPHEN = "--"; // Indicates new segment of a form-data (or end)

    private HttpURLConnection connection;
    private OutputStream outputStream;
    private MyPrintWriter printWriter;
    private String boundary;

    /**
     * Constructs a new FileUploader object with the desired url
     * @param url The desired URL
     * */
    public FileUploader(String url) throws IOException {
        connection = (HttpURLConnection) new URL(url).openConnection();
        boundary = "lol" + System.currentTimeMillis() + "lol"; // User defined boundary

        connection.setDoOutput(true); // Indicates POST method (with body)
        // Request Property: Content-Type: multipart/form-data; boundary="[YOUR BOUNDARY]"
        connection.setRequestProperty("Content-Type",
                "multipart/form-data;" +
                        " boundary=\"" + boundary + "\"");
        outputStream = connection.getOutputStream(); // get its outputStream so you can write stuff to it
        printWriter = new MyPrintWriter(outputStream, true); // We will write text to the outputStream using printWriter

        log("Constructor Ended");
    }

    /**
     * Add a new file to the multipart entity
     * @param fieldName State the field name, give this segment in the request a name
     * @param file File object representing the file to be added
     *
     * @throws IOException If the function couldn't initiate a stream to the file for what ever reason
     * an exception will be thrown.
     * */
    public void addFile(String fieldName, File file) throws IOException {
        // Write --[BOUNDARY]
        printWriter.append(HYPHEN); // --
        printWriter.append(boundary); // The boundary
        printWriter.append(CRLF); // Clear Carriage / Line Feed

        String subHeader1 = "Content-Disposition: form-data; name=\"" + fieldName + "\"; " +
                "filename=\"" + file.getName() + "\"";
        printWriter.append(subHeader1);
        printWriter.append(CRLF);

        String subHeader2 = "Content-Type: " + URLConnection.guessContentTypeFromName(file.getName());
        printWriter.append(subHeader2);
        printWriter.append(CRLF);

        String subHeader3 = "Content-Transfer-Encoding: binary";
        printWriter.append(subHeader3);
        printWriter.append(CRLF);


        printWriter.append(CRLF);

        printWriter.flush();
        // Finished with headers

        // Write File Data in bytes
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileData = new byte[(int) file.length()];
        int bytesRead = fileInputStream.read(fileData);
        log("Read a total of : " + bytesRead + " bytes of data to an array of size: " + fileData.length);

        // All data of the file exists now in the fileData bytes array
        // Write it to the outputStream of the connection
        outputStream.write(fileData);

        // Finished writing the file content
        outputStream.flush();
        fileInputStream.close();

        printWriter.append(CRLF); // After file content -> new line
        printWriter.flush(); // finished with printWriter for now... flush it
    }


    /**
     * Call this function once you're done adding files to the entity and you want to perform the request
     * */
    public int connect() throws IOException {
        printWriter.append(CRLF); // New line

        // --BOUNDARY-- Indicates that this is the end of the form
        printWriter.append(HYPHEN); // --
        printWriter.append(boundary); // BOUNDARY
        printWriter.append(HYPHEN); // --

        printWriter.append(CRLF); // New Line
        printWriter.close(); // Done with printWriter all together

        int responseCode = connection.getResponseCode();
        log("ResponseCode: " + responseCode);
        return responseCode;
    }


    private void log(String msg) {
        Log.i("TAG", msg);
    }
}
