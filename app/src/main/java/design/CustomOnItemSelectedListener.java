package design;

import android.view.View;
import android.widget.AdapterView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ricky.darr.core.R;
import ricky.darr.core.report.DatosReporte;
import ricky.darr.core.solicitudes.DatosSolicitudes;
import ricky.darr.core.webservice_controller.JsonParser;
import sessionManager.SessionManager;

/**
 * Created by Ricky on 2/2/16.
 */
public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    public CustomOnItemSelectedListener(){}
    DatosSolicitudes datosSolicitudes;
    private JsonParser jsonParser;
    String urlBandejas ="http://54.218.36.180:2000/api/plates?";
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.spCentro:
                SessionManager.getInstance().getSession().setSelectedCenter(parent.getItemAtPosition(position).toString());
                break;
            case R.id.spArs:
              SessionManager.getInstance().getSession().setSelectedArs(parent.getItemAtPosition(position).toString());
                break;

            case R.id.spBandeja:
                String plate = SessionManager.getInstance().getSession().getPlate_id().get(position);

                for(int i =0; i<SessionManager.getInstance().getSession().getBandeja_id().size();i++)
                {
                    if(plate.equals(SessionManager.getInstance().getSession().getBandeja_id().get(i))){
                        String _id = SessionManager.getInstance().getSession().getBandeja_id().get(i+1);
                        SessionManager.getInstance().getSession().setSelectedPlate(_id);
                    }

                }


                break;
            case R.id.etprocedimientoID:
                final int current_position = position;
                final ArrayList<String> plateList = new ArrayList<>();
                String procedure = SessionManager.getInstance().getSession().getProcedure_id().get(position);
                SessionManager.getInstance().getSession().setSelectedProcedure(procedure);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getPlates(SessionManager.getInstance().getSession().getProcedure_id().get(current_position));
                        JSONArray plates = SessionManager.getInstance().getSession().getBandejaArray();
                        try {
                            for (int i = 0; i < plates.length(); i++) {
                                JSONObject jplates = plates.getJSONObject(i);
                                String plate_id = String.valueOf(jplates.getString("plate_id"));
                                plateList.add(plate_id);
                                SessionManager.getInstance().getSession().setPlate_id(plateList);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getPlates(String procedimiento){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("procedure_id",procedimiento));
        JSONArray json = JsonParser.getJSONArrFromUrl(urlBandejas, params);
        SessionManager.getInstance().getSession().setBandejaArray(json);
    }
}
