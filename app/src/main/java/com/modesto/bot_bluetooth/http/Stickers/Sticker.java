
package com.modesto.bot_bluetooth.http.Stickers;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.modesto.bot_bluetooth.http.Stickers.Thumb;


public class Sticker {

    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("emoji")
    @Expose
    private String emoji;
    @SerializedName("set_name")
    @Expose
    private String setName;
    @SerializedName("is_animated")
    @Expose
    private Boolean isAnimated;
    @SerializedName("thumb")
    @Expose
    private Thumb thumb;
    @SerializedName("file_id")
    @Expose
    private String fileId;
    @SerializedName("file_unique_id")
    @Expose
    private String fileUniqueId;
    @SerializedName("file_size")
    @Expose
    private String fileSize;

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

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public Boolean getIsAnimated() {
        return isAnimated;
    }

    public void setIsAnimated(Boolean isAnimated) {
        this.isAnimated = isAnimated;
    }

    public Thumb getThumb() {
        return thumb;
    }

    public void setThumb(Thumb thumb) {
        this.thumb = thumb;
    }

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

}
