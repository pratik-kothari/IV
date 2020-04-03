package in.cmile.iv.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import in.cmile.iv.R;
import in.cmile.iv.helper.ConstantHelper;

/**
 * Created by pintu on 2020-03-31
 */
public class RegisterMobileActivity extends AppCompatActivity {
    private Button bLogin;
    private EditText etMobileNumber;

    private String mobileNo;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mobile);

        initView();

    }


    private void initView() {
        progressDialog = new ProgressDialog(getApplicationContext());
        bLogin = findViewById(R.id.f_login_with_mobile_verification_b_login);
        etMobileNumber = findViewById(R.id.f_login_with_mobile_verification_et_mobile_number);


        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                mobileNo = etMobileNumber.getEditableText().toString();
                if (!mobileValidation(mobileNo)) {
                    progressDialog.dismiss();
                } else {
                    startActivity(new Intent(RegisterMobileActivity.this, OtpVerificationActivity.class));
                    progressDialog.dismiss();
                }
                bLogin.setEnabled(false);
            }
        });
    }

    private boolean mobileValidation(String mobileNo) {
        if (mobileNo.isEmpty() || mobileNo.length() < ConstantHelper.MOBILE_LENGTH) {
            etMobileNumber.setError(getResources().getString(R.string.enter_valid_number));
            etMobileNumber.requestFocus();
            return false;
        }
        return true;
    }
}
