package com.nistores.awesomeurch.nistores.Folders.Pages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nistores.awesomeurch.nistores.Folders.Adapters.BusinessLoungeAdapter;
import com.nistores.awesomeurch.nistores.Folders.Helpers.ApiUrls;
import com.nistores.awesomeurch.nistores.Folders.Helpers.BusinessLounge;
import com.nistores.awesomeurch.nistores.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //BusinessLoungeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BusinessLoungeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusinessLoungeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ApiUrls apiUrls;
    private String URL;

    private RecyclerView recyclerView;
    LinearLayout networkErrorLayout;
    private List<BusinessLounge> businessLoungeListList;
    private BusinessLoungeAdapter mAdapter;


    public BusinessLoungeFragment() {
        // Required empty public constructor
    }

    public static BusinessLoungeFragment newInstance() {
        BusinessLoungeFragment fragment = new BusinessLoungeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_business_lounge, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            Bundle args = getArguments();
        }

    }

}
