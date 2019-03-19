package com.example.chitchat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chitchat.Chat;
import com.example.chitchat.MessageActivity;
import com.example.chitchat.R;
import com.example.chitchat.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int Msg_Text_Left=0;
    public static final int Msg_Text_Right=1;
    private List<Chat> mchat;
    private Context context;
    private String imageURL;
    FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Chat> mchat,String imageURL) {
        this.context = context;
        this.mchat = mchat;
        this.imageURL = imageURL;
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mchat.get(position).getSender().equals(firebaseUser.getUid())){
            return Msg_Text_Right;
        }else {
            return Msg_Text_Left;
        }
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i == Msg_Text_Right){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, viewGroup,false);
        return new MessageAdapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i) {

        Chat chat = mchat.get(i);

        viewHolder.textView.setText(chat.getMessage());
        if(imageURL.equals("default")){
            viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(context).load(imageURL).into(viewHolder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.show_message);
            imageView = itemView.findViewById(R.id.profile_image);
        }
    }

}

