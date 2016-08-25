package com.phongnguyen.cloudsyncdemo;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.v2.users.FullAccount;
import com.phongnguyen.cloudsyncdemo.api.interfaces.ApiInterface;
import com.phongnguyen.cloudsyncdemo.dropbox.DropboxClientFactory;
import com.phongnguyen.cloudsyncdemo.dropbox.UriHelpers;
import com.phongnguyen.cloudsyncdemo.dropbox.task.GetCurrentAccountTask;
import com.phongnguyen.cloudsyncdemo.models.ApiResponse;
import com.phongnguyen.cloudsyncdemo.models.MetaData;
import com.phongnguyen.cloudsyncdemo.models.MyFile;
import com.phongnguyen.cloudsyncdemo.models.MyFolder;
import com.phongnguyen.cloudsyncdemo.models.Session;
import com.phongnguyen.cloudsyncdemo.ui.CloudFilesFragment;
import com.phongnguyen.cloudsyncdemo.ui.ContactListFragment;
import com.phongnguyen.cloudsyncdemo.ui.DropboxActivity;
import com.phongnguyen.cloudsyncdemo.ui.MyFilesFragment;
import com.phongnguyen.cloudsyncdemo.ui.dialog.CreateFolderDialogFragment;
import com.phongnguyen.cloudsyncdemo.ui.dialog.LoadingDialog;
import com.phongnguyen.cloudsyncdemo.util.CommonUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends DropboxActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , CloudFilesFragment.OnFragmentInteractionListener
        ,MyFilesFragment.OnFragmentInteractionListener ,AlertDialog.OnClickListener,
        ContactListFragment.OnFragmentInteractionListener,CreateFolderDialogFragment.DialogCallback{

    private static final int PICKFILE_REQUEST_CODE = 1;
    public static final int DEFAULT_TOTAL_CHUNK = 1;
    public static final String DEFAULT_DEST = "/";
    public static final String  DEFAULT_TOKEN = "5075284997574d7f84dd8334a7c1d284";

    // upload post method params
    public static final String CONTENT_TYPE = "multipart/form-data";
    public static final String UPLOAD_PARAM_ID = "upload_id";
    public static final String UPLOAD_PARAM_FILE = "file";
    public static final String UPLOAD_PARAM_NAME = "name";
    public static final String UPLOAD_PARAM_DEST = "dest";
    public static final String UPLOAD_PARAM_TOTAL_SIZE = "reported_total_size";
    public static final String UPLOAD_PARAM_OFFSET = "offset";
    public static final String UPLOAD_PARAM_CHUNK = "chunk";
    public static final String UPLOAD_PARAM_TOTAL_CHUNK = "chunks";

    //request read contact constant
    private static final int REQUEST_PERMISSIONS = 20;
    public static final String[] requestedPermissions = {Manifest.permission.READ_CONTACTS};

    //folder action constant
    public static final String ACTION_NEW = "new";
    public static final String ACTION_RENAME = "rename";
    public static final String ACTION_MOVE = "move";
    public static final String ACTION_DELETE = "delete";

    //folder action params
    public static final String PARAM_FOLDER_PATH = "file_path";
    public static final String PARAM_FOLDER_NAME = "folder_name";

    private boolean hasToken;
    private boolean hasCheckedPermission =false;

    private Session mCurrentSession;
    private MetaData mMetaData;
    private MyFolder myFolder;
    private SparseIntArray mErrorString;



    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    Retrofit mRetrofit;

    @Inject
    ApiInterface mApiInterface;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mErrorString = new SparseIntArray();
        ((MyApplication) getApplication()).getMainComponent().inject(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getFolder(DEFAULT_DEST);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    File file = UriHelpers.getFileForUri(this, data.getData());
                    if (file != null)
                        requestSession(file, DEFAULT_DEST, DEFAULT_TOTAL_CHUNK);

                }catch (Exception ex)
                {
                    Log.e("error",ex.getMessage());
                }
            }

        }
    }


    private MetaData getFileMetaData(File file){
        mMetaData = new MetaData();
        mMetaData.setName(file.getName());
        mMetaData.setDest(DEFAULT_DEST); //default destination on server: /
        mMetaData.setOffset(0); //default offset
        mMetaData.setChunk(0); //current chunk ( default = 0 if file size < 8Mb
        mMetaData.setTotalSize(file.length());
        mMetaData.setTotalChunk(DEFAULT_TOTAL_CHUNK); //default total chunk = 1 if file size < 8Mb
        return  mMetaData;
    }

    private void getFolder(String folderPath){
        final String name = CommonUtils.getFolderNameFromPath(folderPath);
        Call<MyFolder> browse = mApiInterface.browse(DEFAULT_TOKEN,folderPath);
        browse.enqueue(new Callback<MyFolder>() {
            @Override
            public void onResponse(Call<MyFolder> call, Response<MyFolder> response) {
                if (response.isSuccessful()) {
                    myFolder = response.body();
                    Log.d("file_count_get",myFolder.getTotalFiles()+"");
                    Log.d("file_count_actual",myFolder.getFileList().size()+"");
                    myFolder.setName(name);
                    if(myFolder!=null)
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.content, MyFilesFragment.newInstance(myFolder)).commit();
                }
            }

            @Override
            public void onFailure(Call<MyFolder> call, Throwable t) {

            }
        });

    }

    private void uploadFile(Map<String,RequestBody> params,File file) {
        MultipartBody.Part body = MultipartBody.Part.createFormData(UPLOAD_PARAM_FILE,file.getName()
                ,RequestBody.create(MediaType.parse(CONTENT_TYPE),file));
        Call<ApiResponse> upload = mApiInterface.uploadFile(DEFAULT_TOKEN,params,body);
        upload.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),response.body().getMessage()+" - Uploaded",Toast.LENGTH_SHORT).show();

                } else
                    if (response.code() == 400) {
                        try {
                            Log.v("Error code 400",response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });


    }

    private Map<String, RequestBody> createParamsMap(Session session, MetaData metaData) {
        Map<String, RequestBody> params = new HashMap<>();
        params.put(UPLOAD_PARAM_ID,RequestBody.create(MediaType.parse(CONTENT_TYPE)
                ,session.getSessionId()));
        params.put(UPLOAD_PARAM_NAME,RequestBody.create(MediaType.parse(CONTENT_TYPE)
                ,metaData.getName()));
        params.put(UPLOAD_PARAM_DEST,RequestBody.create(MediaType.parse(CONTENT_TYPE)
                ,metaData.getDest()));
        params.put(UPLOAD_PARAM_TOTAL_SIZE,RequestBody.create(MediaType.parse(CONTENT_TYPE)
                ,String.valueOf(metaData.getTotalSize())));
        params.put(UPLOAD_PARAM_OFFSET,RequestBody.create(MediaType.parse(CONTENT_TYPE)
                ,String.valueOf(metaData.getOffset())));
        params.put(UPLOAD_PARAM_CHUNK,RequestBody.create(MediaType.parse(CONTENT_TYPE)
                ,String.valueOf(metaData.getChunk())));
        params.put(UPLOAD_PARAM_TOTAL_CHUNK,RequestBody.create(MediaType.parse(CONTENT_TYPE)
                ,String.valueOf(metaData.getTotalChunk())));
        return params;
    }

    private void requestSession(File file,String fileDest,int totalChunk) {
        final File mFile = file;
        Call<Session> request = mApiInterface.requestUploadSession(DEFAULT_TOKEN,mFile.getName(),fileDest,file.length(),totalChunk);
        request.enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if (response.isSuccessful()) {
                    mCurrentSession = response.body();
                    if (mCurrentSession != null)
                        uploadFile(createParamsMap(mCurrentSession, getFileMetaData(mFile)),mFile);
                } else {
                    Log.i("ERROR", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id== R.id.nav_my_files){
            getSupportFragmentManager().beginTransaction().replace(R.id.content,MyFilesFragment.newInstance(myFolder)).commit();
        }
        else if( id == R.id.nav_cloud_files) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, CloudFilesFragment.newInstance(hasToken)).commit();
        }else {
            if (hasCheckedPermission) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
                    onPermissionsGranted(true);
                else
                    onPermissionsGranted(false);
            } else
                requestAppPermissions(requestedPermissions, R.string.runtime_permissions_txt, REQUEST_PERMISSIONS);


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void launchFilePicker() {
        // Launch intent to pick file for upload
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void loadData() {

        new GetCurrentAccountTask(DropboxClientFactory.getClient(), new GetCurrentAccountTask.Callback() {
            @Override
            public void onComplete(FullAccount result) {
                if(hasToken()) {
                    hasToken = true;
                    Toast.makeText(MainActivity.this, result.getName().getDisplayName(), Toast.LENGTH_SHORT).show();
                }else {
                    hasToken = false;
                    Toast.makeText(MainActivity.this, "No access token", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(getClass().getName(), "Failed to get account details.", e);
            }
        }).execute();
    }

    @Override
    public void onFragmentInteraction(String fabAction) {
        switch (fabAction){
            case "createFolder":
                showCreateFolderDialog();
                break;
            case "uploadFile":
                launchFilePicker();
                break;
        }

    }

    private void showCreateFolderDialog() {
        CreateFolderDialogFragment.newInstance(myFolder).show(getSupportFragmentManager(),CreateFolderDialogFragment.class.getSimpleName());
    }

    @Override
    public void onFileInteraction() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(true);
        } else
            onPermissionsGranted(false);
    }

    //Launch Setting Intent to change permission manually
    private void changePermissionSetting(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }

    //Check permission and make request dialog if permission is not granted
    private void requestAppPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(this, permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                showRequestDiaglog();
            } else {
                onPermissionsGranted(false);
            }
        } else {
            onPermissionsGranted(true);
        }
        hasCheckedPermission = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    //BUild and show dialog to explain why this app need permission
    private void showRequestDiaglog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.common_ok, this)
                .setNegativeButton(R.string.common_cancel, this)
                .setMessage("This app need READ CONTACT permission to operate functionally")
                .setTitle("Request permission")
                .create().show();
    }

    //Launch contact fragment with state depend on permission is granted or not
    private void onPermissionsGranted(boolean isGranted){
        getSupportFragmentManager().beginTransaction().replace(R.id.content, ContactListFragment.newInstance(isGranted)).commitAllowingStateLoss();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == AlertDialog.BUTTON_POSITIVE) {
            ActivityCompat.requestPermissions(MainActivity.this, requestedPermissions, REQUEST_PERMISSIONS);
        }else if(i == AlertDialog.BUTTON_NEGATIVE){
            onPermissionsGranted(false);
        }
    }

    @Override
    public void onFragmentInteraction(int id) {
        if(id == R.id.btnGrantPermission){
            changePermissionSetting();
        }
    }

    @Override
    public void createFolderCallback(String folderPath, String folderName) {
        showProgressDialog("Processing...");
        folderAction(ACTION_NEW,createParamMapFolderAction(folderPath,folderName));
    }

    private void folderAction(String action,Map<String,RequestBody> params){
        Call<MyFile> folderAction = mApiInterface.folderAction(DEFAULT_TOKEN,action,params);
        folderAction.enqueue(new Callback<MyFile>() {
            @Override
            public void onResponse(Call<MyFile> call, Response<MyFile> response) {
                if(response.isSuccessful()){
                    dismissLoadingDialog();
                    if(response.body().getId()!=""){
                        refreshData();
                    }else
                        Log.e("folder_action:",response.message());
                }
            }

            @Override
            public void onFailure(Call<MyFile> call, Throwable t) {

            }
        });
    }

    private void refreshData() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, MyFilesFragment.newInstance(myFolder)).commitAllowingStateLoss();
        getFolder(DEFAULT_DEST);
    }

    private void showProgressDialog(String displayText){
        LoadingDialog.newInstance(displayText).show(getSupportFragmentManager(),LoadingDialog.class.getSimpleName());
    }

    public void dismissLoadingDialog() {
        Fragment frag = getSupportFragmentManager().findFragmentByTag(LoadingDialog.class.getSimpleName());
        if (frag != null) {
            LoadingDialog loading = (LoadingDialog) frag;
            loading.dismiss();
        }
    }

    private Map<String,RequestBody> createParamMapFolderAction(String folderPath, String folderName){
        Map<String, RequestBody> params = new HashMap<>();
        params.put(PARAM_FOLDER_PATH,RequestBody.create(MediaType.parse(CONTENT_TYPE)
                ,folderPath));
        params.put(PARAM_FOLDER_NAME,RequestBody.create(MediaType.parse(CONTENT_TYPE)
                ,folderName));
        return params;
    }
}

