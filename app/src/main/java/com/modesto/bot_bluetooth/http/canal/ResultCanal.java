
package com.modesto.bot_bluetooth.http.canal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultCanal {

    @SerializedName("update_id")
    @Expose
    private String updateId;
    @SerializedName("channel_post")
    @Expose
    private ChannelPost channelPost;

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public ChannelPost getChannelPost() {
        return channelPost;
    }

    public void setChannelPost(ChannelPost channelPost) {
        this.channelPost = channelPost;
    }

}
