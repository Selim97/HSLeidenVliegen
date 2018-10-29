package ikpmd.dursun.hsleidenvliegen;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


// In plaats van telkens los een database transactie doen, gebeurt dat nu met deze klasse

public class FirebaseUtil {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static FirebaseUtil firebaseUtil;
    public static ArrayList<TravelDeal> mDeals;

    // Deze (lege) constructor zorgt ervoor dat deze klasse niet van buitenaf kan worden geïnstantieerd
    private FirebaseUtil(){}

    // Creeert een reference naar de child die wordt meegegeven als parameter
    public static void openFbReference (String ref) {
        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDeals = new ArrayList<TravelDeal>();
        }
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }
}
