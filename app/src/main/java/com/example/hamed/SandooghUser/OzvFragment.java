package com.example.hamed.SandooghUser;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OzvFragment extends Fragment {

    View v;
    DecimalFormat formatter=new DecimalFormat("#,###,###");

    RecyclerView recyclerView;
    public TextView mojodi;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    public List<PersonUtils> personUtilsList;
    String username;

    public OzvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_ozv, container, false);
        recyclerView=(RecyclerView)v.findViewById(R.id.recycleViewOzv);
        mojodi=(TextView)v.findViewById(R.id.mojodi);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        personUtilsList = new ArrayList<>();
        mAdapter = new CustomRecyclerAdapter(getActivity(), personUtilsList);
        recyclerView.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username=SharedPrefManager.getInstance(getActivity()).getUsername();
        username=username.replaceAll("\\d", "").trim();
        username=username.replace(" ","%20");
        getOzv();



    }

    public void getOzv(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST, Constans.GET_USER+"?string="+username, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        personUtilsList.clear();
                        int mojodi1=0;
                        for(int i = 0; i < response.length(); i++){
                            PersonUtils personUtils = new PersonUtils();
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                personUtils.setCode(jsonObject.getInt("id"));
                                personUtils.setid(i+1);
                                personUtils.setusername(jsonObject.getString("username"));
                                personUtils.setmojodi(jsonObject.getInt("mojodi"));
                                personUtils.setwam(jsonObject.getInt("wam"));
                                mojodi1+=jsonObject.getInt("mojodi");
                                personUtils.setchecked(false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            personUtilsList.add(personUtils);
                        }
                        mojodi.setText(String.valueOf("موجودی="+formatter.format(mojodi1)));
                        mAdapter.notifyDataSetChanged();
                        // recyclerView.scrollToPosition(0);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley Error: ", error.toString());
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }


}
