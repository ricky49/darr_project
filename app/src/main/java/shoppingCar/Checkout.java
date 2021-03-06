package ShoppingCar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ricky.darr.core.R;
import sessionManager.SessionManager;

public class Checkout extends Fragment {

    CheckoutAdapter checkoutAdapter;
    JSONArray carList;
    JSONArray products;
    ListView listView;
    Button payment;
    TextView generalTotal;
    android.support.v4.app.FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.displaycar, container, false);
        generalTotal = (TextView) view.findViewById(R.id.generalTotal);
        checkoutAdapter = new CheckoutAdapter(getActivity(),R.layout.activity_screen1,generalTotal,getActivity());
        listView = (ListView) view.findViewById(R.id.listCar);
        listView.setAdapter(checkoutAdapter);
        payment =(Button)view.findViewById(R.id.Payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Confirmacion de Pedido")
                        .setMessage("Estas seguro que deseas realizar esta compra?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fragmentManager = getFragmentManager();
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.displayCar, new Payment());
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        carContent();
        return view;
    }

    public void carContent(){
        final Thread thread = new Thread(

                new Runnable() {
                    ArrayList<String> carId = new ArrayList<>();
                    @Override
                    public void run() {
                        try {

                            carList = SessionManager.getInstance().getSession().getCarProduct();
                            products = SessionManager.getInstance().getSession().getProductArray();
                            int count = 0;
                            int x=0;
                            String product_name,product_code,product_price,product_id,product_quantity,_id,total;
                            while (count < carList.length()){
                                JSONObject jsonObject = carList.getJSONObject(count);
                                _id = jsonObject.getString("_id");
                                carId.add(_id);
                                SessionManager.getInstance().getSession().setCarId(carId);
                                product_id = jsonObject.getString("product_id");
                                product_quantity = jsonObject.getString("quantity");
                                count++;
                                for(int i=0;i<products.length();i++){
                                    JSONObject jsonObject2 = products.getJSONObject(i);
                                    if(product_id.equals(jsonObject2.getString("_id"))){
                                        product_code = jsonObject2.getString("CODIGO");
                                        product_name = jsonObject2.getString("DETALLE");
                                        product_price = jsonObject2.getString("PRECIO");
                                        total = String.valueOf(Integer.valueOf(product_price) * Integer.valueOf(product_quantity));
                                        x += Integer.valueOf(total);
                                        final ModelProducts modelProducts= new ModelProducts(product_name,product_code,product_price,product_quantity,total);
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                checkoutAdapter.add(modelProducts);
                                            }
                                        });

                                    }
                                }

                            }
                            final int result = x;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    generalTotal.setText(" $ "+String.valueOf(result));
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        thread.start();
    }
    public void getCart(){


        Thread thread = new Thread(new Runnable() {
            String _id ="";
            ArrayList<String> carId = new ArrayList<>();
            @Override
            public void run() {
                int count =0;
                ModelProducts modelProducts = new ModelProducts();
                modelProducts.getCar();
                try {

                    JSONArray carList = SessionManager.getInstance().getSession().getCarProduct();
                    while(count < carList.length())
                    {
                        JSONObject jsonObject = carList.getJSONObject(count);
                        _id = jsonObject.getString("_id");
                        carId.add(_id);
                        SessionManager.getInstance().getSession().setCarId(carId);
                        count++;

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
    }


    @Override
    public void onResume() {
        getCart();
        generalTotal = (TextView) view.findViewById(R.id.generalTotal);
        checkoutAdapter = new CheckoutAdapter(getActivity(),R.layout.activity_screen1,generalTotal,getActivity());
        listView = (ListView) view.findViewById(R.id.listCar);
        listView.setAdapter(checkoutAdapter);
        carContent();
        super.onResume();
    }
}

