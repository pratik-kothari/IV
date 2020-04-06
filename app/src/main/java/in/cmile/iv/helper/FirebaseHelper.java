/*
 * Created by Chirag Palesha on 25/7/19 11:11 AM
 * Last Modified: 25/7/19 11:11 AM
 * Copyright (c) 2019. All rights reserved. Cmile
 */
package in.cmile.iv.helper;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;

public class FirebaseHelper {
    //CONSTANTS

    public static final String DOC_KEY_VENDOR_ID = "vendorId";
    public static final String DOC_KEY_AREA = "area";
    public static final String DOC_KEY_PINCODE = "pincode";
    public static final String DOC_KEY_MOBILITY = "mobility";
    public static final String DOC_KEY_ADDRESS = "address";
    public static final String DOC_KEY_LATTITUDE = "lattitude";
    public static final String DOC_KEY_CITY = "city";
    public static final String DOC_KEY_MOBILE = "mobile";
    public static final String DOC_KEY_TYPE = "type";
    public static final String DOC_KEY_NAME = "name";
    public static final String DOC_KEY_CLOSE_TIME = "closeTime";
    public static final String DOC_KEY_STATE = "state";
    public static final String DOC_KEY_OPEN_TIME = "openTime";
    public static final String DOC_KEY_STATUS = "status";
    public static final String DOC_KEY_LONGITUDE = "longitude";
    public static final String ROOT_COL_VENDORS = "vendors";
    public static final String DOC_KEY_CATEGORIES = "categories";

    public static final String CATEGORY_VEGETABLES = "Vegetables";
    public static final String CATEGORY_FRUITS = "Fruits";
    public static final String CATEGORY_DAIRY_PRODUCTS = "DairyProducts";
    public static final String CATEGORY_GENERAL_STORE = "GeneralStore";
    public static final String CATEGORIES = "categories";


    public static final String SEPARATOR = "/";
    public static final int SETUP_OPT_SET_NEW = 1;
    public static final int SETUP_OPT_SET_MERGE = 2;
    public static final int BATCH_WRITE_SET = 1;
    public static final int BATCH_WRITE_UPDATE = 2;
    public static final int BATCH_WRITE_DELETE = 0;
    public static final int LIMIT = 5;

    public static int READ_COUNT = 0;
    private static FirebaseUser currentFirebaseUser = null;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestoreSettings settings;
    private WriteBatch writeBatch;

    public FirebaseHelper() {
        FirebaseHelper.currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        if (!db.getFirestoreSettings().equals(settings)) {
            db.setFirestoreSettings(settings);
        }
    }

    public static FirebaseUser getCurrentFirebaseUser() {
        return currentFirebaseUser;
    }

    public void initWriteBatch() {
        writeBatch = db.batch();
    }

    public void addTaskToBatch(@NonNull String urlToDoc, int taskType, String field, Object valueObject) {
        DocumentReference docRef;
        docRef = db.document(urlToDoc);
        switch (taskType) {
            case BATCH_WRITE_SET:
                writeBatch.set(docRef, valueObject);
                break;
            case BATCH_WRITE_DELETE:
                writeBatch.delete(docRef);
                break;
            case BATCH_WRITE_UPDATE:
                writeBatch.update(docRef, field, valueObject);
                break;
        }
    }

    public void performWriteBatch(final WriteBatchListener writeBatchListener) {
        writeBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                writeBatchListener.onWriteBatchCompleted(task);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        writeBatchListener.onWriteBatchFailure(e);
                    }
                });
    }

    public void onUser(LoggedIn loggedIn) {
        FirebaseHelper.currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (loggedIn == null)
            return;
        if (FirebaseHelper.currentFirebaseUser != null) {
            loggedIn.successfullyLogged();
        } else {
            loggedIn.unsuccessfullyLogged();
        }
    }

    public void signInWith(String email, String password, final SignInListener signInListener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    signInListener.successfullySignedIn(task);
                } else {
                    // If sign in fails, display a message to the user.
                    signInListener.failedSigningIn(task);
                }
            }
        });
    }

    public void userLogout() {
        FirebaseAuth.getInstance().signOut();
    }

    public void writeDocumentToFirestore(String url, String docName, Object documentData,
                                         int action, final DocumentWriteListener documentWriteListener) {
        DocumentReference docRef;
        if (docName != null) {
            docRef = db.document(url + ConstantHelper.TEXT_SLASH + docName);
            switch (action) {
                case SETUP_OPT_SET_NEW:
                    docRef.set(documentData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            documentWriteListener
                                    .onWriteSuccess(null);
                        }
                    })
                            .addOnFailureListener(documentWriteListener::onWriteFailure);
                    break;
                case SETUP_OPT_SET_MERGE:
                    docRef.set(documentData, SetOptions.merge()).addOnSuccessListener
                            (new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    documentWriteListener.onWriteSuccess(null);
                                }
                            })
                            .addOnFailureListener(documentWriteListener::onWriteFailure);
                    break;
                default:
                    break;
            }
        } else {
            db.collection(url).add(documentData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    documentWriteListener.onWriteSuccess(documentReference);
                }
            })
                    .addOnFailureListener(documentWriteListener::onWriteFailure);
        }
    }

    public void writeDocumentFieldToFirestore(String url, String docName, Object documentData,
                                              int action, final DocumentWriteListener documentWriteListener) {
        DocumentReference docRef;

        if (docName != null) {
            docRef = db.document(url + "/" + docName);
            switch (action) {
                case SETUP_OPT_SET_NEW:
                    docRef.set(documentData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            documentWriteListener
                                    .onWriteSuccess(null);
                        }
                    })
                            .addOnFailureListener(documentWriteListener::onWriteFailure);
                    break;
                case SETUP_OPT_SET_MERGE:
                    docRef.set(documentData, SetOptions.merge()).addOnSuccessListener
                            (new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    documentWriteListener.onWriteSuccess(null);
                                }
                            })
                            .addOnFailureListener(documentWriteListener::onWriteFailure);
                    break;
                default:
                    break;
            }
        } else {
            db.collection(url).add(documentData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    documentWriteListener.onWriteSuccess(documentReference);
                }
            })
                    .addOnFailureListener(documentWriteListener::onWriteFailure);
        }
    }

    public String getUniqueId(String collection) {
        DocumentReference docRef = db.collection(collection).document();
        return docRef.getId();
    }

    public void readDocumentFromFirestore(String url, final DocumentReadListener readListener) {
        DocumentReference docRef = db.document(url);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    READ_COUNT++;
                    if (document.exists()) {
                        readListener.handleReadDocument(document);
                    } else {
                        readListener.handleReadDocument(null);
                    }
                } else {
                    readListener.onDocumentReadError(task.getException());
                }
            }
        });
    }

    public void readDocumentFromFirestoreOnComplete(String url, final DocumentReadListener readListener) {
        DocumentReference docRef = db.document(url);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                READ_COUNT++;
                if (document.exists()) {
                    readListener.handleReadDocument(document);
                } else {
                    readListener.handleReadDocument(null);
                }
            }
        });
    }

    public void readCollectionFromFirestore(String url, final CollectionReadListener readListener) {
        CollectionReference colRef = db.collection(url);
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    readListener.handleDocuments(task);
                } else {
                    readListener.onDocumentsReadError(task.getException());
                }
            }
        });
    }

    public void readCollectionFromFirestoreWithQuery(String url, List<String> categoryItem, final CollectionReadListener readListener) {
        Query colRef;

        if (categoryItem.size() > 0) {
            colRef = db.collection(url).whereArrayContainsAny(FirebaseHelper.CATEGORIES, categoryItem).limit(LIMIT);
        } else {
            colRef = db.collection(url).limit(LIMIT);
        }

        colRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                readListener.handleDocuments(task);
            } else {
                readListener.onDocumentsReadError(task.getException());
            }
        });
    }

    public void readCollectionFromFirestoreAt(@NonNull String url, @NonNull Object startAt, int limit,
                                              @NonNull String orderBy, Query.Direction orderByDirection, final CollectionReadListener readListener) {
        try {
            Query query;

            if (orderByDirection == null) {
                query = db.collection(url)
                        .orderBy(orderBy)
                        .startAt((int) startAt)
                        .limit(limit);
            } else {
                query = db.collection(url)
                        .orderBy(orderBy, orderByDirection)
                        .startAt((int) startAt)
                        .limit(limit);
            }
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        readListener.handleDocuments(task);
                    } else {
                        readListener.onDocumentsReadError(task.getException());
                    }
                }
            });
        } catch (ClassCastException exception) {
            Query query = db.collection(url)
                    .orderBy(orderBy)
                    .startAt((DocumentSnapshot) startAt)
                    .limit(limit);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        readListener.handleDocuments(task);
                    } else {
                        readListener.onDocumentsReadError(task.getException());
                    }
                }
            });
        }
    }

    public void readCollectionFromFirestoreAfter(@NonNull String url, @NonNull Object startAfter, int limit,
                                                 @NonNull String orderBy, final CollectionReadListener readListener) {
        try {
            Query query = db.collection(url)
                    .orderBy(orderBy)
                    .startAfter((int) startAfter)
                    .limit(limit);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        readListener.handleDocuments(task);
                    } else {
                        readListener.onDocumentsReadError(task.getException());
                    }
                }
            });
        } catch (ClassCastException exception) {
            Query query = db.collection(url)
                    .orderBy(orderBy)
                    .startAfter((DocumentSnapshot) startAfter)
                    .limit(limit);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        readListener.handleDocuments(task);
                    } else {
                        readListener.onDocumentsReadError(task.getException());
                    }
                }
            });
        }
    }

    public void deleteDocumentFromFirestore(String url,
                                            final DocumentDeleteListener deleteListener) {
        db.document(url).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deleteListener.onDeleteSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        deleteListener.onDeleteFailure(e);
                    }
                });
    }

    public void readCollectionFromFirestoreWithNextData(String vendorsURL, List<String> category, Object lastVisible, CollectionReadListener readListener) {

        try {
            Query query = db.collection(vendorsURL)
                    .startAfter((int) lastVisible)
                    .limit(LIMIT);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        readListener.handleDocuments(task);
                    } else {
                        readListener.onDocumentsReadError(task.getException());
                    }
                }
            });
        } catch (ClassCastException exception) {
            Query query = db.collection(vendorsURL)
                    .startAfter((DocumentSnapshot) lastVisible)
                    .limit(LIMIT);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        readListener.handleDocuments(task);
                    } else {
                        readListener.onDocumentsReadError(task.getException());
                    }
                }
            });
        }
    }


    public interface WriteBatchListener {
        void onWriteBatchCompleted(Task<Void> task);

        void onWriteBatchFailure(Exception exception);
    }

    public interface CollectionReadListener {
        void handleDocuments(Task<QuerySnapshot> task);

        void onDocumentsReadError(Exception error);
    }

    public interface DocumentWriteListener {
        void onWriteSuccess(DocumentReference documentReference);

        void onWriteFailure(Exception exception);
    }

    public interface DocumentReadListener {
        void handleReadDocument(DocumentSnapshot documentSnapshot);

        void onDocumentReadError(Exception error);
    }

    public interface DocumentDeleteListener {
        void onDeleteSuccess();

        void onDeleteFailure(Exception exception);
    }

    public interface SignInListener {
        void successfullySignedIn(@NonNull Task<AuthResult> task);

        void failedSigningIn(@NonNull Task<AuthResult> task);
    }

    public interface LoggedIn {
        void successfullyLogged();

        void unsuccessfullyLogged();
    }
}