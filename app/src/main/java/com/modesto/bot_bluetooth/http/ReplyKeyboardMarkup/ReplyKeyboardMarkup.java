package com.modesto.bot_bluetooth.http.ReplyKeyboardMarkup;

import java.util.List;

public class ReplyKeyboardMarkup {

    private List<KeyboardButton> keyboard;
    private Boolean resize_keyboard;
    private Boolean one_time_keyboard;
    private Boolean selective;

    public List<KeyboardButton> getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(List<KeyboardButton> keyboard) {
        this.keyboard = keyboard;
    }

    public Boolean getResize_keyboard() {
        return resize_keyboard;
    }

    public void setResize_keyboard(Boolean resize_keyboard) {
        this.resize_keyboard = resize_keyboard;
    }

    public Boolean getOne_time_keyboard() {
        return one_time_keyboard;
    }

    public void setOne_time_keyboard(Boolean one_time_keyboard) {
        this.one_time_keyboard = one_time_keyboard;
    }

    public Boolean getSelective() {
        return selective;
    }

    public void setSelective(Boolean selective) {
        this.selective = selective;
    }
}
