package in.cmile.iv.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import in.cmile.iv.R;
import in.cmile.iv.helper.ConstantHelper;

/**
 * Created by pintu on 2020-03-31
 */
public class OtpVerificationActivity extends AppCompatActivity {

    private String mobileNumber;
    private Bundle bundle;
    private View view;
    private EditText etOTP;
    private TextView tvResendOTP;
    private Button bLogin;
    private String verificationId, registrationCode;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    //    private SharedPreferenceHelper sharedPreferenceHelper;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        etOTP.setText(code);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException exception) {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken token) {
                    super.onCodeSent(s, token);
                    verificationId = s;
                    resendToken = token;
                }
            };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        bundle = getIntent().getExtras();
        mobileNumber = bundle.getString(ConstantHelper.PD_SELECTED_MOBILE_NUMBER);
        sendVerificationCode(mobileNumber);
        initView();
        initModel();
    }


    private void initModel() {
        //stored registration code into SharedPreference
//        sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());
//        registrationCode = sharedPreferenceHelper.getRegistrationCode();
    }

    private void initView() {
        progressDialog = new ProgressDialog(getApplicationContext());
        auth = FirebaseAuth.getInstance();
        etOTP = view.findViewById(R.id.f_login_with_mobile_verification_et_otp);
        bLogin = view.findViewById(R.id.f_login_with_mobile_verification_b_login);
        tvResendOTP = view.findViewById(R.id.f_login_with_mobile_verification_tv_resend_otp);


        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = etOTP.getText().toString().trim();
                if (code.isEmpty() || code.length() < ConstantHelper.CODE_LENGTH) {
                    etOTP.setError(getString(R.string.enter_valid_code));
                    etOTP.requestFocus();
                    return;
                }
                verifyVerificationCode(code);
            }
        });

        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode(mobileNumber);
            }
        });
    }

    private void resendVerificationCode(String mobileNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                ConstantHelper.MOBILE_PREFIX_CODE + mobileNumber,
                ConstantHelper.OTP_SECONDS,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks,
                resendToken);
    }

    private void sendVerificationCode(String mobileNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                ConstantHelper.MOBILE_PREFIX_CODE + mobileNumber,
                ConstantHelper.OTP_SECONDS,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private void verifyVerificationCode(String code) {
        progressDialog.show();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
//        signInWithPhoneAuthCredential(credential);
    }

//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        auth.signInWithCredential(credential)
//                .addOnCompleteListener((Activity) getApplicationContext(), task -> {
//                    if (task.isSuccessful()) {
//                        progressDialog.dismiss();
//                        SharedPreferenceHelper sharedPreferenceHelper = new
//                                SharedPreferenceHelper(getApplicationContext());
//
//                        sharedPreferenceHelper.setUserMobileNumber(mobileNumber);
//                        FirebaseHelper firebaseHelper = new FirebaseHelper();
//
//                        String userURL = FirebaseHelper.ROOT_COLLECTION_USERS
//                                + FirebaseHelper.SEPARATOR
//                                + auth.getCurrentUser().getUid();
//
//                        firebaseHelper.readDocumentFromFirestore(userURL, new FirebaseHelper.DocumentReadListener() {
//                            @Override
//                            public void handleReadDocument(DocumentSnapshot documentSnapshot) {
//                                if (documentSnapshot == null) {
//                                    //user should be registered on Firebase
//                                    writeUserDetailsToFirebase();
//                                } else {
//                                    //user sign in
//                                    getUserDetails();
//                                    ((LoginRegisterActivity) getActivity()).goToMainActivity();
//                                }
//                            }
//
//                            @Override
//                            public void onDocumentReadError(Exception error) {
//
//                            }
//                        });
//
//                    } else {
//                        progressDialog.dismiss();
//                        if (task.getException() != null) {
//                            Toast.makeText(getContext(), task.getException().getLocalizedMessage(),
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
}
