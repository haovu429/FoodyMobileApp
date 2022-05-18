package hcmute.edu.vn.foody_24.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


import hcmute.edu.vn.foody_24.R;
import hcmute.edu.vn.foody_24.models.Diner;
import hcmute.edu.vn.foody_24.models.DinerRate;
import hcmute.edu.vn.foody_24.models.Post;
import hcmute.edu.vn.foody_24.models.User;
import hcmute.edu.vn.foody_24.service.DinerRateService;
import hcmute.edu.vn.foody_24.service.LikePostService;
import hcmute.edu.vn.foody_24.service.PostService;
import hcmute.edu.vn.foody_24.service.UserService;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private User user;
    private int userId;
    private List<Post> listPost;

    public PostAdapter(int userId, List<Post> listPost) {
        this.userId = userId;
        this.listPost = listPost;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = listPost.get(position);
        if( post == null){
            return;
        }

        UserService userService = new UserService();
        user = userService.getOne(post.getUserId());

        holder.img_user.setImageResource(R.drawable.user_hao);
        holder.tv_username.setText(user.getUsername());

        DinerRateService dinerRateService = new DinerRateService();
        DinerRate rate = dinerRateService.getOneByPostId(post.getId());
        if (rate!=null){
            System.out.println("rate:" + rate);
            holder.tv_average_rate.setText(rate.getAverageRate()+"");
        }

        holder.tv_post_time.setText(String.valueOf(post.getCreateAt()));
        holder.tv_title.setText(post.getTitle());
        holder.tv_content.setText(post.getContent());

        PostService postService = new PostService();
        int post_amount = postService.post_amount_of_user(post.getUserId());
        holder.tv_post_amount.setText(post_amount+"");

        LikePostService likePostService = new LikePostService();
        AtomicInteger like_amount = new AtomicInteger(likePostService.like_amount_of_post(post.getId()));
        holder.tv_post_react.setText(like_amount+" lượt thích," + " 14 lượt xem");

        if(likePostService.isLikedPost(userId, post.getId()) > 0){
            holder.img_like_post.setImageResource(R.drawable.ic_like);
        } else {
            holder.img_like_post.setImageResource(R.drawable.ic_unlike);

        }

        holder.layout_like.setOnClickListener(View -> {
            if(likePostService.isLikedPost(userId, post.getId()) > 0){
                likePostService.unLikePost(userId, post.getId());
                holder.img_like_post.setImageResource(R.drawable.ic_unlike);
            } else {
                likePostService.insert(userId, post.getId());
                holder.img_like_post.setImageResource(R.drawable.ic_like);
            }
            like_amount.set(likePostService.like_amount_of_post(post.getId()));
            holder.tv_post_react.setText(like_amount+" luọt thích," + " 14 lượt xem");
        });

        holder.layout_comment.setOnClickListener(View -> {
            // Mở nest fragment để nhập comment.
        });

    }

    private void changeLikeAmount(int addAmount){

    }

    @Override
    public int getItemCount() {
        if (listPost != null){
            return listPost.size();
        }
        return 0;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_user, img_like_post;
        private TextView tv_username, tv_average_rate, tv_post_time, tv_title, tv_content, tv_post_react, tv_post_amount;
        private LinearLayout layout_like, layout_comment, layout_share;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            img_user = itemView.findViewById(R.id.img_user);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_average_rate = itemView.findViewById(R.id.tv_average_rate);
            tv_post_time = itemView.findViewById(R.id.tv_post_time);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_post_react = itemView.findViewById(R.id.tv_post_react);
            layout_like = itemView.findViewById(R.id.layout_like);
            layout_comment = itemView.findViewById(R.id.layout_comment);
            layout_share = itemView.findViewById(R.id.layout_share);
            img_like_post = itemView.findViewById(R.id.img_like_post);
            tv_post_amount = itemView.findViewById(R.id.tv_post_amount);

        }
    }
}