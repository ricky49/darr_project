package ShoppingCar;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ricky.darr.core.R;
import ricky.darr.core.webservice_controller.JsonParser;
import sessionManager.SessionManager;

/**
 * Created by Ricky on 2/23/16.
 */
public class ProductsAdapter extends ArrayAdapter {
    List list = new ArrayList();
    String urlCar = "http://54.218.36.180:2000/api/cars";
    ArrayList<Integer>product = new ArrayList<>();
    int value = 0;
    private String arr [];
    ShoppingCar shoppingCar = new ShoppingCar();


    public ProductsAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(ModelProducts modelProducts){
        super.add(modelProducts);
        list.add(modelProducts);
        notifyDataSetChanged();
    }

    public int getCount(){
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        arr =new String[getCount()];
        View row;
        row = convertView;
        final View vi;
        final ProductHolder productHolder;
        if(row ==null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.productlist,parent,false);
            vi = layoutInflater.inflate(R.layout.activity_shopping_car,parent,false);
            final TextView textView = (TextView)vi.findViewById(R.id.carlength);
            productHolder = new ProductHolder();
            productHolder.tx_productName = (TextView) row.findViewById(R.id.txtProducto);
            productHolder.tx_productPrice = (TextView) row.findViewById(R.id.txtPrecio);
            productHolder.tx_productCode = (TextView) row.findViewById(R.id.txtCodigo);
            productHolder.etcantidad = (EditText) row.findViewById(R.id.etCantidad);
            productHolder.etcantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    arr[productHolder.ref] = s.toString();
                }
            });
            productHolder.btagregar =(Button) row.findViewById(R.id.agregar);
            productHolder.btagregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                sendToCar(SessionManager.getInstance().getSession().getProductId().get(position), productHolder.etcantidad.getText().toString());
                                ModelProducts modelProducts = new ModelProducts();
                                modelProducts.getCar();
                            }
                        });
                    thread.start();
                    try {
                        thread.join();
                        value = SessionManager.getInstance().getSession().getCarProduct().length();
                        textView.setText(String.valueOf(value));
                        vi.setVisibility(vi.VISIBLE);
                        vi.invalidate();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getContext(), "agregado", Toast.LENGTH_SHORT).show();
                }
            });
            row.setTag(productHolder);
        }else{
            productHolder =(ProductHolder)row.getTag();
        }
        productHolder.ref =position;
        ModelProducts modelProducts = (ModelProducts)this.getItem(position);
        productHolder.tx_productName.setText(modelProducts.getProductName());
        productHolder.tx_productCode.setText(modelProducts.getProductCode());
        productHolder.tx_productPrice.setText(modelProducts.getProductPrice());
        productHolder.etcantidad.setText(arr[position]);

        if(position%2==0){
            row.setBackgroundColor(Color.parseColor("#B6B6B6"));

        }else {
            row.setBackgroundColor(Color.parseColor("#F5F5F5"));
        }

        return row;
    }

    static class ProductHolder{
        TextView tx_productName,tx_productPrice,tx_productCode;
        Button btagregar;
        EditText etcantidad;
        int ref;
    }

    private void sendToCar(String id,String quantity){

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("user_id", SessionManager.getInstance().getSession().getUser_id()));
        params.add(new BasicNameValuePair("product_id", id));
        params.add(new BasicNameValuePair("quantity", quantity));
        JSONObject json = JsonParser.postJSONFromUrl(urlCar, params);
    }



}
