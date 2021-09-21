package com.scottlogic.training.order;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.scottlogic.training.FirestoreRepository;
import com.scottlogic.training.matcher.Direction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private FirestoreRepository firestoreRepository;

    public void addOrder(Order order) {
        String docId = order.id.toString();
        DocumentReference docRef = firestoreRepository.db.collection("orders").document(docId);
        ApiFuture<WriteResult> result = docRef.set(order.toMap());
        try {
            System.out.println("Update time : " + result.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getOrders() {
        try {
            ApiFuture<QuerySnapshot> query = firestoreRepository.db.collection("orders").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            return documents.stream().map(this::entityToOrder).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Order> getOrders(String username) {
        List<Order> orders = getOrders();
        orders.removeIf(order -> !order.username.equals(username));
        return orders;
    }

    public void removeOrder(Order order) {
        String id = order.id.toString();
        DocumentReference document = firestoreRepository.db.collection("orders").document(id);
        ApiFuture<WriteResult> writeResult = document.delete();
        try {
            System.out.println("Update time : " + writeResult.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private Order entityToOrder(QueryDocumentSnapshot document) {
        UUID id;
        try {
            id = UUID.fromString(document.getId());
        } catch (IllegalArgumentException e) {
            id = UUID.randomUUID();
        }
        String username = document.getString("username");
        Long priceLong = document.getLong("price");
        Long quantityLong = document.getLong("quantity");
        String directionString = document.getString("direction");
        Timestamp timestampData = document.getTimestamp("timestamp");
        if (priceLong == null || quantityLong == null || timestampData == null || directionString == null) {
            return null;
        }
        int price = priceLong.intValue();
        int quantity = quantityLong.intValue();
        Date timestamp = timestampData.toDate();
        Direction direction = Direction.valueOf(directionString);
        return new Order(id, username, price, quantity, direction, timestamp);
    }
}
