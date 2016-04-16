package ShoppingCar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import ricky.darr.core.webservice_controller.JsonParser;
import sessionManager.SessionManager;

/**
 * Created by Ricky on 1/25/16.
 */
public class ModelProducts {
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    String  productName;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String quantity;

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    String productCode;
    String  productPrice;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    String total;

    public String getTotalGeneral() {
        return totalGeneral;
    }

    public void setTotalGeneral(String totalGeneral) {
        this.totalGeneral = totalGeneral;
    }

    String totalGeneral="";
    private JsonParser jsonParser;
    String products = "http://54.218.36.180:2000/api/products";
    String car = "http://54.218.36.180:2000/api/cars?";
    public ModelProducts(String productName, String productCode, String productPrice){
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCode = productCode;
    }

    public ModelProducts(String productName, String productCode, String productPrice,String cantidad,String total){
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCode = productCode;
        this.quantity = cantidad;
        this.total = total;
    }

    public ModelProducts(){}

    public void getProducts(){
        List<NameValuePair> params = new ArrayList<>();
        JSONArray json = JsonParser.getJSONArrFromUrl(products, params);
        SessionManager.getInstance().getSession().setProductArray(json);
    }


    public void getCar(){
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("user_id",SessionManager.getInstance().getSession().getUser_id()));
        JSONArray json = JsonParser.getJSONArrFromUrl(car, params);
        SessionManager.getInstance().getSession().setCarProduct(json);
    }


}
