package in.cmile.iv.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import in.cmile.iv.R;
import in.cmile.iv.activity.BottomNavigation;
import in.cmile.iv.adapters.VendorsListAdapter;
import in.cmile.iv.helper.FirebaseHelper;
import in.cmile.iv.models.VendorInfo;

/**
 * Created by pintu on 2020-04-01
 */
public class DashboardFragment extends Fragment {
    List<VendorInfo> vendorList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VendorsListAdapter mAdapter;
    private ProgressDialog progressDialog;
    private String vendorsURL;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        getActivity().setTitle(R.string.dashboard);
        ((BottomNavigation) getActivity()).setBNVVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_vendors);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.getting_data));

        getVendorInfoList();
        mAdapter = new VendorsListAdapter(getActivity(), vendorList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        return rootView;

    }


    private void getVendorInfoList() {
        progressDialog.show();
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        vendorsURL = null;
        vendorsURL = FirebaseHelper.ROOT_COL_VENDORS;
        firebaseHelper.readCollectionFromFirestore(vendorsURL,
                new FirebaseHelper.CollectionReadListener() {
                    @Override
                    public void handleDocuments(Task<QuerySnapshot> task) {
                        task.addOnSuccessListener(queryDocumentSnapshots -> {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();

                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Map<String, Object> keySortedtreeMap = convertToTreeMap(documentSnapshot.getData());
                                    String vendorId = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_VENDOR_ID).toString();
                                    String area = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_AREA).toString();
                                    String pincode = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_PINCODE).toString();
                                    String mobility = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_MOBILITY).toString();
                                    String address = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_ADDRESS).toString();
                                    String lattitude = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_LATTITUDE).toString();
                                    String city = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_CITY).toString();
                                    String mobile = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_MOBILE).toString();
                                    String type = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_TYPE).toString();
                                    String name = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_NAME).toString();
                                    String closeTime = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_CLOSE_TIME).toString();
                                    String state = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_STATE).toString();
                                    String openTime = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_OPEN_TIME).toString();
                                    String status = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_STATUS).toString();
                                    String longitude = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_LONGITUDE).toString();

                                    VendorInfo vendorInfo = new VendorInfo(name, address, area, city, state, pincode, mobile, type, mobility, vendorId, status, openTime, closeTime, lattitude, longitude);
                                    vendorList.add(vendorInfo);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }).addOnFailureListener(e -> Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onDocumentsReadError(Exception error) {
                        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Convert HashMap to Sorted TreeMap
    public static <String, Object> Map<String, Object> convertToTreeMap(Map<String, Object> hashMap) {
        // Create a new TreeMap
        Map<String, Object> treeMap = new TreeMap<>(Collections.reverseOrder());
        // Pass the hashMap to putAll() method
        treeMap.putAll(hashMap);
        // Return the TreeMap
        return treeMap;
    }
}
