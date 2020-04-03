package in.cmile.iv.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import in.cmile.iv.R;
import in.cmile.iv.activity.BottomNavigation;

/**
 * Created by pintu on 2020-04-01
 */
public class FavouriteFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
        getActivity().setTitle(R.string.title_favourite);
        ((BottomNavigation) getActivity()).setBNVVisibility(View.VISIBLE);

        return rootView;
    }
}
