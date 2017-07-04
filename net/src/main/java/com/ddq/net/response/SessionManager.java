package com.ddq.net.response;

import com.ddq.net.Session;

import java.lang.ref.WeakReference;

/**
 * Created by dongdaqing on 2017/6/30.
 */

public class SessionManager {
    private static SessionManager manager;
    private WeakReference<Session> mSessionHandler;
    private SessionManager(){

    }

    static SessionManager getManager(){
        if (manager == null)
            manager = new SessionManager();
        return manager;
    }

    public static void registerHandler(Session session){
        getManager().mSessionHandler = new WeakReference<>(session);
    }

    public static void clean(){
        if (manager != null)
            manager.mSessionHandler = null;
    }

    Session getSessionHandler() {
        return mSessionHandler != null ? mSessionHandler.get() : null;
    }
}
