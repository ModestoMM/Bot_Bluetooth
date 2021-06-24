
package com.modesto.bot_bluetooth.http.canal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TelegramCanalRequest {

    @SerializedName("ok")
    @Expose
    private String ok;
    @SerializedName("result")
    @Expose
    private List<ResultCanal> resultCanal = null;

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public List<ResultCanal> getResultCanal() {
        return resultCanal;
    }

    public void setResultCanal(List<ResultCanal> resultCanal) {
        this.resultCanal = resultCanal;
    }

}
