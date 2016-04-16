package sessionManager;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ricky.darr.core.report.DatosReporte;
import ricky.darr.core.webservice_controller.TxnThread;

/**
 * Created by Ricky on 12/9/15.
 */
public class SessionManager {

    private TxnSession txnSession;

    private static SessionManager instance;

    private SessionManager() {
        this.txnSession = new TxnSession();
    }

    public static SessionManager getInstance() {
        if (instance == null)
            instance = new SessionManager();
        return instance;
    }

    public TxnSession getSession() {
        return txnSession;
    }

    public void logout(){
        txnSession.setToken("");
        txnSession.setUsername("");
        txnSession.setPassword("");
        txnSession.setKey_sucess("");
        txnSession.setMail("");
    }
}
