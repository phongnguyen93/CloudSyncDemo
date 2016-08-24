package com.phongnguyen.cloudsyncdemo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dropbox.core.v1.DbxEntry;
import com.phongnguyen.cloudsyncdemo.MainActivity;
import com.phongnguyen.cloudsyncdemo.R;
import com.phongnguyen.cloudsyncdemo.dropbox.PicassoClient;
import com.phongnguyen.cloudsyncdemo.models.MyFile;
import com.phongnguyen.cloudsyncdemo.models.MyFolder;
import com.phongnguyen.cloudsyncdemo.ui.adapter.FileDisplayAdapter;
import com.phongnguyen.cloudsyncdemo.util.CommonUtils;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFilesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFilesFragment extends Fragment implements FileDisplayAdapter.Callback{


    private OnFragmentInteractionListener mListener;
    private MyFolder myFolder;
    private FileDisplayAdapter adapter;
    private TextView tvSummary;
    public MyFilesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MyFilesFragment newInstance(MyFolder myFolder) {
        MyFilesFragment fragment = new MyFilesFragment();
        Bundle b = new Bundle();
        b.putParcelable("folder",myFolder);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_files, container, false);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction();
            }
        });
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.rvFileDisplay);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new FileDisplayAdapter(PicassoClient.getPicasso(),this);
        tvSummary =(TextView)v.findViewById(R.id.tvSummary);

        recyclerView.setLayoutManager(linearLayoutManager);
        myFolder = getArguments().getParcelable("folder");
        tvSummary.setText(CommonUtils.makeFolderSummaryText(myFolder));
        if(myFolder != null)
        {
            adapter.setFiles(myFolder.getFileList());
            recyclerView.setAdapter(adapter);

        }

        return  v;
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
    public void onFolderClicked() {
        mListener.onFileInteraction();
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
        void onFragmentInteraction();
        void onFileInteraction();
    }
}
