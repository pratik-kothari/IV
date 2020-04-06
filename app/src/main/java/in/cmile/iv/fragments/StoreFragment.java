package in.cmile.iv.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import in.cmile.iv.R;
import in.cmile.iv.activity.BottomNavigation;

/**
 * Created by pintu on 2020-04-01
 */
public class StoreFragment extends Fragment {
    LinearLayout llVegetables, llFruits, llDairyProducts, llGeneralStore, llSubmit, llUpdateCancel, llVehicle, llStore, llToggleOpenClose;
    TextInputEditText edShopName, edMobileNo, edAddress, edCity, edState, edPincode, edArea;
    EditText edMorningTime, edEveningTime;
    ImageView ivLocation, ivVegitables, ivFruits, ivDairy, ivGeneralStore, ivVegitablesBorder, ivFruitsBorder, ivDairyBorder,
            ivGeneralStoreBorder, ivVehicleBorder, ivVehicle, ivStoreBorder, ivStore;
    View rootView;
    Button btnSubmit, btnCancel, btnUpdate;
    ToggleButton toggleOpenClose;
    private int flagVeg = 0;
    private int flagFruits = 0;
    private int flagDairy = 0;
    private int flagGeneral = 0;
    private int flagVehicle = 0;
    private int flagStore = 0;
    boolean isShopOpen = false;
    boolean isStoreSelected = false;
    boolean isVehicleSelected = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_store, container, false);

        getActivity().setTitle(R.string.title_store);
        ((BottomNavigation) getActivity()).setBNVVisibility(View.VISIBLE);

        init();
        return rootView;
    }

    private void init() {
        llVegetables = rootView.findViewById(R.id.ll_vegetables);
        llFruits = rootView.findViewById(R.id.ll_fruits);
        llDairyProducts = rootView.findViewById(R.id.ll_dairy_products);
        llGeneralStore = rootView.findViewById(R.id.ll_general_store);
        llSubmit = rootView.findViewById(R.id.ll_submit);
        llUpdateCancel = rootView.findViewById(R.id.ll_update_cancel);
        llVehicle = rootView.findViewById(R.id.ll_vehicle);
        llStore = rootView.findViewById(R.id.ll_store);
        llToggleOpenClose = rootView.findViewById(R.id.ll_toggle_open_close);

        edShopName = rootView.findViewById(R.id.ed_shop_name);
        edMobileNo = rootView.findViewById(R.id.ed_mobile_no);
        edAddress = rootView.findViewById(R.id.ed_address);
        edArea = rootView.findViewById(R.id.ed_area);
        edCity = rootView.findViewById(R.id.ed_city);
        edState = rootView.findViewById(R.id.ed_state);
        edPincode = rootView.findViewById(R.id.ed_pincode);
        edMorningTime = rootView.findViewById(R.id.ed_morning_time);
        edEveningTime = rootView.findViewById(R.id.ed_evening_time);

        ivLocation = rootView.findViewById(R.id.iv_location);
        ivVegitables = rootView.findViewById(R.id.iv_vegitables);
        ivFruits = rootView.findViewById(R.id.iv_fruits);
        ivDairy = rootView.findViewById(R.id.iv_dairy_products);
        ivGeneralStore = rootView.findViewById(R.id.iv_general_store);
        ivVegitablesBorder = rootView.findViewById(R.id.iv_vegitables_border);
        ivFruitsBorder = rootView.findViewById(R.id.iv_fruits_border);
        ivDairyBorder = rootView.findViewById(R.id.iv_dairy_products_border);
        ivGeneralStoreBorder = rootView.findViewById(R.id.iv_general_store_border);
        ivStoreBorder = rootView.findViewById(R.id.iv_store_border);
        ivVehicleBorder = rootView.findViewById(R.id.iv_vehicle_border);
        ivVehicle = rootView.findViewById(R.id.iv_vehicle);
        ivStore = rootView.findViewById(R.id.iv_store);

        btnSubmit = rootView.findViewById(R.id.btn_submit);
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        btnUpdate = rootView.findViewById(R.id.btn_update);

        toggleOpenClose = rootView.findViewById(R.id.toggle_open_close);
        llVegetables.setOnClickListener(view -> {
            if (flagVeg == 0) {
                ivVegitablesBorder.setVisibility(View.VISIBLE);
                ivVegitables.setVisibility(View.GONE);
                ivVegitablesBorder.setImageResource(R.drawable.ic_veggies);
                flagVeg = 1;
            } else if (flagVeg == 1) {
                ivVegitables.setVisibility(View.VISIBLE);
                ivVegitablesBorder.setVisibility(View.GONE);
                ivVegitables.setImageResource(R.drawable.ic_veggies_border);
                flagVeg = 0;
            }
        });

        llFruits.setOnClickListener(view -> {
            if (flagFruits == 0) {
                ivFruitsBorder.setVisibility(View.VISIBLE);
                ivFruits.setVisibility(View.GONE);
                ivFruitsBorder.setImageResource(R.drawable.ic_fruits);
                flagFruits = 1;
            } else if (flagFruits == 1) {
                ivFruits.setVisibility(View.VISIBLE);
                ivFruitsBorder.setVisibility(View.GONE);
                ivFruits.setImageResource(R.drawable.ic_fruits_border);
                flagFruits = 0;
            }
        });

        llDairyProducts.setOnClickListener(view -> {
            if (flagDairy == 0) {
                ivDairyBorder.setVisibility(View.VISIBLE);
                ivDairy.setVisibility(View.GONE);
                ivDairyBorder.setImageResource(R.drawable.ic_dairy);
                flagDairy = 1;
            } else if (flagDairy == 1) {
                ivDairy.setVisibility(View.VISIBLE);
                ivDairyBorder.setVisibility(View.GONE);
                ivDairy.setImageResource(R.drawable.ic_dairy_border);
                flagDairy = 0;
            }
        });

        llGeneralStore.setOnClickListener(view -> {
            if (flagGeneral == 0) {
                ivGeneralStoreBorder.setVisibility(View.VISIBLE);
                ivGeneralStore.setVisibility(View.GONE);
                ivGeneralStoreBorder.setImageResource(R.drawable.ic_general_store);
                flagGeneral = 1;
            } else if (flagGeneral == 1) {
                ivGeneralStore.setVisibility(View.VISIBLE);
                ivGeneralStoreBorder.setVisibility(View.GONE);
                ivGeneralStore.setImageResource(R.drawable.ic_general_store_border);
                flagGeneral = 0;
            }
        });

        llVehicle.setOnClickListener(view -> {
            if (flagVehicle == 0) {
                ivVehicleBorder.setVisibility(View.VISIBLE);
                ivVehicle.setVisibility(View.GONE);
                ivVehicleBorder.setImageResource(R.drawable.ic_vehicle);
                isVehicleSelected = false;
                flagVehicle = 1;

                ivStore.setVisibility(View.VISIBLE);
                ivStoreBorder.setVisibility(View.GONE);
                ivStore.setImageResource(R.drawable.ic_shop_border);
                flagStore = 0;

            } else if (flagVehicle == 1) {
                ivVehicle.setVisibility(View.VISIBLE);
                ivVehicleBorder.setVisibility(View.GONE);
                ivVehicle.setImageResource(R.drawable.ic_vehicle);
                flagVehicle = 0;

                ivStoreBorder.setVisibility(View.VISIBLE);
                ivStore.setVisibility(View.GONE);
                ivStoreBorder.setImageResource(R.drawable.ic_store);
                isStoreSelected = false;
                flagStore = 1;
            }
        });

        llStore.setOnClickListener(view -> {
            if (flagStore == 0) {
                ivStoreBorder.setVisibility(View.VISIBLE);
                ivStore.setVisibility(View.GONE);
                ivStoreBorder.setImageResource(R.drawable.ic_store);
                isStoreSelected = false;
                flagStore = 1;

                ivVehicle.setVisibility(View.VISIBLE);
                ivVehicleBorder.setVisibility(View.GONE);
                ivVehicle.setImageResource(R.drawable.ic_vehicle_border);
                flagVehicle = 0;

            } else if (flagStore == 1) {
                ivStore.setVisibility(View.VISIBLE);
                ivStoreBorder.setVisibility(View.GONE);
                ivStore.setImageResource(R.drawable.ic_shop_border);
                flagStore = 0;

                ivVehicleBorder.setVisibility(View.VISIBLE);
                ivVehicle.setVisibility(View.GONE);
                ivVehicleBorder.setImageResource(R.drawable.ic_vehicle);
                isVehicleSelected = false;
                flagVehicle = 1;
            }
        });

        toggleOpenClose.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isShopOpen = true;
                // The toggle is enabled
                Toast.makeText(getActivity(), "Shop Open", Toast.LENGTH_SHORT).show();
            } else {
                isShopOpen = false;
                // The toggle is disabled
                Toast.makeText(getActivity(), "Shop Close", Toast.LENGTH_SHORT).show();
            }
        });


        btnSubmit.setOnClickListener(view -> {

            String shopName = edShopName.getText().toString();
            String address = edAddress.getText().toString();
            String mobileNo = edMobileNo.getText().toString();
            String area = edArea.getText().toString();
            String state = edState.getText().toString();
            String city = edCity.getText().toString();
            String pincode = edPincode.getText().toString();
            String morningTime = edMorningTime.getText().toString();
            String eveningTime = edEveningTime.getText().toString();


        });
    }
}
