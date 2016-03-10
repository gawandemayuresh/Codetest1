package com.sh.owner.codetest_2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private ProgressDialog pDialog;
    private Spinner spinner;
    private Toolbar toolbar;
    private PlaceholderFragment placeholder_fragment;
    private ArrayList<String> al_name,al_fromcarname,al_fromcardist,al_fromtrainname,al_fromtraindist,al_lat,al_lang;
    public static final String Tag_name="name";
    public static final String Tag_fromcarname="fromcarname";
    public static final String Tag_fromtrainname="fromtrainname";
    public static final String Tag_car="car";
    public static final String Tag_train="train";
    public static final String Tag_url="http://express-it.optusnet.com.au/sample.json";
    public static final String Tag_latitude="latitude";
    public static final String Tag_longitude="longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        call_ws();
         spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                placeholder_fragment = new PlaceholderFragment();
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString(Tag_name, spinner.getItemAtPosition(position).toString());
                bundle.putString(Tag_fromcarname,  al_fromcarname.get(position).toString()+" - ");
                bundle.putString(Tag_car,  al_fromcardist.get(position).toString());
                bundle.putString(Tag_fromtrainname, al_fromtrainname.get(position).toString()+" - ");
                bundle.putString(Tag_train, al_fromtraindist.get(position).toString());
                bundle.putString(Tag_latitude, al_lat.get(position).toString());
                bundle.putString(Tag_longitude, al_lang.get(position).toString());
                placeholder_fragment.setArguments(bundle);
                ft.replace(R.id.container, placeholder_fragment, "MyFragmentTag");
                ft.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
   }

    private void call_ws() {
        String tag_json_arry = "json_array_req";
         pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(Tag_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String res = response.toString();
                        if(res!=null){
                        try {
                            al_name=new ArrayList<String>();
                            al_fromcarname=new ArrayList<String>();
                            al_fromcardist=new ArrayList<String>();
                            al_fromtrainname=new ArrayList<String>();
                            al_fromtraindist=new ArrayList<String>();
                            al_lat=new ArrayList<String>();
                            al_lang=new ArrayList<String>();

                            JSONArray ja= new JSONArray(res);
                            for (int i=0;i<ja.length();i++){
                                JSONObject jobj = ja.getJSONObject(i);
                                String name=jobj.getString(Tag_name);
                                     al_name.add(name);

                                JSONObject fromc = jobj.getJSONObject("fromcentral");
                                if(fromc.has(Tag_car)){
                                String cardist = fromc.getString(Tag_car);
                                    al_fromcarname.add(Tag_car);
                                    al_fromcardist.add(cardist);
                                }
                                else{
                                    al_fromcarname.add(Tag_car);
                                    al_fromcardist.add("");
                                }
                                if(fromc.has(Tag_train)){
                                    String traindist = fromc.getString(Tag_train);
                                    al_fromtrainname.add(Tag_train);
                                    al_fromtraindist.add(traindist);
                                }
                                else{
                                    al_fromtrainname.add(Tag_train);
                                    al_fromtraindist.add("");
                                }

                                JSONObject loc=jobj.getJSONObject("location");
                                String lat=loc.getString(Tag_latitude);
                                al_lat.add(lat);
                                String lang=loc.getString(Tag_longitude);
                                al_lang.add(lang);
                            }
                            spinner.setAdapter(new MyAdapter(
                                    toolbar.getContext(),al_name
                                    ));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        }
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }
        });
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, tag_json_arry);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
          int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, ArrayList<String> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));
            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }


    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private Button button;
        private TextView textView,textView2,textView3;
        private String selectedlat,selectedlang,selectedname;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
             selectedname = getArguments().getString(Tag_name);
            String selectedfromcarname = getArguments().getString(Tag_fromcarname);
            String selectedfromcardist = getArguments().getString(Tag_car);
            String selectedfromtrainname = getArguments().getString(Tag_fromtrainname);
            String selectedfromtraindist = getArguments().getString(Tag_train);
             selectedlat = getArguments().getString(Tag_latitude);
             selectedlang = getArguments().getString(Tag_longitude);

            button=(Button)rootView.findViewById(R.id.button);
            textView=(TextView)rootView.findViewById(R.id.textView);
            textView2=(TextView)rootView.findViewById(R.id.textView2);
            textView3=(TextView)rootView.findViewById(R.id.textView3);
            textView.setText(selectedname);
            textView2.setText("Mode of Transport :");

            textView3.setText(selectedfromcarname+"  "+selectedfromcardist+"\n"+selectedfromtrainname+"  "+selectedfromtraindist);
           // textView3.setText(Tag_latitude+"  "+selectedlat+"\n"+Tag_longitude+"  "+selectedlang);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), MapsActivity.class);
                    i.putExtra(Tag_latitude, selectedlat);
                    i.putExtra(Tag_longitude,  selectedlang);
                    i.putExtra(Tag_name,  selectedname);
                    startActivity(i);
                }
            });    return rootView;
        }
    }
}
