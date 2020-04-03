package in.cmile.iv.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

import in.cmile.iv.R;
import in.cmile.iv.fragments.DashboardFragment;
import in.cmile.iv.fragments.FavouriteFragment;
import in.cmile.iv.fragments.StoreFragment;
import in.cmile.iv.helper.FragmentHelper;

public class BottomNavigation extends AppCompatActivity {
    //helper
    private FragmentHelper fragmentHelper;
    private int visibilityValue;
    private DashboardFragment dashboardFragment;
    private FavouriteFragment favouriteFragment;
    private StoreFragment storeFragment;
    private BottomNavigationView bnvMainBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        bnvMainBar = findViewById(R.id.bottom_nav);
        initFirebase();
        initView();
        goToDashboardFragment(FragmentHelper.ADD_FRAGMENT, false, null);
    }


    private void initView() {

        fragmentHelper = new FragmentHelper();
        bnvMainBar = findViewById(R.id.bottom_nav);

        bnvMainBar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_dashboard:

                                fragmentHelper = new FragmentHelper();
                                goToDashboardFragment(FragmentHelper.REPLACE_FRAGMENT,
                                        false, null);
                                return true;
                            case R.id.navigation_favourite:
                                fragmentHelper = new FragmentHelper();
                                goToFavouriteFragment(FragmentHelper.REPLACE_FRAGMENT,
                                        false, null);
                                return true;

                            case R.id.navigation_store:
                                fragmentHelper = new FragmentHelper();
                                goToStoreFragment(FragmentHelper.REPLACE_FRAGMENT,
                                        false, null);
                                return true;
                        }
                        return false;
                    }
                });
        initHelpers();
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
    }

    private void initHelpers() {
        fragmentHelper = new FragmentHelper();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getSupportFragmentManager().getFragments().get(
                        getSupportFragmentManager().getFragments().size() - 1);

                if (fragment instanceof DashboardFragment) {
                    setBNVVisibility(View.VISIBLE);
                }

                if (fragment instanceof FavouriteFragment) {
                    setBNVVisibility(View.VISIBLE);
                }

                if (fragment instanceof StoreFragment) {
                    setBNVVisibility(View.VISIBLE);
                }
            }
        });
    }

    public int setBNVVisibility(int visibility) {
        visibilityValue = visibility;
        bnvMainBar.setVisibility(visibility);
        return visibility;
    }


    public void goToDashboardFragment(int action, boolean addToStack, String tag) {
        dashboardFragment = new DashboardFragment();
        fragmentHelper.changeFragment(getSupportFragmentManager(), R.id.a_main_fl_container,
                dashboardFragment, action, addToStack, tag);
    }

    public void goToFavouriteFragment(int action, boolean addToStack, String tag) {
        favouriteFragment = new FavouriteFragment();
        fragmentHelper.changeFragment(getSupportFragmentManager(), R.id.a_main_fl_container,
                favouriteFragment, action, addToStack, tag);
    }

    public void goToStoreFragment(int action, boolean addToStack, String tag) {
        storeFragment = new StoreFragment();
        fragmentHelper.changeFragment(getSupportFragmentManager(), R.id.a_main_fl_container,
                storeFragment, action, addToStack, tag);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            if (visibilityValue == View.VISIBLE) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BottomNavigation.this);
                builder.setMessage(getString(R.string.are_you_sure_you_want_to_exist));
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BottomNavigation.super.onBackPressed();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            } else {
                super.onBackPressed();

            }
        }
    }
}
