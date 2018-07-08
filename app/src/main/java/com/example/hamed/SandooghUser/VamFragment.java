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
public class VamFragment extends Fragment {
    View v;
    DecimalFormat formatter=new DecimalFormat("#,###,###");

    RecyclerView recyclerView;
    public TextView mojodi;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    public List<VamUtils> vamUtilsList;
    String username;


    public VamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_vam, container, false);
        recyclerView=(RecyclerView)v.findViewById(R.id.recycleViewVam);
        mojodi=(TextView)v.findViewById(R.id.mojodi);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        vamUtilsList = new ArrayList<>();
        mAdapter = new VamRecyclerAdapter(getActivity(), vamUtilsList);
        recyclerView.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username=SharedPrefManager.getInstance(getActivity()).getUsername();
        username=username.replaceAll("\\d", "").trim();
        username=username.replace(" ","%20");
        getVam();
    }

    public void getVam(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST, Constans.GET_VAM+"?string="+username, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        vamUtilsList.clear();
                        int mojodi=0;int maghsat,mandavam;
                        for(int i = 0; i < response.length(); i++){
                            VamUtils vamUtils = new VamUtils();
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                vamUtils.setId(i+1);
                                vamUtils.setcode(jsonObject.getInt("id"));
                                vamUtils.setUsername(jsonObject.getString("username"));
                                vamUtils.setMvam(jsonObject.getInt("m_vam"));
                                vamUtils.setTaghsat(jsonObject.getInt("t_aghsat"));
                                vamUtils.setTpardakhtshoda(jsonObject.getInt("t_pardakhtshoda"));
                                maghsat=jsonObject.getInt("m_aghsat");
                                mandavam=jsonObject.getInt("m_vam")-(maghsat*jsonObject.getInt("t_pardakhtshoda"));
                                vamUtils.setMandavam(mandavam);
                                vamUtils.setMaghsat(jsonObject.getInt("m_aghsat"));
                                vamUtils.settozihat(jsonObject.getString("tozihat"));

                                String str[]=jsonObject.getString("tarikh").split("-");
                                CalendarTool calendarTool = new CalendarTool();

                                calendarTool.setGregorianDate(Integer.parseInt(str[0]),Integer.parseInt(str[1]),Integer.parseInt(str[2]));
                                vamUtils.settarikh(calendarTool.getIranianYear()+"/"+calendarTool.getIranianMonth()+"/"+calendarTool.getIranianDay());
                                mojodi+=jsonObject.getInt("mojodi");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            vamUtilsList.add(vamUtils);

                        }
                        mAdapter.notifyDataSetChanged();
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
