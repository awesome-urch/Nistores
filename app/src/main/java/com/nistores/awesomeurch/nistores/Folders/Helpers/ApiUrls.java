package com.nistores.awesomeurch.nistores.Folders.Helpers;


/**
 * Created by Awesome Urch on 27/07/2018.
 * A class that holds my API calls
 */

public class ApiUrls {

    private String live = "https://www.nistores.com.ng/api/src/routes/";
    private String online = "https://www.nistores.com.ng/";
    private String offline = "http://192.168.43.60/pagesn/";
    private String local = "http://192.168.43.60/pagesn/";
	private String apiURL = local+"process_one.php?";
	private String loginURL = "https://www.nistores.com.ng/api/src/routes/process_one.php?request=login";
    private String productsURL = "https://www.nistores.com.ng/api/src/routes/process_one.php?request=products";
    public String url2 = "https://www.nistores.com.ng/api/src/routes/process_user.php";
    private String apiURL2 = local+"test.php";

    private String uploadsFolder = "https://www.nistores.com.ng/api/src/routes/";
    private String processPost = local+"process_post.php";

    public String getProductsURL(){
        return productsURL;
    }
	public String getApiUrl(){
		return apiURL;
	}
	public String getLoginURL(){
		return loginURL;
	}

    public String getApiURL2() {
        return apiURL2;
    }

    public String getUploadsFolder() {
        return local;
    }

    public String getProcessPost() {
        return processPost;
    }

    public String getOffline() {
        return offline;
    }

    public String getOnline() {
        return online;
    }

    public String getBaseURL(){ return online; }
}
