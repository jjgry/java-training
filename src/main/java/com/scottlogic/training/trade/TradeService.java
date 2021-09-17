package com.scottlogic.training.trade;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class TradeService {
    private Firestore db;

    public TradeService() {
        try {
            InputStream serviceAccount = new FileInputStream("C:\\Users\\jjgray\\firebase-keys.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> tradeToEntity(Trade trade) {
        Map<String, Object> data = new HashMap<>();
        data.put("buyerUsername", trade.buyerUsername);
        data.put("sellerUsername", trade.sellerUsername);
        data.put("price", trade.price);
        data.put("quantity", trade.quantity);
        data.put("timestamp", trade.timestamp);
        return data;
    }

    private Trade entityToTrade(QueryDocumentSnapshot document) {
        UUID id;
        try {
            id = UUID.fromString(document.getId());
        } catch (IllegalArgumentException e) {
            id = UUID.randomUUID();
        }
        String buyerUsername = document.getString("buyerUsername");
        String sellerUsername = document.getString("sellerUsername");
        Long priceLong = document.getLong("price");
        Long quantityLong = document.getLong("quantity");
        Timestamp timestampData = document.getTimestamp("timestamp");
        if (priceLong == null || quantityLong == null || timestampData == null) {
            return null;
        }
        int price = priceLong.intValue();
        int quantity = priceLong.intValue();
        Date timestamp = timestampData.toDate();
        return new Trade(id, buyerUsername, sellerUsername, price, quantity, timestamp);
    }

    public void addTrade(Trade trade) {
        String docId = trade.id.toString();
        DocumentReference docRef = db.collection("trades").document(docId);
        Map<String, Object> data = tradeToEntity(trade);
        ApiFuture<WriteResult> result = docRef.set(data);
        try {
            System.out.println("Update time : " + result.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public List<Trade> getTrades() {
        try {
            ApiFuture<QuerySnapshot> query = db.collection("trades").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            return documents.stream().map(this::entityToTrade).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

