
package com.modesto.bot_bluetooth.http.Stickers;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Stickers {

    @SerializedName("sticker")
    @Expose
    private Sticker sticker;

    public Sticker getSticker() {
        return sticker;
    }

    public void setSticker(Sticker sticker) {
        this.sticker = sticker;
    }

}
