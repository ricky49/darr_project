package ricky.darr.core.report;

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
 * Created by Ricky on 2/18/16.
 */
public class DatosReporteAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public DatosReporteAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(DatosReporte datosReporte){
        super.add(datosReporte);
        list.add(datosReporte);
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
        DatosReporteHolder datosReporteHolder;
        if(row ==null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.report_sent,parent,false);
            datosReporteHolder = new DatosReporteHolder();
            datosReporteHolder.tx_paciente = (TextView) row.findViewById(R.id.tvPaciente);
            datosReporteHolder.tx_seguro = (TextView) row.findViewById(R.id.tvSeguro);
            datosReporteHolder.tx_nss = (TextView) row.findViewById(R.id.tvNSS);
            datosReporteHolder.tx_bandeja = (TextView) row.findViewById(R.id.tvBandeja);
            datosReporteHolder.tx_fecha = (TextView) row.findViewById(R.id.tvFecha);
            datosReporteHolder.tx_centro = (TextView) row.findViewById(R.id.tvCentro);
            datosReporteHolder.tx_doctor = (TextView) row.findViewById(R.id.tvDoctor);
            datosReporteHolder.tx_cirujano = (TextView) row.findViewById(R.id.tvCirujano);
            datosReporteHolder.tx_observaciones = (TextView) row.findViewById(R.id.tvObservaciones);
            row.setTag(datosReporteHolder);
        }else{
            datosReporteHolder =(DatosReporteHolder)row.getTag();
        }
        DatosReporte datosReporte = (DatosReporte)this.getItem(position);
        datosReporteHolder.tx_paciente.setText(datosReporte.getPaciente());
        datosReporteHolder.tx_seguro.setText(datosReporte.getAseguradora());
        datosReporteHolder.tx_nss.setText(datosReporte.getNss());
        datosReporteHolder.tx_bandeja.setText(datosReporte.getBandeja());
        datosReporteHolder.tx_fecha.setText(datosReporte.getFecha());
        datosReporteHolder.tx_centro.setText(datosReporte.getCentro());
        datosReporteHolder.tx_doctor.setText(datosReporte.getDoctor());
        datosReporteHolder.tx_cirujano.setText(datosReporte.getCirujano());
        datosReporteHolder.tx_observaciones.setText(datosReporte.getObservaciones());

        if(position%2==0){
            row.setBackgroundColor(Color.parseColor("#B6B6B6"));

        }else {
            row.setBackgroundColor(Color.parseColor("#F5F5F5"));
        }

        return row;
    }

    static class DatosReporteHolder{
        TextView tx_paciente,tx_seguro,tx_nss,tx_bandeja,tx_fecha,tx_centro,tx_doctor,tx_cirujano,tx_observaciones;
    }
}
