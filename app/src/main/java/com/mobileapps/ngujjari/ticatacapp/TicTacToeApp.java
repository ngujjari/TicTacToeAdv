package com.mobileapps.ngujjari.ticatacapp;

import android.app.Application;

/**
 * Created by ngujjari on 2/21/15.
 */
public class TicTacToeApp extends Application {

    public TicTacAbstractController getMs() {
        return ms;
    }

    public void setMs(TicTacAbstractController ms) {
        this.ms = ms;
    }

    private TicTacAbstractController ms = null;

}
