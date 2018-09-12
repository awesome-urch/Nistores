package com.nistores.awesomeurch.nistores.Folders.Helpers;


/**
 * Created by Awesome Urch on 27/07/2018.
 * A class that holds my API calls
 */

public class ApiUrls {
    
	private String apiURL = "https://www.nistores.com.ng/api/src/routes/process_one.php?";
	private String loginURL = "https://www.nistores.com.ng/api/src/routes/process_one.php?request=login";
    private String productsURL = "https://www.nistores.com.ng/api/src/routes/process_one.php?request=products";
    public String url2 = "https://www.nistores.com.ng/api/src/routes/process_user.php";
    public String getProductsURL(){
        return productsURL;
    }
	public String getApiUrl(){
		return apiURL;
	}
	public String getLoginURL(){
		return loginURL;
	}
}
