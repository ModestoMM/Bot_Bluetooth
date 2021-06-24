
package com.modesto.bot_bluetooth.http.Stickers;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Thumb {

    @SerializedName("file_id")
    @Expose
    private String fileId;
    @SerializedName("file_unique_id")
    @Expose
    private String fileUniqueId;
    @SerializedName("file_size")
    @Expose
    private String fileSize;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileUniqueId() {
        return fileUniqueId;
    }

    public void setFileUniqueId(String fileUniqueId) {
        this.fileUniqueId = fileUniqueId;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

}
