package com.scottlogic.training.user;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.scottlogic.training.FirestoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class UserService {
    @Autowired
    private FirestoreRepository firestoreRepository;

    public void addUser(User user) {
        CollectionReference collectionRef = firestoreRepository.db.collection("users");
        ApiFuture<DocumentReference> result = collectionRef.add(user.toMap());
        try {
            System.out.println("Update time : " + result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUsers() {
        try {
            ApiFuture<QuerySnapshot> query = firestoreRepository.db
                    .collection("users").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            return documents.stream().map(this::entityToUser).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public User getUser(String username) {
        try {
            ApiFuture<DocumentSnapshot> query = firestoreRepository.db
                    .collection("users").document(username).get();
            DocumentSnapshot documentSnapshot = query.get();
            return entityToUser(documentSnapshot);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private User entityToUser(DocumentSnapshot document) {
        String username = document.getId();
        String salt = document.getString("salt");
        String passwordHash = document.getString("passwordHash");
        if (salt == null || passwordHash == null) {
            return null;
        }
        return new User(username, salt.getBytes(), passwordHash.getBytes());
    }

    public void removeUser(String username) {
        DocumentReference document = firestoreRepository.db
                .collection("orders")
                .document(username);
        ApiFuture<WriteResult> writeResult = document.delete();
        try {
            System.out.println("Update time : " + writeResult.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

