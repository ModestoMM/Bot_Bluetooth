
package com.modesto.bot_bluetooth.http.canal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChannelPost {

    @SerializedName("message_id")
    @Expose
    private String messageId;
    @SerializedName("sender_chat")
    @Expose
    private SenderChat senderChat;
    @SerializedName("chat")
    @Expose
    private ChatCanal chatCanal;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("text")
    @Expose
    private String text;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public SenderChat getSenderChat() {
        return senderChat;
    }

    public void setSenderChat(SenderChat senderChat) {
        this.senderChat = senderChat;
    }

    public ChatCanal getChatCanal() {
        return chatCanal;
    }

    public void setChatCanal(ChatCanal chatCanal) {
        this.chatCanal = chatCanal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
