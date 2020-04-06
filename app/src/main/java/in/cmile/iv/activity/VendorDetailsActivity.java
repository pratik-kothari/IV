package in.cmile.iv.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import in.cmile.iv.R;
import in.cmile.iv.helper.FirebaseHelper;
import in.cmile.iv.models.VendorInfo;

/**
 * Created by pintu on 2020-03-31
 */
public class VendorDetailsActivity extends AppCompatActivity {
    TextView tvName, tvAddress, tvCity, tvState, tvPincode, tvMobileNo, tvMorningTime, tvEveningTime, tvShopStatus;
    LinearLayout llVehicle, llStore, llVegetables, llFruits, llDairyProducts, llGeneralStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_details);

        tvName = findViewById(R.id.tv_shop_name);
        tvAddress = findViewById(R.id.tv_address);
        tvCity = findViewById(R.id.tv_city);
        tvState = findViewById(R.id.tv_state);
        tvPincode = findViewById(R.id.tv_pincode);
        tvMobileNo = findViewById(R.id.tv_mobile_no);
        tvMorningTime = findViewById(R.id.tv_morning_time);
        tvEveningTime = findViewById(R.id.tv_evening_time);
        tvShopStatus = findViewById(R.id.tv_status);
        llVehicle = findViewById(R.id.ll_vehicle);
        llStore = findViewById(R.id.ll_store);
        llVegetables = findViewById(R.id.ll_vegetables);
        llFruits = findViewById(R.id.ll_fruits);
        llDairyProducts = findViewById(R.id.ll_dairy_products);
        llGeneralStore = findViewById(R.id.ll_general_store);

        VendorInfo vendorInfo = (VendorInfo) getIntent().getSerializableExtra("vendorInfo");

        if (vendorInfo != null) {
            tvName.setText(vendorInfo.getName());
            tvAddress.setText(vendorInfo.getAddress());
            tvCity.setText(vendorInfo.getCity());
            tvState.setText(vendorInfo.getState());
            tvPincode.setText(vendorInfo.getPincode());
            tvMobileNo.setText(vendorInfo.getMobileNo());

            tvMorningTime.setText(vendorInfo.getOpenTime());
            tvEveningTime.setText((vendorInfo.getCloseTime()));

            System.out.println("Vendor Info:" + vendorInfo.toString());
            if (vendorInfo.getStatus().equalsIgnoreCase("true")) {
                tvShopStatus.setText(R.string.open);
            } else {
                tvShopStatus.setText(R.string.close);
            }

            if (vendorInfo.getType().equalsIgnoreCase("vehicle")) {
                llVehicle.setVisibility(View.VISIBLE);
            } else {
                llStore.setVisibility(View.VISIBLE);
            }

            if (vendorInfo.getCategoryItem().contains(FirebaseHelper.CATEGORY_FRUITS)) {
                llFruits.setVisibility(View.VISIBLE);
            }
            if (vendorInfo.getCategoryItem().contains(FirebaseHelper.CATEGORY_VEGETABLES)) {
                llVegetables.setVisibility(View.VISIBLE);
            }
            if (vendorInfo.getCategoryItem().contains(FirebaseHelper.CATEGORY_DAIRY_PRODUCTS)) {
                llDairyProducts.setVisibility(View.VISIBLE);
            }
            if (vendorInfo.getCategoryItem().contains(FirebaseHelper.CATEGORY_GENERAL_STORE)) {
                llGeneralStore.setVisibility(View.VISIBLE);
            }
        }
    }
}
