package in.cmile.iv.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    LinearLayout llVegetables, llFruits, llDairyProducts, llGeneralStore, llSubmit, llUpdateCancel, llMobile, llStore, llToggleOpenClose;
    ImageView ivVegitables, ivFruits, ivDairy, ivGeneralStore, ivVegitablesBorder, ivFruitsBorder, ivDairyBorder,
            ivGeneralStoreBorder;
    View rootView;
    private RecyclerView recyclerView;
    private VendorsListAdapter mAdapter;
    private ProgressDialog progressDialog;
    private String vendorsURL;
    private int flagVeg = 0;
    private int flagFruits = 0;
    private int flagDairy = 0;
    private int flagGeneral = 0;
    private DocumentSnapshot lastVisible;
    private boolean isScrolling = false;
    private boolean isLastItemReached = false;
    List<String> category = new ArrayList<>();

    //Convert HashMap to Sorted TreeMap
    public static <String, Object> Map<String, Object> convertToTreeMap(Map<String, Object> hashMap) {
        // Create a new TreeMap
        Map<String, Object> treeMap = new TreeMap<>(Collections.reverseOrder());
        // Pass the hashMap to putAll() method
        treeMap.putAll(hashMap);
        // Return the TreeMap
        return treeMap;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        getActivity().setTitle(R.string.dashboard);
        ((BottomNavigation) getActivity()).setBNVVisibility(View.VISIBLE);

        initView();

        return rootView;
    }

    private void initView() {

        recyclerView = rootView.findViewById(R.id.rv_vendors);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.getting_data));
        progressDialog.setCanceledOnTouchOutside(false);

        llVegetables = rootView.findViewById(R.id.ll_vegetables);
        llFruits = rootView.findViewById(R.id.ll_fruits);
        llDairyProducts = rootView.findViewById(R.id.ll_dairy_products);
        llGeneralStore = rootView.findViewById(R.id.ll_general_store);

        ivVegitables = rootView.findViewById(R.id.iv_vegitables);
        ivFruits = rootView.findViewById(R.id.iv_fruits);
        ivDairy = rootView.findViewById(R.id.iv_dairy_products);
        ivGeneralStore = rootView.findViewById(R.id.iv_general_store);
        ivVegitablesBorder = rootView.findViewById(R.id.iv_vegitables_border);
        ivFruitsBorder = rootView.findViewById(R.id.iv_fruits_border);
        ivDairyBorder = rootView.findViewById(R.id.iv_dairy_products_border);
        ivGeneralStoreBorder = rootView.findViewById(R.id.iv_general_store_border);


        llVegetables.setOnClickListener(view -> {
            if (flagVeg == 0) {
                ivVegitablesBorder.setVisibility(View.VISIBLE);
                ivVegitables.setVisibility(View.GONE);
                ivVegitablesBorder.setImageResource(R.drawable.ic_veggies);
                flagVeg = 1;
                readVendorsData(flagVeg, flagFruits, flagDairy, flagGeneral);

            } else if (flagVeg == 1) {
                ivVegitables.setVisibility(View.VISIBLE);
                ivVegitablesBorder.setVisibility(View.GONE);
                ivVegitables.setImageResource(R.drawable.ic_veggies_border);
                flagVeg = 0;
                readVendorsData(flagVeg, flagFruits, flagDairy, flagGeneral);
            }
        });

        llFruits.setOnClickListener(view -> {
            if (flagFruits == 0) {
                ivFruitsBorder.setVisibility(View.VISIBLE);
                ivFruits.setVisibility(View.GONE);
                ivFruitsBorder.setImageResource(R.drawable.ic_fruits);
                flagFruits = 1;
                readVendorsData(flagVeg, flagFruits, flagDairy, flagGeneral);

            } else if (flagFruits == 1) {
                ivFruits.setVisibility(View.VISIBLE);
                ivFruitsBorder.setVisibility(View.GONE);
                ivFruits.setImageResource(R.drawable.ic_fruits_border);
                flagFruits = 0;
                readVendorsData(flagVeg, flagFruits, flagDairy, flagGeneral);
            }
        });

        llDairyProducts.setOnClickListener(view -> {
            if (flagDairy == 0) {
                ivDairyBorder.setVisibility(View.VISIBLE);
                ivDairy.setVisibility(View.GONE);
                ivDairyBorder.setImageResource(R.drawable.ic_dairy);
                flagDairy = 1;
                readVendorsData(flagVeg, flagFruits, flagDairy, flagGeneral);


            } else if (flagDairy == 1) {
                ivDairy.setVisibility(View.VISIBLE);
                ivDairyBorder.setVisibility(View.GONE);
                ivDairy.setImageResource(R.drawable.ic_dairy_border);
                flagDairy = 0;
                readVendorsData(flagVeg, flagFruits, flagDairy, flagGeneral);
            }
        });

        llGeneralStore.setOnClickListener(view -> {
            if (flagGeneral == 0) {
                ivGeneralStoreBorder.setVisibility(View.VISIBLE);
                ivGeneralStore.setVisibility(View.GONE);
                ivGeneralStoreBorder.setImageResource(R.drawable.ic_general_store);
                flagGeneral = 1;
                readVendorsData(flagVeg, flagFruits, flagDairy, flagGeneral);

            } else if (flagGeneral == 1) {
                ivGeneralStore.setVisibility(View.VISIBLE);
                ivGeneralStoreBorder.setVisibility(View.GONE);
                ivGeneralStore.setImageResource(R.drawable.ic_general_store_border);
                flagGeneral = 0;
                readVendorsData(flagVeg, flagFruits, flagDairy, flagGeneral);
            }
        });


        getVendorInfoList();
        mAdapter = new VendorsListAdapter(getActivity(), vendorList, flagVeg, flagFruits, flagDairy, flagGeneral);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);


//        readVendorsData(flagVeg, flagFruits, flagDairy, flagGeneral);
    }

    private void readVendorsData(int flagVeg, int flagFruits, int flagDairy, int flagGeneral) {
        progressDialog.show();
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        vendorsURL = FirebaseHelper.ROOT_COL_VENDORS;
        vendorList.clear();

        String categoryItem = "";
        if (flagVeg == 1) {
            categoryItem = FirebaseHelper.CATEGORY_VEGETABLES;
            category.add(categoryItem);
        } else {
            category.remove(categoryItem);
        }

        if (flagFruits == 1) {
            categoryItem = FirebaseHelper.CATEGORY_FRUITS;
            category.add(categoryItem);
        } else {
            category.remove(categoryItem);
        }

        if (flagDairy == 1) {
            categoryItem = FirebaseHelper.CATEGORY_DAIRY_PRODUCTS;
            category.add(categoryItem);
        } else {
            category.remove(categoryItem);
        }

        if (flagGeneral == 1) {
            categoryItem = FirebaseHelper.CATEGORY_GENERAL_STORE;
            category.add(categoryItem);
        } else {
            category.remove(categoryItem);
        }

        firebaseHelper.readCollectionFromFirestoreWithQuery(vendorsURL, category,
                new FirebaseHelper.CollectionReadListener() {
                    @Override
                    public void handleDocuments(Task<QuerySnapshot> task) {
                        task.addOnSuccessListener(queryDocumentSnapshots -> {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();

                                addDataToList(queryDocumentSnapshots);
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Map<String, Object> keySortedtreeMap = convertToTreeMap(documentSnapshot.getData());
                                    String vendorId = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_VENDOR_ID).toString();
                                    String area = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_AREA).toString();
                                    String pincode = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_PINCODE).toString();
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
                                    ArrayList categoriesItem = new ArrayList();
                                    if (keySortedtreeMap.get(FirebaseHelper.DOC_KEY_CATEGORIES) != null) {
                                        categoriesItem = (ArrayList) keySortedtreeMap.get(FirebaseHelper.DOC_KEY_CATEGORIES);
                                    }

                                    VendorInfo vendorInfo = new VendorInfo(name, address, area, city, state, pincode, mobile, type, vendorId, status, openTime, closeTime, lattitude, longitude, categoriesItem);
                                    vendorList.add(vendorInfo);
                                    mAdapter.notifyDataSetChanged();
                                }

                                lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                                System.out.println("Last visible:" + lastVisible.toString());


                                RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                            isScrolling = true;
                                        }
                                    }

                                    @Override
                                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);

                                        LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                                        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                                        int visibleItemCount = linearLayoutManager.getChildCount();
                                        int totalItemCount = linearLayoutManager.getItemCount();
                                        if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                            isScrolling = false;
                                            Toast.makeText(getActivity(), "Scroll clicked", Toast.LENGTH_SHORT).show();
                                            firebaseHelper.readCollectionFromFirestoreWithQuery(vendorsURL, category,
                                                    new FirebaseHelper.CollectionReadListener() {
                                                        @Override
                                                        public void handleDocuments(Task<QuerySnapshot> task) {
                                                            task.addOnSuccessListener(queryDocumentSnapshots -> {
                                                                if (task.isSuccessful()) {
                                                                    vendorList.clear();
                                                                    System.out.println("Query document size:" + queryDocumentSnapshots.size());
                                                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                                        Map<String, Object> keySortedtreeMap = convertToTreeMap(documentSnapshot.getData());
                                                                        String vendorId = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_VENDOR_ID).toString();
                                                                        String area = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_AREA).toString();
                                                                        String pincode = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_PINCODE).toString();
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
                                                                        ArrayList categoriesItem = new ArrayList();
                                                                        if (keySortedtreeMap.get(FirebaseHelper.DOC_KEY_CATEGORIES) != null) {
                                                                            categoriesItem = (ArrayList) keySortedtreeMap.get(FirebaseHelper.DOC_KEY_CATEGORIES);
                                                                        }

                                                                        VendorInfo vendorInfo = new VendorInfo(name, address, area, city, state, pincode, mobile, type, vendorId, status, openTime, closeTime, lattitude, longitude, categoriesItem);
                                                                        vendorList.add(vendorInfo);
                                                                        mAdapter.notifyDataSetChanged();
                                                                    }
                                                                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                                                                    System.out.println("Last visible:" + lastVisible.toString());
                                                                    if (queryDocumentSnapshots.getDocuments().size() < FirebaseHelper.LIMIT) {
                                                                        Toast.makeText(getActivity(), "Last items", Toast.LENGTH_SHORT).show();
                                                                        isLastItemReached = true;
                                                                    }
                                                                }
                                                            });
                                                        }

                                                        @Override
                                                        public void onDocumentsReadError(Exception error) {
                                                            progressDialog.dismiss();
                                                        }
                                                    });
                                        }
                                    }
                                };
                                recyclerView.addOnScrollListener(onScrollListener);
                            }
                        }).addOnFailureListener(e -> Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onDocumentsReadError(Exception error) {
                        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void addDataToList(QuerySnapshot queryDocumentSnapshots) {


    }

    private void getVendorInfoList() {
        progressDialog.show();
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        vendorsURL = null;
        vendorsURL = FirebaseHelper.ROOT_COL_VENDORS;
        firebaseHelper.readCollectionFromFirestoreWithQuery(vendorsURL, category,
                new FirebaseHelper.CollectionReadListener() {
                    @Override
                    public void handleDocuments(Task<QuerySnapshot> task) {
                        task.addOnSuccessListener(queryDocumentSnapshots -> {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                vendorList.clear();

                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Map<String, Object> keySortedtreeMap = convertToTreeMap(documentSnapshot.getData());
                                    String vendorId = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_VENDOR_ID).toString();
                                    String area = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_AREA).toString();
                                    String pincode = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_PINCODE).toString();
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
                                    ArrayList categoriesItem = new ArrayList();
                                    if (keySortedtreeMap.get(FirebaseHelper.DOC_KEY_CATEGORIES) != null) {
                                        categoriesItem = (ArrayList) keySortedtreeMap.get(FirebaseHelper.DOC_KEY_CATEGORIES);
                                    }

                                    VendorInfo vendorInfo = new VendorInfo(name, address, area, city, state, pincode, mobile, type, vendorId, status, openTime, closeTime, lattitude, longitude, categoriesItem);
                                    vendorList.add(vendorInfo);
                                    mAdapter.notifyDataSetChanged();
                                    lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                                    System.out.println("Last visible:" + lastVisible.toString());

                                    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                                        @Override
                                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                            super.onScrollStateChanged(recyclerView, newState);
                                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                                isScrolling = true;
                                            }
                                        }

                                        @Override
                                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);

                                            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                                            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                                            int visibleItemCount = linearLayoutManager.getChildCount();
                                            int totalItemCount = linearLayoutManager.getItemCount();

                                            System.out.println("COUNT:" + firstVisibleItemPosition + visibleItemCount + totalItemCount);
                                            if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                                                isScrolling = false;
                                                Toast.makeText(getActivity(), "Scroll clicked", Toast.LENGTH_SHORT).show();
                                                firebaseHelper.readCollectionFromFirestoreWithNextData(vendorsURL, category, lastVisible,
                                                        new FirebaseHelper.CollectionReadListener() {
                                                            @Override
                                                            public void handleDocuments(Task<QuerySnapshot> task) {
                                                                task.addOnSuccessListener(queryDocumentSnapshots -> {
                                                                    if (task.isSuccessful()) {
                                                                        vendorList.clear();
                                                                        System.out.println("Query document size:" + queryDocumentSnapshots.size());
                                                                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                                            Map<String, Object> keySortedtreeMap = convertToTreeMap(documentSnapshot.getData());
                                                                            String vendorId = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_VENDOR_ID).toString();
                                                                            String area = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_AREA).toString();
                                                                            String pincode = keySortedtreeMap.get(FirebaseHelper.DOC_KEY_PINCODE).toString();
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
                                                                            ArrayList categoriesItem = new ArrayList();
                                                                            if (keySortedtreeMap.get(FirebaseHelper.DOC_KEY_CATEGORIES) != null) {
                                                                                categoriesItem = (ArrayList) keySortedtreeMap.get(FirebaseHelper.DOC_KEY_CATEGORIES);
                                                                            }

                                                                            VendorInfo vendorInfo = new VendorInfo(name, address, area, city, state, pincode, mobile, type, vendorId, status, openTime, closeTime, lattitude, longitude, categoriesItem);
                                                                            vendorList.add(vendorInfo);
                                                                            mAdapter.notifyDataSetChanged();
                                                                        }
                                                                        lastVisible = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                                                                        System.out.println("Last visible:" + lastVisible.toString());
                                                                        if (queryDocumentSnapshots.size() < FirebaseHelper.LIMIT) {
                                                                            Toast.makeText(getActivity(), "Last items", Toast.LENGTH_SHORT).show();
                                                                            isLastItemReached = true;
                                                                        }
                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onDocumentsReadError(Exception error) {
                                                                progressDialog.dismiss();
                                                            }
                                                        });
                                            }
                                        }
                                    };
                                    recyclerView.addOnScrollListener(onScrollListener);
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
}
