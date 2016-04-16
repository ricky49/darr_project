package ricky.darr.core.report;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ricky.darr.core.R;
import sessionManager.SessionManager;

/**
 * Created by Ricky on 2/18/16.
 */
public class DisplayListView extends Fragment {
    JSONArray jsonArray;
    DatosReporteAdapter datosReporteAdapter;
    ListView listView;
    FloatingActionButton button;
    android.support.v4.app.FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public View onCreateView (LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_listview_report, container, false);
        datosReporteAdapter = new DatosReporteAdapter(getActivity(),R.layout.report_sent);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.invalidateViews();
        listView.setAdapter(datosReporteAdapter);
        button = (FloatingActionButton)view.findViewById(R.id.floatingReport);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.displayList, new Reporte());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonArray = SessionManager.getInstance().getSession().getReportArray();
                            int count = 0;
                            String paciente, aseguradora, centro, doctor, observaciones,cirujano,nss, bandeja,fecha;
                            while (count < jsonArray.length()){
                                JSONObject jsonObject = jsonArray.getJSONObject(count);
                                paciente = jsonObject.getString("pacient_name");
                                aseguradora = jsonObject.getString("insurance_name");
                                nss = jsonObject.getString("nss");
                                bandeja = jsonObject.getString("bandeja_id");
                                doctor = jsonObject.getString("doctor");
                                cirujano = jsonObject.getString("surgeon_name");
                                centro= jsonObject.getString("center_name");
                                fecha = jsonObject.getString("date");

                                if(jsonObject.getString("observations").equals("")){
                                    observaciones = "";
                                }
                                else {
                                    observaciones = jsonObject.getString("observations");
                                }

                                DatosReporte datosReporte = new DatosReporte(paciente,aseguradora,centro,nss, doctor, bandeja, cirujano, observaciones,fecha);
                                datosReporteAdapter.add(datosReporte);
                                count++;


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
        );
        thread.start();
        return view;
    }
}
