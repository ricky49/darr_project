package sessionManager;

import android.app.Activity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricky on 12/9/15.
 */
public class TxnSession {
    private String username = "";
    private String password = "";
    private String name = "";
    private String mail = "";
    private String lastname = "";
    private String token = "";
    private String key_sucess = "";
    private List<String> listContents = new ArrayList<String>();
    private String user_id="";
    private ArrayList<String> productName;
    private String [] detalle={};
    private int [] productPrice={};
    private JSONArray requestArray;
    private JSONArray reportArray;
    private JSONArray productArray;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private Activity activity;

    public ArrayList<String> getCarId() {
        return carId;
    }

    public void setCarId(ArrayList<String> carId) {
        this.carId = carId;
    }

    private ArrayList<String> carId;

    public String getSelectedPlate_id() {
        return selectedPlate_id;
    }

    public void setSelectedPlate_id(String selectedPlate_id) {
        this.selectedPlate_id = selectedPlate_id;
    }

    private String selectedPlate_id;

    public JSONArray getCarProduct() {
        return carProduct;
    }

    public void setCarProduct(JSONArray carProduct) {
        this.carProduct = carProduct;
    }

    private JSONArray carProduct;

    public ArrayList<String> getProductId() {
        return productId;
    }

    public void setProductId(ArrayList<String> productId) {
        this.productId = productId;
    }

    private ArrayList<String> productId;

    public ArrayList<String> getBandeja_id() {
        return bandeja_id;
    }

    public void setBandeja_id(ArrayList<String> bandeja_id) {
        this.bandeja_id = bandeja_id;
    }

    private ArrayList<String> bandeja_id;

    public ArrayList<String> getPlate_id() {
        return plate_id;
    }

    public void setPlate_id(ArrayList<String> plate_id) {
        this.plate_id = plate_id;
    }

    private ArrayList<String> plate_id;

    public JSONArray getBandejaArray() {
        return bandejaArray;
    }

    public void setBandejaArray(JSONArray bandejaArray) {
        this.bandejaArray = bandejaArray;
    }

    private JSONArray bandejaArray;
    private String selectedArs = "";
    private String selectedProcedure = "";

    public String getSelectedProcedure() {
        return selectedProcedure;
    }

    public void setSelectedProcedure(String selectedProcedure) {
        this.selectedProcedure = selectedProcedure;
    }

    public String getSelectedArs() {
        return selectedArs;
    }

    public void setSelectedArs(String selectedArs) {
        this.selectedArs = selectedArs;
    }

    public String getSelectedPlate() {
        return selectedPlate;
    }

    public void setSelectedPlate(String selectedPlate) {
        this.selectedPlate = selectedPlate;
    }

    private String selectedPlate = "";

    public String getSelectedCenter() {
        return selectedCenter;
    }

    public void setSelectedCenter(String selectedCenter) {
        this.selectedCenter = selectedCenter;
    }

    private String selectedCenter = "";

    public String getProc_id() {
        return proc_id;
    }

    public void setProc_id(String proc_id) {
        this.proc_id = proc_id;
    }

    private String proc_id ="";
    private JSONArray procedureArray;

    public ArrayList<String> getProcedure_name() {
        return procedure_name;
    }

    public void setProcedure_name(ArrayList<String> procedure_name) {
        this.procedure_name = procedure_name;
    }

    private ArrayList<String> procedure_name;

    public ArrayList<String> getProcedure_id() {
        return procedure_id;
    }

    public void setProcedure_id(ArrayList<String> procedure_id) {
        this.procedure_id = procedure_id;
    }

    private ArrayList<String> procedure_id;

    public JSONArray getProcedureArray() {
        return procedureArray;
    }

    public void setProcedureArray(JSONArray procedureArray) {
        this.procedureArray = procedureArray;
    }

    public JSONArray getRequestArray() {
        return requestArray;
    }

    public void setRequestArray(JSONArray requestArray) {
        this.requestArray = requestArray;
    }

    public JSONArray getProductArray() {
        return productArray;
    }

    public void setProductArray(JSONArray productArray) {
        this.productArray = productArray;
    }

    private ArrayList<String> center= new ArrayList<>();

    public JSONArray getReportArray() {
        return reportArray;
    }

    public void setReportArray(JSONArray reportArray) {
        this.reportArray = reportArray;
    }



    public List<String> getListContents() {
        return listContents;
    }

    public void setListContents(List<String> listContents) {
        this.listContents = listContents;
    }



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<String> getProductName() {
        return productName;
    }

    public void setProductName(ArrayList<String> productName) {
        this.productName = productName;
    }



    public String[] getDetalle() {
        return detalle;
    }

    public void setDetalle(String[] detalle) {
        this.detalle = detalle;
    }


    public int[] getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int[] productPrice) {
        this.productPrice = productPrice;
    }


    public ArrayList<String> getInsurance() {
        return insurance;
    }

    public void setInsurance(ArrayList<String> insurance) {
        this.insurance = insurance;
    }

    private ArrayList<String> insurance;


    public ArrayList<String> getCenter() {
        return center;
    }

    public void setCenter(ArrayList<String> center) {
        this.center = center;
    }


    public TxnSession() {
    }

    public TxnSession getSession() {
        return this;
    }

    public void setSession(TxnSession session) {
        this.setUsername(session.getUsername());
        this.setPassword(session.getPassword());
        this.setMail(session.getMail());
        this.setToken(session.getToken());
        this.setKey_sucess(session.getKey_sucess());
        this.setName(session.getName());
        this.setLastname(session.getLastname());
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setKey_sucess(String key_sucess) {
        this.key_sucess = key_sucess;
    }
    public String getToken() {
        return token;
    }
    public String getKey_sucess() {
        return key_sucess;
    }
    public void setName(String name){this.name = name;}
    public String getName(){return name;}
    public void setLastname(String lastname){this.lastname = lastname;}
    public String getLastname(){return lastname;}
}
