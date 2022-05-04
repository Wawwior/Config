package me.wawwior.config.io.impl;

import me.wawwior.config.io.AdapterInfo;

public class FileInfo implements AdapterInfo {

    public final String path, file;

    private FileInfo(String path, String file) {
        this.path = path;
        this.file = file;
    }

    public static FileInfo of(String path, String file) {
        return new FileInfo(path, file);
    }

}
