package ShoppingCar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ricky.darr.core.R;
import ricky.darr.core.Register;
import sessionManager.SessionManager;

public class ShoppingCar extends Fragment{
    ProductsAdapter productsAdapter;
    ListView listView;
    Button bViewCart;
    JSONArray products;
    TextView carlenght;
    android.support.v4.app.FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    View view;
    ViewGroup viewGroup;


    public View onCreateView (LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_shopping_car, container, false);
        viewGroup = container;
        productsAdapter = new ProductsAdapter(getActivity(),R.layout.productlist);
        listView = (ListView) view.findViewById(R.id.listProduct);
        listView.setAdapter(productsAdapter);
        carlenght = (TextView)view.findViewById(R.id.carlength);
        carlenght.setText(String.valueOf(SessionManager.getInstance().getSession().getCarProduct().length()));
        bViewCart = (Button) view.findViewById(R.id.bViewCar);
        bViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.productContainer, new Checkout());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        final Thread thread = new Thread(

                new Runnable() {
                    @Override
                    public void run() {
                        final ArrayList<String> productId = new ArrayList<>();
                        try {
                            products = SessionManager.getInstance().getSession().getProductArray();
                            int count = 0;
                            String product_name,product_code,product_price,product_id;
                            while (count < products.length()){
                                JSONObject jsonObject = products.getJSONObject(count);
                                product_id = jsonObject.getString("_id");
                                product_code = jsonObject.getString("CODIGO");
                                product_name = jsonObject.getString("DETALLE");
                                product_price = jsonObject.getString("PRECIO");

                                productId.add(product_id);
                                SessionManager.getInstance().getSession().setProductId(productId);
                                ModelProducts modelProducts= new ModelProducts(product_name,product_code,product_price);

                                productsAdapter.add(modelProducts);
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

