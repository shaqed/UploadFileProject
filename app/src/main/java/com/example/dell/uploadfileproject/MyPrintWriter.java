package com.example.dell.uploadfileproject;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class MyPrintWriter extends PrintWriter {
    private PrintWriter fileWriter;
    public MyPrintWriter(OutputStream out, boolean autoFlush) throws IOException {
        super(out, autoFlush);
        File file = new File(Environment.getExternalStorageDirectory() + "/log3.txt");
        if (file.createNewFile()) {

            L.log("Created new file at : " + file.getPath());
        } else {
            L.log("Couldnt create file... prolly already exists: " + file.getPath());
        }
        fileWriter = new PrintWriter(file, "utf-8");
    }

    @Override
    public PrintWriter append(CharSequence csq) {
        try {
            writeToLog(csq + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.append(csq);
    }

    private void writeToLog(String str) throws IOException {
        fileWriter.print(str);
    }

    @Override
    public void close() {
        super.close();
        fileWriter.close();
    }
}
