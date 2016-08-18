package com.phongnguyen.cloudsyncdemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.android.Auth;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CloudFilesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CloudFilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CloudFilesFragment extends Fragment implements View.OnClickListener {


    private OnFragmentInteractionListener mListener;
    private boolean hasToken;
    private static final String HAS_TOKEN = "hasToken";


    public CloudFilesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CloudFilesFragment newInstance(boolean hasToken) {
        CloudFilesFragment fragment = new CloudFilesFragment();
        Bundle b= new Bundle();
        b.putBoolean(HAS_TOKEN,hasToken);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.hasToken= getArguments().getBoolean(HAS_TOKEN);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cloud_files, container, false);
        Button dropboxButton = (Button)v.findViewById(R.id.add_dropbox_button);
        Button ggdriveButton = (Button)v.findViewById(R.id.add_ggdrive_button);
        ggdriveButton.setOnClickListener(this);
        dropboxButton.setOnClickListener(this);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.add_dropbox_button) {
            if(!hasToken)
                Auth.startOAuth2Authentication(getActivity(), getString(R.string.app_key));
            else
            {
                startActivity(FilesActivity.getIntent(getActivity(),""));
            }
        }else if (view.getId() == R.id.add_ggdrive_button){
            startActivity(new Intent(getActivity(),GGDriveActivity.class));
        }

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

