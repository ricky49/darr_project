package ricky.darr.core.solicitudes;

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

import org.json.JSONArray;
import org.json.JSONObject;

import design.CustomOnItemSelectedListener;
import mail.Correo;
import ricky.darr.core.R;
import ricky.darr.core.webservice_controller.TxnThread;
import sessionManager.SessionManager;

//import com.tonikamitv.loginregister.R;

/**
 * Created by Ricky on 9/8/15.
 */
public class Solicitudes  extends Fragment {

    EditText etpaciente, etcedula, etnss, ettelefonopaciente, etautorizacion, etfechacirugia,etcirujanoID;
    Spinner sPprocedimiento,sPcentro,sPbandeja,spArs;
    Button bsolicitudes;
    String subject="Solicitud de Materiales ";
    String bodyMessage="Una solicitud fue enviada, sino fue usted el responsable, por favor envie un mail a esta cuenta lo antes posible";
    String bodyMessageAdmin="Nueva solicitud recibida";



    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_solicitudes, container, false);

        etpaciente = (EditText) view.findViewById(R.id.etpaciente);
        etcedula = (EditText) view.findViewById(R.id.etcedula);
        etnss = (EditText) view.findViewById(R.id.etNSS);
        ettelefonopaciente = (EditText) view.findViewById(R.id.ettelefono_paciente);
        spArs = (Spinner) view.findViewById(R.id.spArs);
        etautorizacion = (EditText) view.findViewById(R.id.etautorizacion);
        sPprocedimiento = (Spinner) view.findViewById(R.id.etprocedimientoID);
        etfechacirugia = (EditText) view.findViewById(R.id.etfechacirugia);
        sPcentro = (Spinner) view.findViewById(R.id.spCentro);
        etcirujanoID = (EditText) view.findViewById(R.id.etCIRUJANOID);
        sPbandeja = (Spinner)view.findViewById(R.id.spBandeja);
        System.out.println(SessionManager.getInstance().getSession().getSelectedPlate());

        addItemsOnSpinner(sPcentro, spArs, sPbandeja, sPprocedimiento);
        addListenerOnSpinnerItemSelection(sPcentro, spArs,sPbandeja,sPprocedimiento);

        bsolicitudes= (Button) view.findViewById(R.id.benviarSolicitud);
        bsolicitudes.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String paciente = etpaciente.getText().toString();
                String ars = SessionManager.getInstance().getSession().getSelectedArs();
                String procedimiento = SessionManager.getInstance().getSession().getSelectedProcedure();
                String cedula = etcedula.getText().toString();
                String telefono_paciente = ettelefonopaciente.getText().toString();
                String autorizacion = etautorizacion.getText().toString();
                String fecha_cirugia = etfechacirugia.getText().toString();
                String centro= SessionManager.getInstance().getSession().getSelectedCenter();
                String cirujanoID=etcirujanoID.getText().toString();
                String bandeja = SessionManager.getInstance().getSession().getSelectedPlate();
                String nss = etnss.getText().toString();


                final DatosSolicitudes datosSolicitudes = new DatosSolicitudes( paciente,  ars,  procedimiento, cedula, telefono_paciente, autorizacion, fecha_cirugia, centro,  cirujanoID, bandeja,nss);

                final TxnThread txnThread = new TxnThread((Activity)getContext(),false,false);
                txnThread.setTitle("Envio de solicitud");
                txnThread.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            txnThread.setText("Enviando solicitud");
                            JSONObject solicitud = datosSolicitudes.sendSolicitud();
                            if (solicitud.getString("_id")!= null) {
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
                if(SessionManager.getInstance().getSession().getKey_sucess().equals("send")){
                    SessionManager.getInstance().getSession().setKey_sucess("");
                    mailUser(subject, bodyMessage);
                    mailAdmin("darrproject@gmail.com", subject, bodyMessageAdmin);
                }
            }
        }));

        return view;


    }



    private void mailUser(String subject, String bodyMessage){
        String email= SessionManager.getInstance().getSession().getMail();
        Correo correo= new Correo(getContext());
        correo.sendMail(email, subject, bodyMessage);

    }
    private void mailAdmin(String mail,String subject, String bodyMessage){

        Correo correo= new Correo(getContext());
        correo.sendMail(mail,subject,bodyMessage);
    }

    public void addItemsOnSpinner(Spinner spCentro, Spinner spArs, Spinner sPbandeja, Spinner sPprocedimiento) {

        ArrayAdapter<String> centerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, SessionManager.getInstance().getSession().getCenter());
        centerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCentro.setAdapter(centerAdapter);

        ArrayAdapter<String> arsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, SessionManager.getInstance().getSession().getInsurance());
        arsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spArs.setAdapter(arsAdapter);

        ArrayAdapter<String> bandejaAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, SessionManager.getInstance().getSession().getPlate_id());
        centerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sPbandeja.setAdapter(bandejaAdapter);

        ArrayAdapter<String> procedimientoAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, SessionManager.getInstance().getSession().getProcedure_name());
        arsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sPprocedimiento.setAdapter(procedimientoAdapter);

    }

    public void addListenerOnSpinnerItemSelection(Spinner spCentro, Spinner spArs,Spinner sPbandeja, Spinner sPprocedimiento) {
        spCentro.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spArs.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        sPbandeja.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        sPprocedimiento.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

}
