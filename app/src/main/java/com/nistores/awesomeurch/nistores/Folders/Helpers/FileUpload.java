package com.nistores.awesomeurch.nistores.Folders.Helpers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nistores.awesomeurch.nistores.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public abstract class FileUpload {
    public Context context;
    private ApiUrls apiUrls;
    private String URL;

    public FileUpload(Context context){
        this.context = context;
    }

    private void uploadImage(final String image, final String ext){

        //Log.d("SEE",""+image.getBytes().length);
        apiUrls = new ApiUrls();
        URL = apiUrls.getApiURL2();
        //Handle interstitial anxiety
        onProcess();
        //String iURL = imgURL + "request=upload";
        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {

                Log.d("DFILE",s);
                //progressDialog.dismiss();
                if(s.equals("error")){

                    Toast.makeText(context, "Some error occurred!", Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_LONG).show();
                    String imgPath = apiUrls.getUploadsFolder() + s;
                    onSuccess(imgPath);

                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(context, "Network error occurred. Try again", Toast.LENGTH_LONG).show();
                Log.d("ERR",volleyError.toString());


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> parameters = new HashMap<String, String>();
                //parameters.put("Content-Type", "application/form-data");
                //parameters.put("Content-Length", ""+97957);
                parameters.put("Connection", "Keep-Alive");
                return parameters;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
                //return "application/x-www-form-urlencoded";
            }

            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError  {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", image);
                parameters.put("ext", ext);
                return parameters;
            }
        };


        request.setRetryPolicy(new DefaultRetryPolicy( 70000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(context);
        request.setShouldCache(false);
        //InitiateVolley.getInstance().addToRequestQueue(request);

        rQueue.add(request);

    }

    public abstract void onProcess();
    public abstract void onSuccess(String imgPath);
    public abstract void onServerError();
    public abstract void onNetworkError();

}
