package com.phongnguyen.cloudsyncdemo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.phongnguyen.cloudsyncdemo.R;
import com.phongnguyen.cloudsyncdemo.dropbox.FileThumbnailRequestHandler;
import com.phongnguyen.cloudsyncdemo.models.MyFile;
import com.phongnguyen.cloudsyncdemo.util.CommonUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by phongnguyen on 8/24/16.
 */
public class FileDisplayAdapter extends RecyclerView.Adapter<FileDisplayAdapter.FileDisplayViewHolder> {

    private Picasso mPicasso;
    private Callback mCallback;
    private ArrayList<MyFile> myFiles;

    public void setFiles(ArrayList<MyFile> files){
        this.myFiles = files;
        notifyDataSetChanged();
    }

    public FileDisplayAdapter(Picasso picasso,Callback callback){
        this.mPicasso = picasso;
        this.mCallback = callback;
    }

    @Override
    public FileDisplayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.file_item, parent, false);
        return new FileDisplayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FileDisplayViewHolder holder, int position) {
        holder.bind(myFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return myFiles.size();
    }

    public interface Callback {
        void onFolderClicked();
    }



    public class FileDisplayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvName,tvSize,tvLastModified;
        private final ImageView mImageView;
        private MyFile mItem;

        public FileDisplayViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.thumb);
            tvName = (TextView)itemView.findViewById(R.id.name);
            tvSize = (TextView)itemView.findViewById(R.id.size);
            tvLastModified = (TextView)itemView.findViewById(R.id.last_mod);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                mCallback.onFolderClicked();
        }

        public void bind(MyFile item) {
            mItem = item;
            tvName.setText(CommonUtils.makeFileName(item));
            tvLastModified.setText(CommonUtils.makeDateText(item.getLastModified()));
            // Load based on file path
            // Prepending a magic scheme to get it to
            // be picked up by DropboxPicassoRequestHandler

            if (!item.isDir()) {
                tvSize.setText(CommonUtils.makeSizeText(item.getSize()));
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String ext = item.getExtension();
                String type = mime.getMimeTypeFromExtension(ext);
                if (type != null && type.startsWith("image/")) {
                    mPicasso.load(R.drawable.ic_photo_grey_600_36dp)
                            .error(R.drawable.ic_photo_grey_600_36dp)
                            .into(mImageView);
                } else {
                    mPicasso.load(R.drawable.ic_insert_drive_file_blue_36dp)
                            .noFade()
                            .into(mImageView);
                }
            } else{
                mPicasso.load(R.drawable.ic_folder_blue_36dp)
                        .noFade()
                        .into(mImageView);
            }
        }
    }
}
