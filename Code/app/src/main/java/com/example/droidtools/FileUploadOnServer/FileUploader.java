package com.example.droidtools.FileUploadOnServer;

public class FileUploader {

    private  String name;
    private  String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public FileUploader(String name, String url) {
        this.name = name;
        this.url = url;
    }
    public FileUploader(){}
}
