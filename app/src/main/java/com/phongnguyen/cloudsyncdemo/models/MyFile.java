package com.phongnguyen.cloudsyncdemo.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by phongnguyen on 8/24/16.
 */
public class MyFile  implements Parcelable{
    private String id;
    private String file_name;
    private String extension;
    private long bytes;
    private String user_last_modified;
    private boolean is_dir;

    public MyFile(Parcel parcel){
        setId(parcel.readString());
        setFileName(parcel.readString());
        setExtension(parcel.readString());
        setSize(parcel.readLong());
        setLastModified(parcel.readString());
        setIsDir(parcel.readByte() != 0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return file_name;
    }

    public void setFileName(String file_name) {
        this.file_name = file_name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getLastModified() {
        return user_last_modified;
    }

    public void setLastModified(String user_last_modified) {
        this.user_last_modified = user_last_modified;
    }

    public boolean isDir() {
        return is_dir;
    }

    public void setIsDir(boolean is_dir) {
        this.is_dir = is_dir;
    }

    public long getSize() {
        return bytes;
    }

    public void setSize(long bytes) {
        this.bytes = bytes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(file_name);
        parcel.writeString(extension);
        parcel.writeLong(bytes);
        parcel.writeString(user_last_modified);
        parcel.writeByte((byte)(is_dir ? 1 : 0));
    }

    public static final Parcelable.Creator<MyFile> CREATOR
            = new Parcelable.Creator<MyFile>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public MyFile createFromParcel(Parcel in) {
            return new MyFile(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public MyFile[] newArray(int size) {
            return new MyFile[size];
        }
    };
}
