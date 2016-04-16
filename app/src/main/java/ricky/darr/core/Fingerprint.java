package ricky.darr.core;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;
import com.samsung.android.sdk.pass.SpassInvalidStateException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ShoppingCar.ModelProducts;
import ShoppingCar.Payment;
import ricky.darr.core.report.DatosReporte;
import ricky.darr.core.solicitudes.DatosSolicitudes;
import ricky.darr.core.webservice_controller.JsonParser;
import ricky.darr.core.webservice_controller.TxnThread;
import sessionManager.SessionManager;
//import com.tonikamitv.loginregister.R;

/**
 * Created by Ricky on 10/5/15.
 */
public class Fingerprint extends Activity{

private Spass spass=new Spass();
private SpassFingerprint mSpassFingerprint;
private Context mContext;
private JsonParser jsonParser;
private  boolean isFeaturedEnabled=false;
String authenticate="http://54.218.36.180:2000/authenticate";


private boolean onReadyIdentify= false;

private SpassFingerprint.IdentifyListener listener= new SpassFingerprint.IdentifyListener(){
  public void onStarted()
  {
      Toast.makeText(Fingerprint.this,"Fingerprint sensor touched",Toast.LENGTH_SHORT).show();
  }

    public void onReady()
    {
        Toast.makeText(Fingerprint.this,"Identify is ready",Toast.LENGTH_SHORT).show();
    }
    public void onFinished(int eventStatus)
    {
        onReadyIdentify=false;
        if (eventStatus== SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS
            || eventStatus==SpassFingerprint.STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS)
        {
            Toast.makeText(Fingerprint.this,"Authentication Sucess",Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(Fingerprint.this,"Authentication fail",Toast.LENGTH_SHORT).show();
        }
    }
};
    private SpassFingerprint.RegisterListener mRegisterListener= new SpassFingerprint.RegisterListener()
    {
        public void onFinished()
        {
            Toast.makeText(Fingerprint.this,"register listener finished",Toast.LENGTH_SHORT).show();
        }
    };

    //----------------------------------------------------on create bundle

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_fingerprint);
        mContext=this;
        mSpassFingerprint= new SpassFingerprint(Fingerprint.this);
        spass= new Spass();

       try {
            spass.initialize(mContext);

        }catch (Exception e)
        {

        }

        isFeaturedEnabled= spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT);

        //Check is fingerprint is enable on device
        Button mchecksupport = (Button) findViewById(R.id.checksupport);
        mchecksupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFeaturedEnabled)
                {
                    Toast.makeText(Fingerprint.this,"Featured is enable",Toast.LENGTH_SHORT).show();
                    System.out.println(mSpassFingerprint.getRegisteredFingerprintUniqueID().get(2));


                }else {
                    Toast.makeText(Fingerprint.this,"Featured is not enable",Toast.LENGTH_SHORT).show();

                }

            }
        });
        //fingerprint registration
        Button mRegister= (Button)findViewById(R.id.buttonRegisterFinger);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!isFeaturedEnabled)
                    {
                        Toast.makeText(Fingerprint.this,"Authentication fail",Toast.LENGTH_SHORT).show();
                    }else{
                        if (onReadyIdentify==false)
                        {
                            mSpassFingerprint.registerFinger(Fingerprint.this, mRegisterListener);
                            Toast.makeText(Fingerprint.this,"jump to the enroll screen",Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(Fingerprint.this,"Cancel identify first",Toast.LENGTH_SHORT).show();
                        }
                    }

                }catch (UnsupportedOperationException e){
                    Toast.makeText(Fingerprint.this,"Fingerprint service is not supported",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //check is fingerprint is registered
        Button   mfingerprintregistered=(Button)findViewById(R.id.buttonHasRegisteredFinger);
        mfingerprintregistered.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try{
                    if (!isFeaturedEnabled)
                    {

                    }else {
                      boolean hasRegisteredFinger = mSpassFingerprint.hasRegisteredFinger();
                        Toast.makeText(Fingerprint.this,"has registered"+ hasRegisteredFinger,Toast.LENGTH_SHORT).show();

                    }

                }catch (UnsupportedOperationException e)
                {
                    Toast.makeText(Fingerprint.this,"Fingerprint service is not supported",Toast.LENGTH_SHORT).show();
                }
            }
        });
     //identify the fingerprint
     Button mIdentifyFinger=(Button)findViewById(R.id.buttonIdentify);
        mIdentifyFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                   if (!isFeaturedEnabled)
                   {
                       Toast.makeText(Fingerprint.this,"Fingerprint service is not supported",Toast.LENGTH_SHORT).show();

                   }else {
                       if (mSpassFingerprint.hasRegisteredFinger()){
                           //Toast.makeText(Fingerprint.this,"Register first",Toast.LENGTH_SHORT).show();
                           mSpassFingerprint.startIdentify(listener);

                       }else{
                           if (onReadyIdentify==false){
                               try{
                                   onReadyIdentify=true;
                                   mSpassFingerprint.startIdentify(listener);
                                   Toast.makeText(Fingerprint.this,"identify finger to verify",Toast.LENGTH_SHORT).show();


                               }catch (SpassInvalidStateException ise){
                                   onReadyIdentify=false;
                                   if (ise.getType()==SpassInvalidStateException.STATUS_OPERATION_DENIED)
                                   {
                                       Toast.makeText(Fingerprint.this,"exception"+ ise.getMessage(),Toast.LENGTH_SHORT).show();

                                   }

                               }catch (IllegalStateException e){
                                   onReadyIdentify=false;
                                   Toast.makeText(Fingerprint.this,"exception"+e,Toast.LENGTH_SHORT).show();

                               }

                           }else{
                               Toast.makeText(Fingerprint.this,"Cancel identify first",Toast.LENGTH_SHORT).show();

                           }
                       }

                   }

                }catch (UnsupportedOperationException e)
                {
                    Toast.makeText(Fingerprint.this,"Fingerprint service is not supported",Toast.LENGTH_SHORT).show();

                }
            }
        });
          //cancel identify
        Button mCancelIdentify= (Button) findViewById(R.id.buttonCancel);
        mCancelIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if (!isFeaturedEnabled)
                    {
                        Toast.makeText(Fingerprint.this,"fingerprint is not supported",Toast.LENGTH_SHORT).show();

                    }else {
                        if (onReadyIdentify==true)
                        {
                            try {
                                mSpassFingerprint.cancelIdentify();
                                Toast.makeText(Fingerprint.this,"Identify cancelled",Toast.LENGTH_SHORT).show();
                            }catch (IllegalStateException ise){
                                Toast.makeText(Fingerprint.this,ise.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        }else
                        {
                            Toast.makeText(Fingerprint.this,"request identify first",Toast.LENGTH_SHORT).show();


                        }
                    }

                }catch (UnsupportedOperationException e)
                {
                    Toast.makeText(Fingerprint.this,"Fingerprint service is not supported",Toast.LENGTH_SHORT).show();

                }
            }
        });

        //identify with dialog
        Button mIdentifywithDialog = (Button) findViewById(R.id.buttonIdentifyWithdialog);
        mIdentifywithDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (!isFeaturedEnabled)
                    {
                        Toast.makeText(Fingerprint.this,"Fingerprint service is not supported",Toast.LENGTH_SHORT).show();

                    }else {
                        if (mSpassFingerprint.hasRegisteredFinger()){
                            //Toast.makeText(Fingerprint.this,"Register first",Toast.LENGTH_SHORT).show();
                            mSpassFingerprint.startIdentifyWithDialog(Fingerprint.this,listener,true);

                        }else{
                            if (onReadyIdentify==false){
                                try{
                                    onReadyIdentify=true;
                                    mSpassFingerprint.startIdentifyWithDialog(Fingerprint.this,listener,true);
                                    Toast.makeText(Fingerprint.this,"identify finger to verify",Toast.LENGTH_SHORT).show();



                                }catch (SpassInvalidStateException ise){
                                    onReadyIdentify=false;
                                    if (ise.getType()==SpassInvalidStateException.STATUS_OPERATION_DENIED)
                                    {
                                        Toast.makeText(Fingerprint.this,"exception"+ ise.getMessage(),Toast.LENGTH_SHORT).show();

                                    }

                                }catch (IllegalStateException e){
                                    onReadyIdentify=false;
                                    Toast.makeText(Fingerprint.this,"exception"+e,Toast.LENGTH_SHORT).show();

                                }

                            }else{
                                Toast.makeText(Fingerprint.this,"Cancel identify first",Toast.LENGTH_SHORT).show();

                            }
                        }

                    }

                }catch (UnsupportedOperationException e)
                {
                    Toast.makeText(Fingerprint.this,"Fingerprint service is not supported",Toast.LENGTH_SHORT).show();

                }

            }
        });

        //List Registered Fingerprint names
        Button mlistFingerprintNames= (Button) findViewById(R.id.buttonGetRegisteredFingerprintName);
        mlistFingerprintNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!isFeaturedEnabled)
                    {

                    }else
                    {
                        SparseArray<String> mlist= mSpassFingerprint.getRegisteredFingerprintName();
                        if (mlist==null){
                            Toast.makeText(Fingerprint.this,"No fingerprint",Toast.LENGTH_SHORT).show();

                        }else
                        {
                            String log="";
                            for (int i=0;i<mlist.size();i++)
                            {
                                int index= mlist.keyAt(i);
                                String name= mlist.get(index);
                                log =log+"index"+index+" : name = " +name +"\n";
                            }
                            Toast.makeText(Fingerprint.this,log,Toast.LENGTH_SHORT).show();

                        }
                    }

                }catch (UnsupportedOperationException e)
                {
                     Toast.makeText(Fingerprint.this,"Fingerprint service is not supported",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void identifyWithDialog(final Activity activity, final Context context){
        final ModelProducts modelProducts = new ModelProducts();
        final DatosReporte datosReporte = new DatosReporte();
        final DatosSolicitudes datosSolicitudes = new DatosSolicitudes();
        final ArrayList<String> centerList = new ArrayList<>();
        final ArrayList<String>insuranceList = new ArrayList<>();
        final ArrayList<String>procedureList = new ArrayList<>();
        final ArrayList<String>procedureListId = new ArrayList<>();
        final ArrayList<String> bandejaList = new ArrayList<>();
        final ArrayList<String> plate_id = new ArrayList<>();
        final TxnThread txnThread = new TxnThread(activity,false,false);
        txnThread.setRunnable(new Runnable() {
            @Override
            public void run() {
                txnThread.setTitle("Cargando Datos");
                txnThread.setText("Por favor... espere");
                JSONObject loginObject = loginUser("001");

                if(loginObject.length() < 3){
                    txnThread.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showErrorMessage(activity,context);
                        }
                    });
                }else{
                    try {
                        SessionManager.getInstance().getSession().setToken(loginObject.getString("token"));
                        SessionManager.getInstance().getSession().setKey_sucess(loginObject.getString("key_success"));
                        SessionManager.getInstance().getSession().setUser_id(((JSONObject) loginObject.get("userData")).getString("_id"));
                        SessionManager.getInstance().getSession().setMail(((JSONObject)loginObject.get("userData")).getString("mail"));
                        SessionManager.getInstance().getSession().setPassword(((JSONObject)loginObject.get("userData")).getString("pass"));
                        SessionManager.getInstance().getSession().setUsername(((JSONObject)loginObject.get("userData")).getString("user"));

                        datosSolicitudes.getProcedure();
                        datosSolicitudes.getRequest();
                        datosReporte.getReports();
                        modelProducts.getProducts();
                        modelProducts.getCar();

                        JSONArray centers = datosReporte.getCenters();
                        JSONArray insurances = datosReporte.getARS();
                        JSONArray allplates = datosSolicitudes.getPlates();
                        JSONArray procedure = SessionManager.getInstance().getSession().getProcedureArray();


                        for (int i = 0; i < centers.length(); i++) {
                            JSONObject jCenters = centers.getJSONObject(i);
                            String center_name = String.valueOf(jCenters.getString("center_name"));
                            centerList.add(center_name);
                            SessionManager.getInstance().getSession().setCenter(centerList);
                        }

                        for (int i = 0; i < insurances.length(); i++) {
                            JSONObject jInsurances = insurances.getJSONObject(i);
                            String insurance_name = String.valueOf(jInsurances.getString("insurance_name"));
                            insuranceList.add(insurance_name);
                            SessionManager.getInstance().getSession().setInsurance(insuranceList);
                        }
                        for (int i = 0; i < procedure.length(); i++) {
                            JSONObject jprocedure = procedure.getJSONObject(i);
                            String procedure_name = String.valueOf(jprocedure.getString("procedure_desc"));
                            String procedure_id = String.valueOf(jprocedure.getString("_id"));
                            procedureList.add(procedure_name);
                            procedureListId.add(procedure_id);
                            SessionManager.getInstance().getSession().setProcedure_name(procedureList);
                            SessionManager.getInstance().getSession().setProcedure_id(procedureListId);
                        }
                        JSONArray plates = datosSolicitudes.getPlatesByuser(SessionManager.getInstance().getSession().getProcedure_id().get(0));
                        for (int i = 0; i < plates.length(); i++) {
                            JSONObject jplate = plates.getJSONObject(i);
                            String plate_id = jplate.getString("plate_id");
                            bandejaList.add(plate_id);
                            SessionManager.getInstance().getSession().setPlate_id(bandejaList);
                        }

                        for (int i = 0; i < allplates.length(); i++) {
                            JSONObject jplate_id = allplates.getJSONObject(i);
                            String bandeja_id = jplate_id.getString("plate_id");
                            String _id = jplate_id.getString("_id");
                            plate_id.add(bandeja_id);
                            plate_id.add(_id);

                            SessionManager.getInstance().getSession().setBandeja_id(plate_id);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(context, MainActivity.class);
                   context.startActivity(intent);

                }

                txnThread.close();
            }
        });
        txnThread.show();

        /*try {
            if (!isFeaturedEnabled)
            {
                Toast.makeText(activity,"Fingerprint service is not supported",Toast.LENGTH_SHORT).show();

            }else {
                if (mSpassFingerprint.hasRegisteredFinger()){
                    //Toast.makeText(Fingerprint.this,"Register first",Toast.LENGTH_SHORT).show();
                    mSpassFingerprint.startIdentifyWithDialog(activity,listener,true);

                }else{
                    if (onReadyIdentify==false){
                        try{
                            onReadyIdentify=true;
                            mSpassFingerprint.startIdentifyWithDialog(Fingerprint.this,listener,true);
                            Toast.makeText(activity,"identify finger to verify",Toast.LENGTH_SHORT).show();
                        }catch (SpassInvalidStateException ise){
                            onReadyIdentify=false;
                            if (ise.getType()==SpassInvalidStateException.STATUS_OPERATION_DENIED)
                            {
                                Toast.makeText(activity,"exception"+ ise.getMessage(),Toast.LENGTH_SHORT).show();

                            }

                        }catch (IllegalStateException e){
                            onReadyIdentify=false;
                            Toast.makeText(activity,"exception"+e,Toast.LENGTH_SHORT).show();

                        }

                    }else{
                        Toast.makeText(activity,"Cancel identify first",Toast.LENGTH_SHORT).show();

                    }
                }

            }

        }catch (UnsupportedOperationException e)
        {
            Toast.makeText(activity,"Fingerprint service is not supported",Toast.LENGTH_SHORT).show();

        }
        */
    }
    public JSONObject loginUser(String fingerprint) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("fingerprint", fingerprint));
        params.add(new BasicNameValuePair("user", "0"));
        params.add(new BasicNameValuePair("pass", "0"));
        JSONObject json = JsonParser.postJSONFromUrl(authenticate, params);
        return json;
    }
    private void showErrorMessage(Activity activity, final Context context) {
        new AlertDialog.Builder(activity)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Registrar fingerprint")
                .setMessage("No cuenta con un registro de fingerprint, desea registrarlo?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context,Register.class);
                        context.startActivity(intent);
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }



}
