package com.phongnguyen.cloudsyncdemo.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by phongnguyen on 8/24/16.
 */
public class MyFolder implements Parcelable {
    private String name;
    private int total_files;
    private int current_page;
    private int total_pages;
    private ArrayList<MyFile> file_info;

    @SuppressWarnings("unchecked")
    public MyFolder(Parcel in) {
        setTotalFiles(in.readInt());
        setCurrentPage(in.readInt());
        setTotalPages(in.readInt());
        setName(in.readString());
        setFileList(in.readArrayList(MyFile.class.getClassLoader()));
    }

    public int getTotalFiles() {
        return total_files;
    }

    public void setTotalFiles(int total_files) {
        this.total_files = total_files;
    }

    public int getCurrentPage() {
        return current_page;
    }

    public void setCurrentPage(int current_page) {
        this.current_page = current_page;
    }

    public int getTotalPages() {
        return total_pages;
    }

    public void setTotalPages(int total_pages) {
        this.total_pages = total_pages;
    }

    public ArrayList<MyFile> getFileList() {
        return file_info;
    }

    public void setFileList(ArrayList<MyFile> file_info) {
        this.file_info = file_info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(file_info);
        parcel.writeInt(total_files);
        parcel.writeInt(total_pages);
        parcel.writeInt(current_page);
        parcel.writeString(name);
    }

    public static final Parcelable.Creator<MyFolder> CREATOR
            = new Parcelable.Creator<MyFolder>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public MyFolder createFromParcel(Parcel in) {
            return new MyFolder(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public MyFolder[] newArray(int size) {
            return new MyFolder[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

