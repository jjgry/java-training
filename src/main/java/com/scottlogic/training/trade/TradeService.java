package com.scottlogic.training.trade;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.scottlogic.training.FirestoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class TradeService {
    @Autowired
    private FirestoreRepository firestoreRepository;
    private long TRADE_HISTORY_LENGTH = 100;

    public void addTrade(Trade trade) {
        String docId = trade.id.toString();
        DocumentReference docRef = firestoreRepository.db.collection("trades").document(docId);
        ApiFuture<WriteResult> result = docRef.set(trade.toMap());
        try {
            System.out.println("Update time : " + result.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public List<Trade> getTrades() {
        try {
            ApiFuture<QuerySnapshot> query = firestoreRepository.db.collection("trades").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            return documents.stream().map(this::entityToTrade).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Trade> getTrades(String username) {
        List<Trade> trades = getTrades();
        trades.removeIf(
                trade -> !(trade.buyerUsername.equals(username) && trade.sellerUsername.equals(username)));
        return trades;
    }

    public List<Trade> getTradeHistory() {
        return getTrades().stream().limit(TRADE_HISTORY_LENGTH).collect(Collectors.toList());
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
}

