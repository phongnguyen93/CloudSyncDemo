package com.phongnguyen.cloudsyncdemo.upload_task.models;

import java.io.File;

/**
 * Created by phongnguyen on 8/23/16.
 */
public class MetaData {
    private String name;
    private String dest;
    private long reported_total_size;
    private int offset;
    private int chunk;
    private int chunks;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public long getTotalSize() {
        return reported_total_size;
    }

    public void setTotalSize(long reported_total_size) {
        this.reported_total_size = reported_total_size;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getChunk() {
        return chunk;
    }

    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    public int getTotalChunk() {
        return chunks;
    }

    public void setTotalChunk(int chunks) {
        this.chunks = chunks;
    }


}
