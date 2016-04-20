package ricky.darr.core.solicitudes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ricky.darr.core.R;

/**
 * Created by Ricky on 2/21/16.
 */
public class DatosSolicitudAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public DatosSolicitudAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(DatosSolicitudes datosSolicitudes){
        list.add(datosSolicitudes);
        notifyDataSetChanged();
        super.add(datosSolicitudes);
    }

    public int getCount(){
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        DatosSolicitudesHolder datosSolicitudesHolder;
        if(row ==null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.request_sent,parent,false);
            datosSolicitudesHolder = new DatosSolicitudesHolder();
            datosSolicitudesHolder.tx_paciente = (TextView) row.findViewById(R.id.txtPaciente);
            datosSolicitudesHolder.tx_documento = (TextView) row.findViewById(R.id.txtCedula);
            datosSolicitudesHolder.tx_tel = (TextView) row.findViewById(R.id.txtTel);
            datosSolicitudesHolder.tx_ars = (TextView) row.findViewById(R.id.txtARS);
            datosSolicitudesHolder.tx_autorizacion = (TextView) row.findViewById(R.id.txtAutorizacion);
            datosSolicitudesHolder.tx_fecha = (TextView) row.findViewById(R.id.txtFecha);
            datosSolicitudesHolder.tx_centro = (TextView) row.findViewById(R.id.txtCentro);
            datosSolicitudesHolder.tx_cirujano = (TextView) row.findViewById(R.id.txtCirujano);
            datosSolicitudesHolder.tx_procedimiento = (TextView) row.findViewById(R.id.txtProcedimiento);
            datosSolicitudesHolder.tx_bandeja = (TextView) row.findViewById(R.id.txtBandeja);
            datosSolicitudesHolder.tx_nss = (TextView) row.findViewById(R.id.txtNss);
            datosSolicitudesHolder.tx_status = (TextView) row.findViewById(R.id.txtStatus);
            row.setTag(datosSolicitudesHolder);
        }else{
            datosSolicitudesHolder =(DatosSolicitudesHolder)row.getTag();
        }
        DatosSolicitudes datosSolicitudes = (DatosSolicitudes)this.getItem(position);
        datosSolicitudesHolder.tx_paciente.setText(datosSolicitudes.getPaciente());
        datosSolicitudesHolder.tx_documento.setText(datosSolicitudes.getCedula());
        datosSolicitudesHolder.tx_tel.setText(datosSolicitudes.getTelefono_paciente());
        datosSolicitudesHolder.tx_ars.setText(datosSolicitudes.getArs());
        datosSolicitudesHolder.tx_autorizacion.setText(datosSolicitudes.getAutorizacion());
        datosSolicitudesHolder.tx_fecha.setText(datosSolicitudes.getFecha_cirugia());
        datosSolicitudesHolder.tx_centro.setText(datosSolicitudes.getCentro());
        datosSolicitudesHolder.tx_cirujano.setText(datosSolicitudes.getCirujano_name());
        datosSolicitudesHolder.tx_procedimiento.setText(datosSolicitudes.getProcedimiento());
        datosSolicitudesHolder.tx_bandeja.setText(datosSolicitudes.getBandeja());
        datosSolicitudesHolder.tx_nss.setText(datosSolicitudes.getNss());
        datosSolicitudesHolder.tx_status.setText(datosSolicitudes.getStatus());

        if(position%2==0){
            row.setBackgroundColor(Color.parseColor("#B6B6B6"));

        }else {
            row.setBackgroundColor(Color.parseColor("#F5F5F5"));
        }

        return row;
    }

    static class DatosSolicitudesHolder{
        TextView tx_paciente,tx_documento,tx_tel,tx_ars,tx_autorizacion,tx_fecha,tx_centro,tx_cirujano,tx_procedimiento,tx_bandeja,tx_nss,tx_status;
    }
}
