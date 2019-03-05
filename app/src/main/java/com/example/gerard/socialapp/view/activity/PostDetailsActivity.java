package com.example.gerard.socialapp.view.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.gerard.socialapp.GlideApp;
import com.example.gerard.socialapp.R;
import com.example.gerard.socialapp.model.Post;
import com.example.gerard.socialapp.model.PostComment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PostDetailsActivity extends AppCompatActivity {

    public ImageView photo;
    public TextView author;
    public TextView content;
    public ImageView image;
    public ImageView like;
    public TextView numLikes;
    public LinearLayout likeLayout;
    public VideoView videoView;

    public Button newComButton;
    public EditText comEditText;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        photo = findViewById(R.id.photo);
        author = findViewById(R.id.author);
        content = findViewById(R.id.content);
        image = findViewById(R.id.image);
        videoView = findViewById(R.id.video);
        like = findViewById(R.id.like);
        numLikes = findViewById(R.id.num_likes);
        likeLayout = findViewById(R.id.like_layout);

        newComButton = findViewById(R.id.new_com_button);
        comEditText = findViewById(R.id.com_edit_text);



        Intent intent = getIntent();
        final String postkey = intent.getStringExtra("POST");


        database = FirebaseDatabase.getInstance().getReference();

        getValue(database.child("posts/data").child(postkey));



        newComButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment(postkey);
            }
        });


        //Rellenar el recycler con los comments

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Post>()
                .setIndexedQuery(setQuery(), mReference.child("posts/data"), Post.class)
                .setLifecycleOwner(this)
                .build();
        RecyclerView commRecycler = findViewById(R.id.comment_recycler);



    }

    public void submitComment(String postkey){
        final String comText = comEditText.getText().toString();

        if (comText.isEmpty()){
            comEditText.setError("Required");
            return;
        }

        newComButton.setEnabled(false);

        String comKey = database.child("posts/data").child(postkey).child("Comments").push().getKey();

        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

        PostComment comment = new PostComment(fuser.getUid(), fuser.getDisplayName(), fuser.getPhotoUrl().toString(), comText);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("posts/data/"+ postkey+ "/comments/"+ comKey, comment.toMap());

        database.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
            }
        });

    }

    public void getValue(final DatabaseReference databaseReference){

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);

                GlideApp.with(getBaseContext()).load(post.authorPhotoUrl).circleCrop().into(photo);
                author.setText(post.author);
                content.setText(post.content);
                if (post.mediaType != null) {
                    if (post.mediaType.equals("image")) {
                        videoView.setVisibility(View.GONE);
                        image.setVisibility(View.VISIBLE);
                        GlideApp.with(getBaseContext()).load(post.mediaUrl).into(image);
                    } else if (post.mediaType.equals("video")) {
                        videoView.setVisibility(View.VISIBLE);
                        image.setVisibility(View.GONE);
                        videoView.setVideoPath(post.mediaUrl);
                        videoView.start();

                    }
                }else{
                    videoView.setVisibility(View.GONE);
                    image.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    Query setQuery(){
        return  database.child("posts/all-posts").limitToFirst(100);
    }

}
