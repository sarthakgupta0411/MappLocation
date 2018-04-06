package com.example.sarthak.mapplocation;

//This sample uses the Apache HTTP client library(org.apache.httpcomponents:httpclient:4.2.4)
//and the org.json library (org.json:json:20170516).

import java.net.URI;

//import com.sun.xml.internal.fastinfoset.util.StringArray;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class mapHTTPRequest {
    //this is the API key
    public static final String subscriptionKey = "AIzaSyB9nFQd3dLvwrsRgW09aRf3DO8wl7hgE00";

    //uribase for coordinate api set up
    public static String uriBase = "https://maps.googleapis.com/maps/api/geocode/json?address=";

    //uribase for autocorrect api
    public static String uriBase3 = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=";

    public static String getCoords(String addr)
    {
        HttpClient httpClient = new DefaultHttpClient();

        try
        {
            String uribase2 = uriBase.concat(addr + subscriptionKey);
            // NOTE: You must use the same location in your REST call as you used to obtain your subscription keys.
            //   For example, if you obtained your subscription keys from westus, replace "westcentralus" in the
            //   URL below with "westus".
            URIBuilder uriBuilder = new URIBuilder(uribase2);

            //uriBuilder.setParameter("language", "unk");
            //uriBuilder.setParameter("detectOrientation ", "true");

            // Request parameters.
            URI uri = uriBuilder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            //request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            //StringEntity requestEntity =
                 //   new StringEntity("{\"url\":\"https://upload.wikimedia.org/wikipedia/commons/thumb/a/af/Atomist_quote_from_Democritus.png/338px-Atomist_quote_from_Democritus.png\"}");
            //request.setEntity(requestEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);
                String results = json.toString();
                int locationIndexStart = results.indexOf("location");
                int locationIndexEnd = results.indexOf("location_type")-2;
                String location = results.substring(locationIndexStart,locationIndexEnd);
                String latitude = location.substring(17,location.indexOf("lng")-2);
                String longitude = location.substring(location.indexOf("lng")+5,location.length()-1);
                StringBuilder address = new StringBuilder(addr);
                for(int i = 0; i < address.length(); i++) {
                    if(address.charAt(i) == '+') {
                        address.deleteCharAt(i);
                        address.insert(i, ' ');
                    }
                }
                String address2 = address.substring(0,address.length()-5);
                //System.out.println("REST Response:\n");
                String send = latitude + "," + longitude + "," + address2;

                return send;
            }
        }
        catch (Exception e)
        {
            // Display error message.
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static String autoCorrect(String addr){
        HttpClient httpClient = new DefaultHttpClient();

        try
        {
            String addrFormatted = addr.replaceAll(" ","");
            String uribase2 = uriBase3.concat(addrFormatted + "&types=geocode&key=" + subscriptionKey);
            // NOTE: You must use the same location in your REST call as you used to obtain your subscription keys.
            //   For example, if you obtained your subscription keys from westus, replace "westcentralus" in the
            //   URL below with "westus".
            URIBuilder uriBuilder = new URIBuilder(uribase2);

            //uriBuilder.setParameter("language", "unk");
            //uriBuilder.setParameter("detectOrientation ", "true");

            // Request parameters.
            URI uri = uriBuilder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            //request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Request body.
            //StringEntity requestEntity =
            //   new StringEntity("{\"url\":\"https://upload.wikimedia.org/wikipedia/commons/thumb/a/af/Atomist_quote_from_Democritus.png/338px-Atomist_quote_from_Democritus.png\"}");
            //request.setEntity(requestEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(jsonString);
                String results = json.toString();
                int descriptionIndexStart = results.indexOf("description") + 14;
                int descriptionIndexEnd = results.indexOf("id")-3;
                String description = results.substring(descriptionIndexStart,descriptionIndexEnd);
                String formatted = description.replaceAll(" ", "+");
                //String longitude = location.substring(17,location.indexOf("lat")-2);
                //String latitude = location.substring(location.indexOf("lat")+5,location.length()-1);
                //System.out.println("REST Response:\n");
                //String send = longitude + "," + latitude;
                return formatted + "&key=";
            }
        }
        catch (Exception e)
        {
            // Display error message.
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static String findCoords(String addr){
        //mapHTTPRequest mapHTTPRequest = new mapHTTPRequest();
        String coordinates = getCoords(mapHTTPRequest.autoCorrect(addr));
        return coordinates;
    }

}
