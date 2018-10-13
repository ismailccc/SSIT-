package com.ssit.realm.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ssit.realm.R;
import com.ssit.realm.adapters.RealmUserAdapter;
import com.ssit.realm.adapters.UserAdapterNew;
import com.ssit.realm.app.Prefs;
import com.ssit.realm.model.Users;
import com.ssit.realm.realm.RealmController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private UserAdapterNew adapter;
    private Realm realm;
    private LayoutInflater inflater;
    private FloatingActionButton fab;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        //get realm instance
        this.realm = RealmController.with(this).getRealm();

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupRecycler();

        if (!Prefs.with(this).getPreLoad()) {
            GetUsersDetails();
        }

        // refresh the realm instance
        RealmController.with(this).refresh();
        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        setRealmAdapter(RealmController.with(this).getUsers());

        Toast.makeText(this, "Press card item long press to remove item", Toast.LENGTH_LONG).show();

        //add new item
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inflater = MainActivity.this.getLayoutInflater();
                View content = inflater.inflate(R.layout.edit_item, null);
                final EditText editname = (EditText) content.findViewById(R.id.name);
                final EditText editphoneno = (EditText) content.findViewById(R.id.phoneno);
                final EditText editcompanyname = (EditText) content.findViewById(R.id.companyname);
                final EditText editaddress = (EditText) content.findViewById(R.id.address);
                final EditText editemail = (EditText) content.findViewById(R.id.email);
                final EditText editusername = (EditText) content.findViewById(R.id.username);
                final EditText editwebsite = (EditText) content.findViewById(R.id.website);
                final EditText editimageurl = (EditText) content.findViewById(R.id.imageurl);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(content)
                        .setTitle("New User - Kindly fill All details ")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Users user = new Users();
                                user.setId(RealmController.getInstance().getUsers().size() + System.currentTimeMillis());
                                user.setName(editname.getText().toString());
                                user.setUsername(editusername.getText().toString());
                                user.setPhone(editphoneno.getText().toString());
                                user.setCompany(editcompanyname.getText().toString());
                                user.setAddress(editaddress.getText().toString());
                                user.setEmail(editemail.getText().toString());
                                user.setUserimage(editimageurl.getText().toString());

                                if (editname.getText().toString().replace(" ", "").length() == 0 ||
                                        editphoneno.getText().toString().replace(" ", "").length() == 0 ||
                                        editcompanyname.getText().toString().replace(" ", "").length() == 0 ||
                                        editaddress.getText().toString().replace(" ", "").length() == 0 ||
                                        editemail.getText().toString().replace(" ", "").length() == 0 ||
                                        editimageurl.getText().toString().replace(" ", "").length() == 0 ||
                                        editusername.getText().toString().replace(" ", "").length() == 0 ||
                                        editwebsite.getText().toString().replace(" ", "").length() == 0) {
                                    Toast.makeText(MainActivity.this, "Entry not saved,Kindly enter correct data", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Persist your data easily
                                    realm.beginTransaction();
                                    realm.copyToRealm(user);
                                    realm.commitTransaction();

                                    adapter.notifyDataSetChanged();

                                    // scroll the recycler view to bottom
                                    recycler.scrollToPosition(RealmController.getInstance().getUsers().size() - 1);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void setRealmAdapter(RealmResults<Users> users) {

        RealmUserAdapter realmAdapter = new RealmUserAdapter(this.getApplicationContext(), users, true);
        // Set the data and tell the RecyclerView to draw
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    private void setupRecycler() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recycler.setHasFixedSize(true);

        // use a linear layout manager since the cards are vertically scrollable
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);

        // create an empty adapter and add it to the recycler view
        adapter = new UserAdapterNew(this);
        recycler.setAdapter(adapter);
    }

    ArrayList<HashMap<String, String>> userdetails = new ArrayList<>();

    private String userimage[] = {"http://ssitco.sa/wp-content/uploads/2018/09/850_9088.jpg",
            "http://ssitco.sa/wp-content/uploads/2018/09/850_9161.jpg",
            "http://ssitco.sa/wp-content/uploads/2018/09/850_9014.jpg",
            "http://ssitco.sa/wp-content/uploads/2018/09/850_8836.jpg",
            "http://ssitco.sa/wp-content/uploads/2018/09/850_9028.jpg",
            "http://ssitco.sa/wp-content/uploads/2018/09/850_8865.jpg",
            "http://ssitco.sa/wp-content/uploads/2018/09/850_8908.jpg",
            "http://ssitco.sa/wp-content/uploads/2018/09/850_9054.jpg",
            "http://ssitco.sa/wp-content/uploads/2018/09/850_8975.jpg",
            "http://ssitco.sa/wp-content/uploads/2018/09/850_9108.jpg",
            "http://ssitco.sa/wp-content/uploads/2018/09/850_9136-1.jpg",
            "http://ssitco.sa/wp-content/uploads/2018/09/850_8995.jpg",
            "http://ssitco.sa/wp-content/uploads/2018/09/850_8848.jpg",
            "http://ssitco.sa/wp-content/uploads/2018/09/850_8924.jpg"};

    private void setRealmData(String Response, ProgressDialog progressDialog) {

        try {
            JSONArray jsonArray = new JSONArray(Response);

            ArrayList<Users> users = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> temp = new HashMap<String, String>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Users user = new Users();
                user.setId(jsonObject.getInt("id"));
                user.setName(jsonObject.getString("name"));
                user.setUsername(jsonObject.getString("username"));
                user.setPhone(jsonObject.getString("phone"));
                user.setWebsite(jsonObject.getString("website"));
                user.setEmail(jsonObject.getString("email"));

                JSONObject addr = jsonObject.getJSONObject("address");
                StringBuilder address = new StringBuilder();
                address.append(addr.getString("street") + ",");
                address.append(addr.getString("suite") + ",");
                address.append(addr.getString("city") + ",");
                address.append(addr.getString("zipcode") + ".");

                user.setAddress(address.toString());

                JSONObject geo = addr.getJSONObject("geo");
                user.setLat(Double.parseDouble(geo.getString("lat")));
                user.setLang(Double.parseDouble(geo.getString("lng")));


                JSONObject comp = jsonObject.getJSONObject("company");
                StringBuilder company = new StringBuilder();
                company.append(comp.getString("name"));
                user.setCompany(company.toString());


                try {
                    user.setUserimage(userimage[i]);

                } catch (Exception e) {

                }

                //add user data one by one
                users.add(user);

            }

            for (Users b : users) {
                // Persist your data easily
                realm.beginTransaction();
                realm.copyToRealm(b);
                realm.commitTransaction();
            }

            Prefs.with(this).setPreLoad(true);

            RealmController.with(this).refresh();
            // get all persisted objects
            // create the helper adapter and notify data set changes
            // changes will be reflected automatically
            setRealmAdapter(RealmController.with(this).getUsers());

            progressDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }

    }


    public void GetUsersDetails() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait.");
        progressDialog.setTitle("Downloading User Data");
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://jsonplaceholder.typicode.com/users";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    setRealmData(response, progressDialog);

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println(error);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {

                return null;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }


        };

        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        requestQueue.getCache().remove(URL);

    }
}