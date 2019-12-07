//package com.rutgers.cryptography.util;
//
///**
// * Created by Qin Yuxin on 2019/12/7 21:28
// */
//
//
//import net.sf.json.JSONObject;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
//import org.apache.http.conn.ssl.SSLSocketFactory;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//
//import java.io.IOException;
//
//public class HttpClientUtil {
//
//    public static JSONObject doGetStr(String accessTokenUrl) {
//        DefaultHttpClient httpClient = new DefaultHttpClient();
//        HttpGet httpGet = new HttpGet(accessTokenUrl);
//        JSONObject jsonObject = null;
//        try {
//            SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
//            HttpResponse response = httpClient.execute(httpGet);
//            HttpEntity entity = response.getEntity();
////            System.err.println(response.getStatusLine().getStatusCode());
//            if (entity != null) {
//                String result = EntityUtils.toString(entity, "UTF-8");
////                System.err.println(result);
//                jsonObject = JSONObject.fromObject(result);
//            }
////            httpGet.releaseConnection();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }
//
//}
//
