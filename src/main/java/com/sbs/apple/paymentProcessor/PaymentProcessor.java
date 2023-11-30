//package com.sbs.apple.paymentProcessor;
//import kr.co.paywelcome.util.SignatureUtil;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.security.NoSuchAlgorithmException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//public class PaymentProcessor {
//
//    public static void main(String[] args) throws Exception {
//        // Set professional field values
//        String mid = "welcometst";
//        String signKey = "QjZXWDZDRmxYUXJPYnMvelEvSjJ5QT09";
//        String timestamp = SignatureUtil.getTimestamp();
//        String oid = mid + "_" + SignatureUtil.getTimestamp();
//        String price = "1000";
//        String cardNoInterestQuota = "";
//        String cardQuotaBase = "2:3:4";
//        String merchantData = URLEncoder.encode("Test Order", "UTF-8");
//        String siteDomain = "http://localhost8090";
//
//        // Change signKey to hash value using SHA-256
//        String mKey = SignatureUtil.hash(signKey, "SHA-256");
//
//        // Signature creation
//        Map<String, String> signParam = new HashMap<>();
//
//        signParam.put("mKey", mKey);
//        signParam.put("oid", oid);
//        signParam.put("price", price);
//        signParam.put("timestamp", timestamp);
//
//        // Generate signature data
//        String signature = SignatureUtil.makeSignature(signParam);
//
//
//        // Print or use the signature, mKey, and other processed variables as needed
//        System.out.println("Signature: " + signature);
//        System.out.println("mKey: " + mKey);
//        // Add additional processing or usage of these variables here as required
//    }
//}