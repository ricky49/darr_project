package ricky.darr.core.report;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ricky.darr.core.webservice_controller.JsonParser;
import sessionManager.SessionManager;

/**
 * Created by Ricky on 9/3/15.
 */
public class DatosReporte {
    private JsonParser jsonParser;

    private String paciente, aseguradora, centro, doctor, observaciones, cirujano,nss, bandeja,fecha;
    String urlreporte = "http://54.218.36.180:2000/api/report?";
    String centers = "http://54.218.36.180:2000/api/center";
    String insurance = "http://54.218.36.180:2000/api/insurance";


    public DatosReporte(String paciente, String aseguradora, String centro, String nss, String doctor, String bandeja, String cirujano, String observaciones, String fecha) {
        this.setPaciente(paciente);
        this.setAseguradora(aseguradora);
        this.setCentro(centro);
        this.setNss(nss);
        this.setDoctor(doctor);
        this.setBandeja(bandeja);
        this.setCirujano(cirujano);
        this.setObservaciones(observaciones);
        this.setFecha(fecha);

    }

    public DatosReporte(String paciente, String aseguradora, String centro, String nss, String doctor, String bandeja, String cirujano, String observaciones) {
        this.setPaciente(paciente);
        this.setAseguradora(aseguradora);
        this.setCentro(centro);
        this.setNss(nss);
        this.setDoctor(doctor);
        this.setBandeja(bandeja);
        this.setCirujano(cirujano);
        this.setObservaciones(observaciones);
    }

    public DatosReporte() {

    }

    public JSONObject sendReport()
    {
        String postreporte = "http://54.218.36.180:2000/api/report";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("pacient_name", paciente));
        params.add(new BasicNameValuePair("insurance_name", aseguradora));
        params.add(new BasicNameValuePair("nss", nss + ""));
        params.add(new BasicNameValuePair("bandeja_id", bandeja + ""));
        params.add(new BasicNameValuePair("doctor", doctor));
        params.add(new BasicNameValuePair("surgeon_name", cirujano + ""));
        params.add(new BasicNameValuePair("observations", observaciones));
        params.add(new BasicNameValuePair("center_name", centro));
        params.add(new BasicNameValuePair("token", SessionManager.getInstance().getSession().getToken()));
        params.add(new BasicNameValuePair("user", SessionManager.getInstance().getSession().getUsername()));

        JSONObject json = JsonParser.postJSONFromUrl(postreporte, params);
        return json;

    }

    public JSONArray getCenters(){
        List<NameValuePair> params = new ArrayList<>();
        JSONArray json = JsonParser.getJSONArrFromUrl(centers, params);
        return json;
    }

    public JSONArray getARS(){
        List<NameValuePair> params = new ArrayList<>();
        JSONArray json = JsonParser.getJSONArrFromUrl(insurance, params);
        return json;
    }
    public void getReports(){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("user",SessionManager.getInstance().getSession().getUsername()));
        JSONArray json = JsonParser.getJSONArrFromUrl(urlreporte, params);
        SessionManager.getInstance().getSession().setReportArray(json);
    }


    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getAseguradora() {
        return aseguradora;
    }

    public void setAseguradora(String aseguradora) {
        this.aseguradora = aseguradora;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCirujano() {
        return cirujano;
    }

    public void setCirujano(String cirujano) {
        this.cirujano = cirujano;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getBandeja() {
        return bandeja;
    }

    public void setBandeja(String bandeja) {
        this.bandeja = bandeja;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}


