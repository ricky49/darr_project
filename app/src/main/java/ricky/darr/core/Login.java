package ricky.darr.core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;
import com.samsung.android.sdk.pass.SpassInvalidStateException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ShoppingCar.ModelProducts;
import ricky.darr.core.report.DatosReporte;
import ricky.darr.core.solicitudes.DatosSolicitudes;
import ricky.darr.core.webservice_controller.JsonParser;
import ricky.darr.core.webservice_controller.TxnThread;
import sessionManager.SessionManager;




public class Login extends AppCompatActivity implements View.OnClickListener {
    Button bLogin;
    TextView registerLink;
    EditText etUsername, etPassword;
    ImageView fingerprint;
    String fingerId;

    private Spass spass=new Spass();
    private SpassFingerprint mSpassFingerprint;
    private Context mContext;
    private JsonParser jsonParser;
    private  boolean isFeaturedEnabled=false;

    private boolean onReadyIdentify= false;

    private SpassFingerprint.IdentifyListener listener= new SpassFingerprint.IdentifyListener(){
        public void onStarted()
        {
            Toast.makeText(Login.this, "Fingerprint sensor touched", Toast.LENGTH_SHORT).show();
        }

        public void onReady()
        {
            Toast.makeText(Login.this,"Identify is ready",Toast.LENGTH_SHORT).show();
        }
        public void onFinished(int eventStatus)
        {
            onReadyIdentify=false;
            if (eventStatus== SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS
                    || eventStatus==SpassFingerprint.STATUS_AUTHENTIFICATION_PASSWORD_SUCCESS)
            {
                Toast.makeText(Login.this,"Authentication Sucess",Toast.LENGTH_SHORT).show();
                int fingerprintIndex = mSpassFingerprint.getIdentifiedFingerprintIndex();
                 fingerId = String.valueOf(mSpassFingerprint.getRegisteredFingerprintUniqueID().get(fingerprintIndex));
                login("0", "0", fingerId, true);

            }else{
                Toast.makeText(Login.this,"Authentication fail",Toast.LENGTH_SHORT).show();
            }
        }
    };
    private SpassFingerprint.RegisterListener mRegisterListener= new SpassFingerprint.RegisterListener()
    {
        public void onFinished()
        {
            Toast.makeText(Login.this,"register listener finished",Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext=this;
        mSpassFingerprint= new SpassFingerprint(Login.this);
        spass= new Spass();

        try {
            spass.initialize(mContext);
            isFeaturedEnabled= spass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT);

        }catch (Exception e)
        {

        }



        bLogin = (Button) findViewById(R.id.bLogin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        //registerLink = (TextView) findViewById(R.id.tvRegisterLink);
        fingerprint = (ImageView) findViewById(R.id.ivFingerprint);


        bLogin.setOnClickListener(this);
        //registerLink.setOnClickListener(this);
        fingerprint.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bLogin:
                 String username = etUsername.getText().toString();
                 String password = etPassword.getText().toString();
                login(username, password,"0",false);

                break;
            case R.id.ivFingerprint:
                try {
                    if (!isFeaturedEnabled)
                    {
                        Toast.makeText(Login.this,"Fingerprint service is not supported",Toast.LENGTH_SHORT).show();

                    }else {
                        if (mSpassFingerprint.hasRegisteredFinger()){
                            //Toast.makeText(Fingerprint.this,"Register first",Toast.LENGTH_SHORT).show();
                            mSpassFingerprint.startIdentifyWithDialog(Login.this,listener,true);

                        }else{
                            if (onReadyIdentify==false){
                                try{
                                    onReadyIdentify=true;
                                    mSpassFingerprint.startIdentifyWithDialog(Login.this,listener,true);
                                    Toast.makeText(Login.this,"identify finger to verify",Toast.LENGTH_SHORT).show();



                                }catch (SpassInvalidStateException ise){
                                    onReadyIdentify=false;
                                    if (ise.getType()==SpassInvalidStateException.STATUS_OPERATION_DENIED)
                                    {
                                        Toast.makeText(Login.this,"exception"+ ise.getMessage(),Toast.LENGTH_SHORT).show();

                                    }

                                }catch (IllegalStateException e){
                                    onReadyIdentify=false;
                                    Toast.makeText(Login.this,"exception"+e,Toast.LENGTH_SHORT).show();

                                }

                            }else{
                                Toast.makeText(Login.this,"Cancel identify first",Toast.LENGTH_SHORT).show();

                            }
                        }

                    }

                }catch (UnsupportedOperationException e)
                {
                    Toast.makeText(Login.this,"Fingerprint service is not supported",Toast.LENGTH_SHORT).show();

                }
                break;

        }
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Usuario o Clave  Incorrecto");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }
    private void emptyField() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Uno o mas campos estan Vacio, para continuar, favor llenar todos los campos");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    public void login(final String username, final String password, final String fingerprint, final Boolean scanner){
        final TxnThread txnThread = new TxnThread(Login.this,false,false);
        final User user = new User();
        final ModelProducts modelProducts = new ModelProducts();
        final DatosReporte datosReporte = new DatosReporte();
        final DatosSolicitudes datosSolicitudes = new DatosSolicitudes();
        final ArrayList<String> centerList = new ArrayList<>();
        final ArrayList<String>insuranceList = new ArrayList<>();
        final ArrayList<String>procedureList = new ArrayList<>();
        final ArrayList<String>procedureListId = new ArrayList<>();
        final ArrayList<String> bandejaList = new ArrayList<>();
        final ArrayList<String> plate_id = new ArrayList<>();

         txnThread.setRunnable(new Runnable() {
             @Override
             public void run() {
                 if (username.equals("") || password.equals("") && scanner ==false) {
                     txnThread.close();
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             emptyField();
                         }
                     });

                 } else {
                     txnThread.setTitle("Verificando Credenciales");
                     txnThread.setText("Cargando los Datos");
                     JSONObject loginObject = user.loginUser(username, password, fingerprint);
                     if (loginObject.length() < 3) {
                         txnThread.close();
                         runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 if (scanner) {
                                    showMessage(fingerId);
                                 } else {
                                     showErrorMessage();
                                 }

                             }
                         });
                     } else {
                         try {
                             SessionManager.getInstance().getSession().setToken(loginObject.getString("token"));
                             SessionManager.getInstance().getSession().setKey_sucess(loginObject.getString("key_success"));
                             SessionManager.getInstance().getSession().setUser_id(((JSONObject) loginObject.get("userData")).getString("_id"));
                             SessionManager.getInstance().getSession().setMail(((JSONObject) loginObject.get("userData")).getString("mail"));
                             SessionManager.getInstance().getSession().setPassword(((JSONObject) loginObject.get("userData")).getString("pass"));
                             SessionManager.getInstance().getSession().setUsername(((JSONObject) loginObject.get("userData")).getString("user"));

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
                             Intent intent = new Intent(Login.this, MainActivity.class);
                             startActivity(intent);

                         } catch (JSONException e) {
                             e.printStackTrace();
                         }

                     }
                 }


                 txnThread.close();

             }
         });

        txnThread.show();
    }

    private void showMessage(final String fingerprint) {
        new AlertDialog.Builder(Login.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Registrar fingerprint")
                .setMessage("No cuenta con un registro de fingerprint, desea registrarlo?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Login.this,Register.class);
                        intent.putExtra("fingerprint",fingerprint);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("No", null)
                .show();

    }



}



