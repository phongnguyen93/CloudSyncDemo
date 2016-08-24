package com.phongnguyen.cloudsyncdemo.models;

/**
 * Created by phongnguyen on 8/24/16.
 */
public class MyFile {
    private String id;
    private String file_name;
    private String extension;
    private long bytes;
    private String user_last_modified;
    private boolean is_dir;

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
}
