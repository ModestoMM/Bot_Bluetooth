package com.modesto.bot_bluetooth.http.ReplyKeyboardMarkup;

public class KeyboardButton {

    private String text;
    private Boolean request_contact;
    private Boolean request_location;
    private KeyboardButtonPollType request_poll;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getRequest_contact() {
        return request_contact;
    }

    public void setRequest_contact(Boolean request_contact) {
        this.request_contact = request_contact;
    }

    public Boolean getRequest_location() {
        return request_location;
    }

    public void setRequest_location(Boolean request_location) {
        this.request_location = request_location;
    }

    public KeyboardButtonPollType getRequest_poll() {
        return request_poll;
    }

    public void setRequest_poll(KeyboardButtonPollType request_poll) {
        this.request_poll = request_poll;
    }
}
