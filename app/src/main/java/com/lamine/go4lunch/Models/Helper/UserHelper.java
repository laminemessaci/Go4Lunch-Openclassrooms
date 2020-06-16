package com.lamine.go4lunch.Models.Helper;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lamine MESSACI on 16/06/2020.
 */
public class UserHelper {

    private static final String COLLECTION_USERS = "users";
    private static final String COLLECTION_LIKED = "restaurantLike";
    private static final String GET_RESTAURANT_ID = "restaurantId";
    private static final String GET_JOINED_RESTAURANT = "joinedRestaurant";
    private static final String GET_VICINITY = "vicinity";
    private static final String GET_URL_PICTURE = "urlPicture";
    private static final String GET_USERNAME = "username";

    private static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_USERS);
    }

    // --- COLLECTION REFERENCE ---

    private static CollectionReference getLikedCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_LIKED);
    }

    public static Task<Void> createUser(String uid, String username, String urlPicture) {
        // 1 - Create User object
        User userToCreate = new User(uid, username, urlPicture);

        // 2 - Add a new User Document to Firestore
        return UserHelper.getUsersCollection()
                .document(uid) // Setting uID for Document
                .set(userToCreate); // Setting object for Document
    }

    // --- CREATE ---

    public static Task<Void> createLike(String restaurantId, String userId) {
        Map<String, Object> user = new HashMap<>();
        user.put(userId, true);
        return UserHelper.getLikedCollection().document(restaurantId).set(user, SetOptions.merge());
    }

    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- GET ---

    private static Task<DocumentSnapshot> getLikeForTheRestaurant(String restaurantId) {
        return UserHelper.getLikedCollection().document(restaurantId).get();
    }

    public static Task<QuerySnapshot> restoreLike(String uid) {
        return UserHelper.getLikedCollection().whereEqualTo(uid, true).get();
    }

    public static Task<QuerySnapshot> getRestaurant(String restaurantId) {
        return UserHelper.getUsersCollection().whereEqualTo(GET_RESTAURANT_ID, restaurantId).get();
    }

    public static Task<DocumentSnapshot> getBookingRestaurant(String uid) {
        return UserHelper.getUsersCollection().document(uid).get();
    }

    public static Task<QuerySnapshot> getAllUsernames() {
        return UserHelper.getUsersCollection().get();
    }

    public static Task<Void> updateUserRestaurant(String userId, String joinedRestaurant, String restaurantId, String vicinity) {
        return UserHelper.getUsersCollection().document(userId).update(GET_JOINED_RESTAURANT, joinedRestaurant, GET_RESTAURANT_ID, restaurantId, GET_VICINITY, vicinity);
    }

    // --- UPDATE ---

    public static Task<Void> updateUser(String uid, String username, String urlPicture) {
        return UserHelper.getUsersCollection().document(uid).update(GET_USERNAME, username, GET_URL_PICTURE, urlPicture);
    }

    public static Boolean deleteLike(String restaurantId, String userId) {
        UserHelper.getLikeForTheRestaurant(restaurantId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> update = new HashMap<>();
                update.put(userId, FieldValue.delete());
                UserHelper.getLikedCollection().document(restaurantId).update(update);
            }
        });
        return true;
    }

    // ---- DELETE ---

    public static Task<Void> deleteUserRestaurant(String userId) {
        Map<String, Object> update = new HashMap<>();
        update.put(GET_JOINED_RESTAURANT, FieldValue.delete());
        update.put(GET_RESTAURANT_ID, FieldValue.delete());
        update.put(GET_VICINITY, FieldValue.delete());
        return UserHelper.getUsersCollection().document(userId).update(update);
    }

    public static void getRestaurantInfo(com.corroy.mathieu.go4lunch.Models.Details.Result result, OnRequestListener onRequestListener) {
        List<User> users = new ArrayList<>();
        FirebaseFirestore.getInstance()
                .collection(COLLECTION_USERS)
                .whereEqualTo(GET_RESTAURANT_ID, result.getPlaceId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                        for (DocumentSnapshot documentSnapshot : myListOfDocuments) {
                            User user = documentSnapshot.toObject(User.class);
                            user.setJoinedRestaurant(result.getName());
                            user.setRestaurantId(result.getPlaceId());
                            if (!user.getUid().equals(getCurrentUser().getUid())) {
                                users.add(user);
                            }
                        }
                        onRequestListener.onResult(users);
                    }
                });
    }

    public static void getCoWorkers(OnRequestListener onRequestListener) {
        List<User> users = new ArrayList<>();
        FirebaseFirestore.getInstance()
                .collection(COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                        for (DocumentSnapshot documentSnapshot : myListOfDocuments) {
                            User user = documentSnapshot.toObject(User.class);
                            if (!user.getUid().equals(getCurrentUser().getUid())) {
                                users.add(user);
                            }
                        }
                        onRequestListener.onResult(users);
                    }
                });
    }

    @Nullable
    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static boolean isCurrentUserLogged() {
        return (getCurrentUser() != null);
    }

    public interface OnRequestListener {
        void onResult(List<User> userList);
    }

}