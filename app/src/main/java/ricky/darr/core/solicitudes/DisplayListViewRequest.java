package ricky.darr.core.solicitudes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ShoppingCar.ModelProducts;
import ricky.darr.core.R;
import ricky.darr.core.report.DatosReporte;
import ricky.darr.core.report.DatosReporteAdapter;
import ricky.darr.core.report.Reporte;
import sessionManager.SessionManager;

/**
 * Created by Ricky on 2/21/16.
 */
public class DisplayListViewRequest extends Fragment{
    JSONArray jsonArray;
    DatosSolicitudAdapter datosSolicitudAdapter;
    ListView listView;
    FloatingActionButton button;
    android.support.v4.app.FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public View onCreateView (LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_listview_request, container, false);
        getRequest();
        datosSolicitudAdapter = new DatosSolicitudAdapter(getActivity(),R.layout.request_sent);
        listView = (ListView) view.findViewById(R.id.listViewRequest);
        listView.setAdapter(datosSolicitudAdapter);
        button = (FloatingActionButton)view.findViewById(R.id.floatingRequest);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.displayListRequest, new Solicitudes());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

         Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonArray = SessionManager.getInstance().getSession().getRequestArray();
                            int count = 0;
                            String paciente, documento, tel, ars, autorizacion,fecha,centro, cirujano,procedimiento_id,bandeja,total,nss,status;
                            String [] items ={};
                            while (count < jsonArray.length()){
                                JSONObject jsonObject = jsonArray.getJSONObject(count);
                                paciente = jsonObject.getString("pacient_name");
                                documento = jsonObject.getString("document");
                                nss = jsonObject.getString("nss");
                                status = jsonObject.getString("status");
                                ars = jsonObject.getString("insurance_name");
                                bandeja = jsonObject.getString("item_manuales");
                                tel = jsonObject.getString("pacient_tel");
                                cirujano = jsonObject.getString("surgeon_name");
                                centro= jsonObject.getString("center_name");
                                fecha = jsonObject.getString("surgery_date");
                                autorizacion = jsonObject.getString("authorization");
                                procedimiento_id = jsonObject.getString("procedure_name");

                                for(int i=0; i<SessionManager.getInstance().getSession().getProcedure_id().size(); i++ ){
                                    if(SessionManager.getInstance().getSession().getProcedure_id().get(i).equals(procedimiento_id)){
                                        procedimiento_id = SessionManager.getInstance().getSession().getProcedure_name().get(i);
                                    }
                                }
                                String tmp="";
                                if(bandeja.contains(",")){
                                    items = new String[]{};
                                    items = bandeja.split(",");
                                }else {
                                    items = new String[]{bandeja};
                                }
                                for(int i=0; i<SessionManager.getInstance().getSession().getBandeja_id().size(); i++ ){
                                    for(int j= 0 ;j<items.length;j++){
                                        if(SessionManager.getInstance().getSession().getBandeja_id().get(i).equals(items[j])){
                                            bandeja = SessionManager.getInstance().getSession().getBandeja_id().get(i-1);
                                            tmp+=bandeja +",";
                                        }
                                    }
                                }
                                bandeja=tmp;
                                if (bandeja.length() > 0 && bandeja.charAt(bandeja.length()-1)==',') {
                                    bandeja = bandeja.substring(0, bandeja.length()-1);
                                }

                                total = "";

                                DatosSolicitudes datosSolicitudes= new DatosSolicitudes(paciente,ars, procedimiento_id, documento, tel, autorizacion, fecha, centro, cirujano, bandeja, total,nss,status);

                                datosSolicitudAdapter.add(datosSolicitudes);
                                count++;


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Thread.currentThread().interrupt();

                    }
                }
        );
        thread.start();
        return view;
    }

    public void getRequest(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DatosSolicitudes datosSolicitudes = new DatosSolicitudes();
                datosSolicitudes.getRequest();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

