package hcmute.edu.vn.foody_24.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.foody_24.models.PhotoSlide;

import hcmute.edu.vn.foody_24.R;

public class PhotoViewPager2Adapter extends RecyclerView.Adapter<PhotoViewPager2Adapter.PhotoViewHolder>{

    List<PhotoSlide> photoSlideList;


    public PhotoViewPager2Adapter(List<PhotoSlide> photoSlideList) {
        this.photoSlideList = photoSlideList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {

        PhotoSlide photoSlide = photoSlideList.get(position);
        if (photoSlide == null){
            return;
        }

        holder.img_photo.setImageResource(photoSlide.getResourceId());

    }

    @Override
    public int getItemCount() {
        if (photoSlideList != null){
            return  photoSlideList.size();
        }
        return 0;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {

        private final ImageView img_photo;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);

            img_photo = itemView.findViewById(R.id.img_photo);

        }
    }
}
