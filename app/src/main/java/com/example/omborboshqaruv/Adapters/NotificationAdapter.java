package com.example.omborboshqaruv.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.omborboshqaruv.Models.NotificationModel;
import com.example.omborboshqaruv.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private final List<NotificationModel> notificationList;

    public NotificationAdapter(List<NotificationModel> notificationList) {
        this.notificationList = notificationList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, messageText;

        public ViewHolder(View view) {
            super(view);
            titleText = view.findViewById(R.id.titleText);
            messageText = view.findViewById(R.id.messageText);
        }
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationModel model = notificationList.get(position);
        holder.titleText.setText(model.getTitle());
        holder.messageText.setText(model.getMessage());
        if (model.isRead()) {
            holder.itemView.setAlpha(0.7f);
        } else {
            holder.itemView.setAlpha(1f);
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
