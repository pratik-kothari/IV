package in.cmile.iv.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import in.cmile.iv.R;
import in.cmile.iv.models.VendorInfo;

/**
 * Created by pintu on 2020-03-31
 */
public class VendorDetailsActivity extends AppCompatActivity {
    TextView tvName, tvAddress, tvCity, tvState, tvPincode, tvMobileNo;

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


        VendorInfo vendorInfo = (VendorInfo) getIntent().getSerializableExtra("vendorInfo");

        if (vendorInfo != null) {
            tvName.setText(vendorInfo.getName());
            tvAddress.setText(vendorInfo.getAddress());
            tvCity.setText(vendorInfo.getCity());
            tvState.setText(vendorInfo.getState());
            tvPincode.setText(vendorInfo.getPincode());
        }
    }
}
