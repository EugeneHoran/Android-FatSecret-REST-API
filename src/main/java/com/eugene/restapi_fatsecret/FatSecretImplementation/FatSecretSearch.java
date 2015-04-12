package com.eugene.restapi_fatsecret.FatSecretImplementation;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class FatSecretSearch {
    /**
     * FatSecret Authentication
     * http://platform.fatsecret.com/api/default.aspx?screen=rapiauth
     * Reference
     * https://github.com/ethan-james/cookbox/blob/master/src/com/vitaminc4/cookbox/FatSecret.java
     */
    final static private String APP_METHOD = "GET";
    final static private String APP_KEY = "...";
    final static private String APP_SECRET = "...";
    final static private String APP_URL = "http://platform.fatsecret.com/rest/server.api";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public JSONObject searchFood(String searchFood, int page) {
        List<String> params = new ArrayList<>(Arrays.asList(generateOauthParams(page)));
        String[] template = new String[1];
        params.add("method=foods.search");
        params.add("search_expression=" + Uri.encode(searchFood));
        params.add("oauth_signature=" + sign(APP_METHOD, APP_URL, params.toArray(template)));

        JSONObject foods = null;
        try {
            URL url = new URL(APP_URL + "?" + paramify(params.toArray(template)));
            URLConnection api = url.openConnection();
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(api.getInputStream()));
            while ((line = reader.readLine()) != null) builder.append(line);
            JSONObject food = new JSONObject(builder.toString());   // { first
            foods = food.getJSONObject("foods");                    // { second
        } catch (Exception exception) {
            Log.e("FatSecret Error", exception.toString());
            exception.printStackTrace();
        }
        return foods;
    }

    private static String[] generateOauthParams(int i) {
        return new String[]{
            "oauth_consumer_key=" + APP_KEY,        // Your API key when you registered as a developer
            "oauth_signature_method=HMAC-SHA1",     //The method used to generate the signature (only HMAC-SHA1 is supported)
            "oauth_timestamp=" +                    //The date and time, expressed in the number of seconds since January 1, 1970 00:00:00 GMT.
                Long.valueOf(System.currentTimeMillis() * 2).toString(), // Should be  Long.valueOf(System.currentTimeMillis() / 1000).toString()
            "oauth_nonce=" + nonce(),               // A randomly generated string for a request that can be combined with the timestamp to produce a unique value
            "oauth_version=1.0",                    // MUST be "1.0"
            "format=json",                          // The desired response format. Valid reponse formats are "xml" or "json" (default value is "xml").
            "page_number=" + i,                     // The zero-based offset into the results for the query. Use this parameter with max_results to request successive pages of search results (default value is 0).
            "max_results=" + 20};                   // The maximum number of results to return (default value is 20). This number cannot be greater than 50.
    }

    private static String sign(String method, String uri, String[] params) {
        String[] p = {method, Uri.encode(uri), Uri.encode(paramify(params))};
        String s = join(p, "&");
        SecretKey sk = new SecretKeySpec(APP_SECRET.getBytes(), HMAC_SHA1_ALGORITHM);
        try {
            Mac m = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            m.init(sk);
            return Uri.encode(new String(Base64.encode(m.doFinal(s.getBytes()), Base64.DEFAULT)).trim());
        } catch (java.security.NoSuchAlgorithmException e) {
            Log.w("FatSecret_TEST FAIL", e.getMessage());
            return null;
        } catch (java.security.InvalidKeyException e) {
            Log.w("FatSecret_TEST FAIL", e.getMessage());
            return null;
        }
    }

    private static String paramify(String[] params) {
        String[] p = Arrays.copyOf(params, params.length);
        Arrays.sort(p);
        return join(p, "&");
    }

    private static String join(String[] array, String separator) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0)
                b.append(separator);
            b.append(array[i]);
        }
        return b.toString();
    }

    private static String nonce() {
        Random r = new Random();
        StringBuilder n = new StringBuilder();
        for (int i = 0; i < r.nextInt(8) + 2; i++)
            n.append(r.nextInt(26) + 'a');
        return n.toString();
    }
}
