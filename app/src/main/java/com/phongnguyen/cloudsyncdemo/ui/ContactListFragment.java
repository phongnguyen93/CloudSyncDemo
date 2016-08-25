package com.phongnguyen.cloudsyncdemo.ui;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.phongnguyen.cloudsyncdemo.R;
import com.phongnguyen.cloudsyncdemo.ui.adapter.ContactAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactListFragment  extends Fragment implements
        AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> , View.OnClickListener  {


    public static final String CONTACT_PERMISSION = "contact_permission";
    private boolean isPermissionGranted;
    private LinearLayout emptyLayout;
    private OnFragmentInteractionListener mListener;
    private ContactAdapter mAdapter;
    private ListView listView;

    public ContactListFragment() {
        // Required empty public constructor
    }
    public static ContactListFragment newInstance(boolean isPermissionGranted) {
        ContactListFragment   fragment = new ContactListFragment();
        Bundle b = new Bundle();
        b.putBoolean(CONTACT_PERMISSION,isPermissionGranted);
        fragment.setArguments(b);
        return fragment;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_contact_list, container, false);
        isPermissionGranted = getArguments().getBoolean(CONTACT_PERMISSION);
        emptyLayout = (LinearLayout)v.findViewById(R.id.empty_layout);
        listView = (ListView)v.findViewById(R.id.contact_list);
        Button btnGrantPermission = (Button)v.findViewById(R.id.btnGrantPermission);
        btnGrantPermission.setOnClickListener(this);
        if(isPermissionGranted){
            emptyLayout.setVisibility(View.GONE);
            mAdapter = new ContactAdapter(getContext());
            getLoaderManager().initLoader(ContactAdapter.ContactsQuery.QUERY_ID, null, this);
            listView.setAdapter(mAdapter);
        }else
        {
            emptyLayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnGrantPermission)
            mListener.onFragmentInteraction(view.getId());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> cursorLoader = null;
        if (id == ContactAdapter.ContactsQuery.QUERY_ID) {
            Uri contentUri = ContactAdapter.ContactsQuery.CONTENT_URI;

            cursorLoader = new CursorLoader(getActivity(),
                    contentUri,
                    ContactAdapter.ContactsQuery.PROJECTION,
                    ContactAdapter.ContactsQuery.SELECTION,
                    null,
                    ContactAdapter.ContactsQuery.SORT_ORDER);
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == ContactAdapter.ContactsQuery.QUERY_ID) {
            Log.d("cursor_count",data.getCount()+"");
            mAdapter.swapCursor(data);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == ContactAdapter.ContactsQuery.QUERY_ID) {
            // When the loader is being reset, clear the cursor from the adapter. This allows the
            // cursor resources to be freed.
            mAdapter.swapCursor(null);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final Cursor cursor = mAdapter.getCursor();

        // Moves to the Cursor row corresponding to the ListView item that was clicked
        cursor.moveToPosition(i);

        // Creates a contact lookup Uri from contact ID and lookup_key
        final Uri uri = ContactsContract.Contacts.getLookupUri(
                cursor.getLong(ContactAdapter.ContactsQuery.ID),
                cursor.getString(ContactAdapter.ContactsQuery.LOOKUP_KEY));


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int id);
    }

}
