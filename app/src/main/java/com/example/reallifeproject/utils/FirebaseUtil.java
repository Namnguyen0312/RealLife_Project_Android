package com.example.reallifeproject.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil {

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn(){
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            return true;
        }
        return false;
    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference getPlayerModelReferenceWithId(){
        return FirebaseFirestore.getInstance().collection("players").document(currentUserId());
    }
    public static CollectionReference getPlayerModelReference(){
        return FirebaseFirestore.getInstance().collection("players");
    }

    public static CollectionReference getEventModelReference(){
        return getPlayerModelReferenceWithId().collection("events");
    }
}
