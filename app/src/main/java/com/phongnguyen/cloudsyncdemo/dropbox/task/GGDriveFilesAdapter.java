package com.phongnguyen.cloudsyncdemo.dropbox.task;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.internal.CloseContentsAndUpdateMetadataRequest;
import com.phongnguyen.cloudsyncdemo.R;
import com.phongnguyen.cloudsyncdemo.dropbox.FileThumbnailRequestHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adapter for file list
 */
public class GGDriveFilesAdapter extends RecyclerView.Adapter<GGDriveFilesAdapter.MetadataViewHolder> {
    private List<Metadata> mFiles;
    private Picasso mPicasso;
    private Callback mCallback;

    public void setFiles(List<Metadata> files) {
        mFiles = Collections.unmodifiableList(new ArrayList<>(files));
        notifyDataSetChanged();
    }

    public interface Callback {
        void onClicked(DriveId id,boolean isFolder);
    }

    public GGDriveFilesAdapter(Picasso picasso, Callback callback) {
        mPicasso = picasso;
        this.mCallback = callback;

    }

    @Override
    public MetadataViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.files_item, viewGroup, false);
        return new MetadataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MetadataViewHolder metadataViewHolder, int i) {
        metadataViewHolder.bind(mFiles.get(i));
    }

    @Override
    public long getItemId(int position) {
        return mFiles.get(position).getDriveId().hashCode();
    }

    @Override
    public int getItemCount() {
        return mFiles == null ? 0 : mFiles.size();
    }

    public class MetadataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mTextView;
        private final ImageView mImageView;
        private Metadata mItem;

        public MetadataViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView)itemView.findViewById(R.id.image);
            mTextView = (TextView)itemView.findViewById(R.id.text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCallback.onClicked(mItem.getDriveId(),mItem.isFolder());
        }
        public void bind(Metadata item) {
            mItem = item;
            if (!item.isFolder()) {
                mTextView.setText(mItem.getOriginalFilename());
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String ext = item.getFileExtension();
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
            } else  {
                mTextView.setText(mItem.getTitle());
                mPicasso.load(R.drawable.ic_folder_blue_36dp)
                        .noFade()
                        .into(mImageView);
            }
        }
    }
}
