package ShoppingCar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
 * Created by Ricky on 3/14/16.
 */
public class CheckoutAdapter extends ArrayAdapter{
    List list = new ArrayList();
    String urlCar = "http://54.218.36.180:2000/api/cars/";
    ArrayList<Integer>product = new ArrayList<>();
    int value = 0;
    Activity activity;
    TextView general;

    public CheckoutAdapter(Context context, int resource,TextView generalTotal, Activity activity) {
        super(context, resource);
        this.activity = activity;
        this.general = generalTotal;
    }

    public void add(ModelProducts modelProducts){
        super.add(modelProducts);
        list.add(modelProducts);
        notifyDataSetChanged();
       // value += Integer.valueOf(modelProducts.getQuantity().toString()) * Integer.valueOf(modelProducts.getProductPrice().toString());

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
        View row;
        row = convertView;
        final ProductHolder productHolder;
        if(row ==null){
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.activity_screen1,parent,false);
            productHolder = new ProductHolder();
            productHolder.tx_productName = (TextView) row.findViewById(R.id.carProducto);
            productHolder.tx_productPrice = (TextView) row.findViewById(R.id.carPrecio);
            productHolder.tx_productCode = (TextView) row.findViewById(R.id.carCodigo);
            productHolder.tx_cantidad = (TextView) row.findViewById(R.id.carCantidad);
            productHolder.tx_total =(TextView)row.findViewById(R.id.carTotal);
            productHolder.btremover =(Button) row.findViewById(R.id.bRemove);
            productHolder.btremover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                           deleteCar(String.valueOf(SessionManager.getInstance().getSession().getCarId().get(productHolder.ref)));
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                value = Integer.valueOf(general.getText().toString().substring(3));
                                int newTotal = value - Integer.valueOf(productHolder.tx_total.getText().toString().substring(2));
                                general.setText(String.valueOf(" $ "+newTotal));
                                //general.invalidate();
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    list.remove(productHolder.ref);
                    notifyDataSetChanged();
                    Toast.makeText(getContext(), "Removido", Toast.LENGTH_SHORT).show();
                }
            });
            row.setTag(productHolder);
        }else{
            productHolder =(ProductHolder)row.getTag();
        }
        productHolder.ref=position;
        ModelProducts modelProducts = (ModelProducts)this.getItem(position);
        productHolder.tx_productName.setText(modelProducts.getProductName());
        productHolder.tx_productCode.setText(modelProducts.getProductCode());
        productHolder.tx_productPrice.setText("$ "+ modelProducts.getProductPrice());
        productHolder.tx_cantidad.setText(modelProducts.getQuantity());
        productHolder.tx_total.setText("$ "+ modelProducts.getTotal());

        if(position%2==0){
            row.setBackgroundColor(Color.parseColor("#B6B6B6"));

        }else {
            row.setBackgroundColor(Color.parseColor("#F5F5F5"));
        }
        return row;
    }

    static class ProductHolder{
        TextView tx_productName,tx_productPrice,tx_productCode,tx_cantidad,tx_total;
        Button btremover;
        int ref;
    }

    private void deleteCar(String id) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("", id));
        String url = urlCar+id;
        JsonParser.deleteJSONFromUrl(url, params);
    }
}

