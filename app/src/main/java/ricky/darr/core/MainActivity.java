package ricky.darr.core;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ricky.darr.core.report.DatosReporte;
import ricky.darr.core.report.DisplayListView;
import ricky.darr.core.solicitudes.DatosSolicitudes;
import ricky.darr.core.solicitudes.DisplayListViewRequest;
import ricky.darr.core.webservice_controller.TxnThread;
import sessionManager.SessionManager;
import ShoppingCar.ShoppingCar;
import ShoppingCar.Checkout;

public class MainActivity extends AppCompatActivity{

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(),getApplicationContext()));

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = ((CustomAdapter)viewPager.getAdapter()).getFragment(position);

                if(position == 3 && fragment!=null){
                    fragment.onResume();
                }

                if(position == 1 && fragment!=null){
                    fragment.onResume();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out_id:
                SessionManager.getInstance().logout();
                Intent intent = new Intent(this,Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
//            case R.id.refresh:
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class CustomAdapter extends FragmentPagerAdapter {
        private Map<Integer,String> mFragmentTags;
        private FragmentManager mFragmentManager;
        private Context mcontext;

        private String fragments [] = {"REPORTES","SOLICITUDES","PRODUCTOS", "CARRITO"};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
            mFragmentManager = supportFragmentManager;
            mFragmentTags = new HashMap<Integer,String>();
            mcontext = applicationContext;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new DisplayListView();
                case 1:
                    return new DisplayListViewRequest();
                case 2:
                    return new ShoppingCar();
                case 3:
                    return new Checkout();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position){
            Object obj = super.instantiateItem(container,position);
            if(obj instanceof Fragment){
                Fragment f = (Fragment)obj;
                String tag = f.getTag();
                mFragmentTags.put(position,tag);
            }
            return obj;
        }
        public Fragment getFragment (int position){
            String tag = mFragmentTags.get(position);
            if(tag ==null){
                return null;
            }
            return mFragmentManager.findFragmentByTag(tag);
        }
    }

}