package ricky.darr.core.report;

import android.app.Activity;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.Fragment;
import android.widget.Spinner;

import org.json.JSONObject;

import design.CustomOnItemSelectedListener;
import mail.Correo;
import ricky.darr.core.R;
import ricky.darr.core.webservice_controller.TxnThread;
import sessionManager.SessionManager;

public class Reporte  extends Fragment{

    EditText etpaciente, etdoctor, etobservaciones, etnss, etbandejas,etcirujanoID;
    Spinner spCentro, spArs;
    Button benviarReporte ;
    String subject="Reporte Medico ";
    String bodyMessage="Un reporte fue enviado desde esta cuenta, sino fue usted el responsable, por favor envie un mail a esta cuenta lo antes posible";
    String bodyMessageAdmin="Nuevo Reporte recibido";
    DatosReporte datosReporte;

    public View onCreateView (LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.activity_reporte, container, false);
        etpaciente = (EditText) view.findViewById(R.id.etpaciente);
        etdoctor = (EditText) view.findViewById(R.id.etdoctor);
        etobservaciones = (EditText) view.findViewById(R.id.etobservaciones);
        etnss = (EditText) view.findViewById(R.id.etnss);
        etcirujanoID = (EditText) view.findViewById(R.id.etcirujanoID);
        etbandejas = (EditText) view.findViewById(R.id.etbandejasID);
        spCentro =(Spinner) view.findViewById(R.id.spCentro);
        spArs =(Spinner) view.findViewById(R.id.spArs);
        addItemsOnSpinner(spCentro,spArs);
        addListenerOnSpinnerItemSelection(spCentro, spArs);

        benviarReporte=(Button) view.findViewById(R.id.benviarReporte);
        benviarReporte.setOnClickListener((new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                String paciente = etpaciente.getText().toString();
                String aseguradora = SessionManager.getInstance().getSession().getSelectedArs();
                String centro = SessionManager.getInstance().getSession().getSelectedCenter();
                String doctor = etdoctor.getText().toString();
                String observaciones = etobservaciones.getText().toString();
                String nss = etnss.getText().toString();
                String bandeja = etbandejas.getText().toString();
                String cirujano_name= etcirujanoID.getText().toString();



                final DatosReporte datosReporte = new DatosReporte(paciente,aseguradora,centro, nss, doctor, bandeja, cirujano_name, observaciones);
                final TxnThread txnThread = new TxnThread((Activity) getContext(),false,false);
                txnThread.setTitle("Envio de Reporte");
                txnThread.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            txnThread.setText("Enviando Reporte");
                            JSONObject reporte = datosReporte.sendReport();
                            if (reporte.getString("_id")!=null) {
                                SessionManager.getInstance().getSession().setKey_sucess("send");
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            txnThread.close();
                        }

                    }
                });
                txnThread.show();
                System.out.println(SessionManager.getInstance().getSession().getKey_sucess());
                if(SessionManager.getInstance().getSession().getKey_sucess().equals("send")){
                    SessionManager.getInstance().getSession().setKey_sucess("");
                    mailUser(subject, bodyMessage);
                    mailAdmin("darrproject@gmail.com", subject, bodyMessageAdmin);
                }
            }
        }));

        return view;

    }

    public void addItemsOnSpinner(Spinner spCentro, Spinner spArs) {

        ArrayAdapter<String> centerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, SessionManager.getInstance().getSession().getCenter());
        centerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCentro.setAdapter(centerAdapter);

        ArrayAdapter<String> arsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, SessionManager.getInstance().getSession().getInsurance());
        arsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spArs.setAdapter(arsAdapter);

    }

    public void addListenerOnSpinnerItemSelection(Spinner spCentro, Spinner spArs) {
        spCentro.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spArs.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }


    private void mailUser(String subject, String bodyMessage){
        String email= SessionManager.getInstance().getSession().getMail();
        Correo correo= new Correo(getContext());
        correo.sendMail(email, subject, bodyMessage);

    }
    private void mailAdmin(String mail,String subject, String bodyMessage){

        Correo correo= new Correo(getContext());
        correo.sendMail(mail, subject, bodyMessage);
    }


    }

