package com.nistores.awesomeurch.nistores.Folders.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nistores.awesomeurch.nistores.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by Awesome Urch on 27/07/2018.
 * A class that utilizes all the methods frequently called in activity and fragment contexts
 */

public class Utility {
    public Context context;
    public SharedPreferences preferences;
    public Utility(Context c){
        context = c;
    }
    private String productsURL = "https://www.nistores.com.ng/api/src/routes/process_one.php?request=products";
    public String url2 = "https://www.nistores.com.ng/api/src/routes/process_user.php";
    public String getProductsURL(){
        return productsURL;
    }

    public String bitmapToBase64(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, 0);

        return imageString;
    }

    public String getSelectedCats(RecyclerView categoryRecycler){
        StringBuilder ret = new StringBuilder();
        int cnt = 0;
        for (int x = 0; x<categoryRecycler.getChildCount();x++){
            CheckBox cb = categoryRecycler.getChildAt(x).findViewById(R.id.name);
            TextView tv = categoryRecycler.getChildAt(x).findViewById(R.id.id);
            if(cb.isChecked()){
                String s = tv.getText().toString();
                String comma = (cnt>0)?",":"";
                ret.append(comma).append(s);
                cnt++;
                //Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
            }
        }
        return String.valueOf(ret);
    }

}
