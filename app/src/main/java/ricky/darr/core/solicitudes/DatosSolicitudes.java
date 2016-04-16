package ricky.darr.core.solicitudes;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ricky.darr.core.webservice_controller.JsonParser;
import sessionManager.SessionManager;

/**
 * Created by Ricky on 9/8/15.
 */
public class DatosSolicitudes {
     String centro,paciente,ars;
     String cedula;
    String nss;
    String telefono_paciente;
    String autorizacion;
    String fecha_cirugia;
    String procedimiento;
    String cirujano_name;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    String total;
    String urlSolicitud = "http://54.218.36.180:2000/api/request?";
    String urlProcedimiento ="http://54.218.36.180:2000/api/procedure";
    String urlBandejasbyid ="http://54.218.36.180:2000/api/plates?";
    String urlBandejas ="http://54.218.36.180:2000/api/plates";
    private JsonParser jsonParser;


    public String getBandeja() {
        return bandeja;
    }

    public void setBandeja(String bandeja) {
        this.bandeja = bandeja;
    }

    String bandeja;

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getArs() {
        return ars;
    }

    public void setArs(String ars) {
        this.ars = ars;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getTelefono_paciente() {
        return telefono_paciente;
    }

    public void setTelefono_paciente(String telefono_paciente) {
        this.telefono_paciente = telefono_paciente;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    public String getFecha_cirugia() {
        return fecha_cirugia;
    }

    public void setFecha_cirugia(String fecha_cirugia) {
        this.fecha_cirugia = fecha_cirugia;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getCirujano_name() {
        return cirujano_name;
    }

    public void setCirujano_name(String cirujano_name) {
        this.cirujano_name = cirujano_name;
    }


    public DatosSolicitudes(String paciente, String ars,  String procedimiento, String cedula, String telefono_paciente, String autorizacion, String fecha_cirugia, String centro, String cirujano_name,String bandeja, String total,String nss,String status)
    {
        this.setPaciente(paciente);
        this.setArs(ars);
        this.setCedula(cedula);
        this.setProcedimiento(procedimiento);
        this.setProcedimiento(procedimiento);
        this.setTelefono_paciente(telefono_paciente);
        this.setAutorizacion(autorizacion);
        this.setFecha_cirugia(fecha_cirugia);
        this.setCentro(centro);
        this.setCirujano_name(cirujano_name);
        this.setBandeja(bandeja);
        this.setTotal(total);
        this.setStatus(status);
        this.setNss(nss);
    }

    public DatosSolicitudes(String paciente, String ars,  String procedimiento, String cedula, String telefono_paciente, String autorizacion, String fecha_cirugia, String centro, String cirujano_name,String bandeja,String nss)
    {
        this.setPaciente(paciente);
        this.setArs(ars);
        this.setCedula(cedula);
        this.setProcedimiento(procedimiento);
        this.setProcedimiento(procedimiento);
        this.setTelefono_paciente(telefono_paciente);
        this.setAutorizacion(autorizacion);
        this.setFecha_cirugia(fecha_cirugia);
        this.setCentro(centro);
        this.setCirujano_name(cirujano_name);
        this.setBandeja(bandeja);
        this.setNss(nss);
    }

    public DatosSolicitudes(){}

    public JSONObject sendSolicitud()
    {
        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("pacient_name",paciente));
        dataToSend.add(new BasicNameValuePair("document", cedula ));
        dataToSend.add(new BasicNameValuePair("nss", nss + ""));
        dataToSend.add(new BasicNameValuePair("pacient_tel",telefono_paciente));
        dataToSend.add(new BasicNameValuePair("insurance_name",ars));
        dataToSend.add(new BasicNameValuePair("authorization", autorizacion));
        dataToSend.add(new BasicNameValuePair("procedure_name", procedimiento));
        dataToSend.add(new BasicNameValuePair("surgery_date", fecha_cirugia));
        dataToSend.add(new BasicNameValuePair("center_name", centro));
        dataToSend.add(new BasicNameValuePair("surgeon_name", cirujano_name));
        dataToSend.add(new BasicNameValuePair("item_manuales", bandeja));
        dataToSend.add(new BasicNameValuePair("user", SessionManager.getInstance().getSession().getUsername()));
        dataToSend.add(new BasicNameValuePair("token", SessionManager.getInstance().getSession().getToken()));

        JSONObject json = JsonParser.postJSONFromUrl(urlSolicitud, dataToSend);
        return json;
    }

    public void getProcedure(){
        List<NameValuePair> params = new ArrayList<>();
        JSONArray json = JsonParser.getJSONArrFromUrl(urlProcedimiento, params);
        SessionManager.getInstance().getSession().setProcedureArray(json);
    }

    public void getRequest(){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("user",SessionManager.getInstance().getSession().getUsername()));
        JSONArray json = JsonParser.getJSONArrFromUrl(urlSolicitud, params);
        SessionManager.getInstance().getSession().setRequestArray(json);
    }

    public JSONArray getPlatesByuser(String procedimiento){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("procedure_id",procedimiento));
        JSONArray json = JsonParser.getJSONArrFromUrl(urlBandejasbyid, params);
        SessionManager.getInstance().getSession().setBandejaArray(json);
        return json;
    }

    public JSONArray getPlates(){
        List<NameValuePair> params = new ArrayList<>();
        JSONArray json = JsonParser.getJSONArrFromUrl(urlBandejas, params);
        return json;
    }



}
