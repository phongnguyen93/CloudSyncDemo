package com.phongnguyen.cloudsyncdemo.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.widget.ListView;

import com.google.common.io.Files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by phongnguyen on 8/26/16.
 */
public class IOUtils{
    public static final String TAG = IOUtils.class.getSimpleName();



    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static List<File> splitFile(File file){
        List<File> files = new ArrayList<>();
        try{
            int partCounter = 1;//I like to name parts from 001, 002, 003, ...
            //you can change it to 0 if you want 000, 001, ...

            int sizeOfFiles = 1*1024 * 1024;// 1MB
            byte[] buffer = new byte[sizeOfFiles];

            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                String name = file.getName();
                int tmp = 0;
                while ((tmp = bis.read(buffer)) > 0) {
                    //write each chunk of data into separate file with different number in name
                    File newFile = new File(file.getParent(), name + "."
                            + String.format("%03d", partCounter++));
                    try (FileOutputStream out = new FileOutputStream(newFile)) {
                        out.write(buffer, 0, tmp);//tmp is chunk size
                    }
                    files.add(newFile);
                }
            }
        }catch (IOException ex){
            Log.e(TAG,ex.getMessage());
        }
        return files;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void mergeFiles(List<File> files, File into)
            throws IOException {
        try (BufferedOutputStream mergingStream = new BufferedOutputStream(
                new FileOutputStream(into))) {
            for (File f : files) {
                Files.copy(f,mergingStream);
            }
        }
    }

    public static List<File> listOfFilesToMerge(File oneOfFiles){
        String tmpName = oneOfFiles.getName();//{name}.{number}
        final String destFileName = tmpName.substring(0, tmpName.lastIndexOf('.'));//remove .{number}
        File[] files = oneOfFiles.getParentFile().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.matches(destFileName + "[.]\\d+");
            }
        });
        Log.d(TAG,files.length+"");
        Arrays.sort(files);//ensuring order 001, 002, ..., 010, ...
        return Arrays.asList(files);
    }


}
