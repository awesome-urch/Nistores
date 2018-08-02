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

}
