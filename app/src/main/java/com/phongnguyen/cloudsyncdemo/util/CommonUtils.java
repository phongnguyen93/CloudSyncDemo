package com.phongnguyen.cloudsyncdemo.util;

import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;

import com.phongnguyen.cloudsyncdemo.models.MyFile;
import com.phongnguyen.cloudsyncdemo.models.MyFolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by phongnguyen on 8/24/16.
 */
public class CommonUtils {

    public static final String TAG = CommonUtils.class.getSimpleName();

    public static final String BASE_TEXT_SIZE = "Size: ";
    public static final String BASE_TEXT_DATE = "Last modified: ";

    public static String getFolderNameFromPath(String folderPath){
         String[] split = folderPath.split("/");
        if(split.length >0)
            return split[split.length-1];
        else
            return "root";
    }


    public static String makeFileName(MyFile item) {
        if(item.isDir())
            return item.getFileName();
        else if(item.getExtension()!=null)
            return item.getFileName()+"."+item.getExtension().toLowerCase();
        else
            return "";
    }

    public static String makeDateText(String lastModified) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.ENGLISH);
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
            Date date = dateFormat2.parse(lastModified);
            if (System.currentTimeMillis() == date.getTime()) {
                return BASE_TEXT_DATE + "Today";
            } else
                return BASE_TEXT_DATE + dateFormat.format(date);
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
            return "";
        }
    }

    public static String makeSizeText(long size) {
        int unit =  1024;
        String pre = "kMGTPE";
        if (size < unit) return size + " B";
        int exp = (int) (Math.log(size) / Math.log(unit));
        return String.format("%.1f %sB", size / Math.pow(unit, exp), pre.charAt(exp-1));
    }

    public static String makeFolderSummaryText(MyFolder folder) {
        return String.format("Folder name: %s , total files: %d",folder.getName(),folder.getTotalFiles());
    }
}
