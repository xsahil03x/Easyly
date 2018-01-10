package com.magarex.easyly.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.library.bubbleview.BubbleTextView;
import com.magarex.easyly.Models.ChatModel;
import com.magarex.easyly.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 12/30/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private Context context;
    private List<ChatModel> chat;

    public ChatAdapter(Context context, List<ChatModel> chat) {
        this.context = context;
        this.chat = chat;
    }

    @Override
    public int getItemViewType(int position) {
        if (chat.get(position).isSend) {
            return 1;
        } else
            return 0;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        if (viewType == 1) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.list_chat_message_send, parent, false);
            return new ChatViewHolder(view);
        } else {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.list_chat_message_recieve, parent, false);
            return new ChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.message.setText(chat.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return chat.size();
    }
}

class ChatViewHolder extends RecyclerView.ViewHolder {

    BubbleTextView message;

    ChatViewHolder(View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.txtMessage);
    }
}
