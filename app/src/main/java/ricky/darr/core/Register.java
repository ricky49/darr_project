package ricky.darr.core;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import ricky.darr.core.webservice_controller.JsonParser;
import ricky.darr.core.webservice_controller.TxnThread;
import sessionManager.SessionManager;
//import com.tonikamitv.loginregister.R;


public class Register extends ActionBarActivity implements View.OnClickListener{
    EditText etName, etcorreo, etUsername, etPassword,etapellido,etCedula;
    Button bRegister;
    String fingerprint="";

    private JsonParser jsonParser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
        fingerprint = intent.getStringExtra("fingerprint");
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRegister:

                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

               final User user= new User(username, password);
                final TxnThread txnThread = new TxnThread(this,false,false);
                txnThread.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (username.equals("") || password.equals("")) {
                                txnThread.close();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        emptyField();
                                    }
                                });
                            }else{
                                txnThread.setText("Registrando Huella");
                                JSONObject loginObject= user.loginUser(username, password, "0");
                                SessionManager.getInstance().getSession().setToken(loginObject.getString("token"));
                                String id = String.valueOf(((JSONObject) loginObject.get("userData")).getString("_id"));
                                if (loginObject.length() < 3) {
                                    txnThread.close();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            txnThread.close();
                                                showErrorMessage();

                                        }
                                    });
                                }else{
                                    user.register(id,fingerprint);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(Register.this, "fingerprint registrado exitosamente", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    Intent loginIntent = new Intent(Register.this, Login.class);
                                    startActivity(loginIntent);
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            txnThread.close();
                        }

                    }
                });
                txnThread.show();
                break;

        }
    }




    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Register.this);
        dialogBuilder.setMessage("Usuario o Clave  Incorrecto");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }
    private void emptyField() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Register.this);
        dialogBuilder.setMessage("Uno o mas campos estan Vacio, para continuar, favor llenar todos los campos");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

}
