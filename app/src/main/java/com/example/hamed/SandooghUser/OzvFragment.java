package com.example.hamed.SandooghUser;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OzvFragment extends Fragment implements View.OnClickListener{

    View v;
    DecimalFormat formatter=new DecimalFormat("#,###,###");

    RecyclerView recyclerView;
    public TextView mojodi,update;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    public List<PersonUtils> personUtilsList;
    String username;
    ProgressBar progressBar;
    int appversion;
    ProgressDialog bar;
    private static String TAG = "MainActivity";
    String PATH;


    public OzvFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_ozv, container, false);
        recyclerView=(RecyclerView)v.findViewById(R.id.recycleViewOzv);
        mojodi=(TextView)v.findViewById(R.id.mojodi);
        update=(TextView)v.findViewById(R.id.update);
        update.setOnClickListener(this);
        progressBar=(ProgressBar)v.findViewById(R.id.progressbar1);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        personUtilsList = new ArrayList<>();
        mAdapter = new CustomRecyclerAdapter(getActivity(), personUtilsList);
        recyclerView.setAdapter(mAdapter);

        PATH=getActivity().getExternalFilesDir(null)+"/";
        if(isfile()){
            update.setText("نصب");
        }



        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username=SharedPrefManager.getInstance(getActivity()).getUsername();
        username=username.replaceAll("\\d", "").trim();
        username=username.replace(" ","%20");
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            appversion = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        getOzv();
        getMojodi();



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
                                progressBar.setVisibility(View.INVISIBLE);

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

    public void getMojodi(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                Constans.GET_MOJODI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj=new JSONObject(response);
                            int version;
                            version=obj.getInt("version");
                            if(appversion<version){

                                update.setVisibility(View.VISIBLE);

                            }else if(appversion==version){

                                File outputFile = new File(getActivity().getExternalFilesDir(null)+"/","app-debug.apk");
                                if(outputFile.exists()){
                                    outputFile.delete();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                        }


                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }

    @Override
    public void onClick(View v) {
        File outputFile = new File(getActivity().getExternalFilesDir(null)+"/","app-debug.apk");
        if(outputFile.exists()){
            OpenNewVersion(PATH);

        }
        else
            new DownloadNewVersion().execute();


    }

    class DownloadNewVersion extends AsyncTask<String,Integer,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            bar = new ProgressDialog(getActivity());
            bar.setCancelable(false);

            bar.setMessage("Downloading...");

            bar.setIndeterminate(true);
            bar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            bar.setCanceledOnTouchOutside(false);
            bar.show();

        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            bar.setIndeterminate(false);
            bar.setMax(100);
            bar.setProgress(progress[0]);
            String msg = "";
            if(progress[0]>99){

                msg="Finishing... ";

            }else {

                msg="Downloading... "+progress[0]+"%";
            }
            bar.setMessage(msg);

        }
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            bar.dismiss();

            if(result){
                update.setText("نصب");
                Toast.makeText(getActivity(),"Update Done",
                        Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(getActivity(),"Error: Try Again",
                        Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            Boolean flag = false;

            try {


                URL url = new URL("http://192.168.43.135/android/app-debug.apk");

                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                //String PATH = getActivity().getExternalFilesDir(null)+"/";
                File file = new File(PATH);
                file.mkdirs();

                File outputFile = new File(PATH,"app-debug.apk");

                if(outputFile.exists()){
                    outputFile.delete();
                }

                FileOutputStream fos =new FileOutputStream(outputFile);


                InputStream is = c.getInputStream();

                int total_size =c.getContentLength();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                int per = 0;
                int downloaded=0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                    downloaded +=len1;
                    per = (int) (downloaded * 100 / total_size);
                    publishProgress(per);
                }
                fos.close();
                is.close();

                OpenNewVersion(PATH);

                flag = true;
            } catch (Exception e) {
                Log.e(TAG, "Update Error: " + e.getMessage());
                flag = false;
            }
            return flag;

        }

    }

    void OpenNewVersion(String location) {



        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File( location+"app-debug.apk")),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);



    }


    boolean isfile(){
        File outputFile = new File(PATH,"app-debug.apk");

        if(outputFile.exists()){
           return true;
        }
        else return false;

    }

}
