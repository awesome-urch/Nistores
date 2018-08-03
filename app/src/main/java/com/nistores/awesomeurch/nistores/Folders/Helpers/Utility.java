package com.nistores.awesomeurch.nistores.Folders.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

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
}
