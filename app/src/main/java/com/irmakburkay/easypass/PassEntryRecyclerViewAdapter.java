package com.irmakburkay.easypass;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PassEntryRecyclerViewAdapter extends
        RecyclerView.Adapter<PassEntryRecyclerViewAdapter.PasswordViewHolder> {

    private final List<Password> passwords;
    private final IEventListener eventListener;

    public PassEntryRecyclerViewAdapter(List<Password> passwords, IEventListener eventListener) {
        this.passwords = passwords;
        this.eventListener = eventListener;
    }

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new PasswordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordViewHolder holder, int position) {
        Password password = passwords.get(position);
        holder.icon.setImageResource(password.getIcon());
        holder.name.setText(password.getName());
        holder.pass.setText(password.getPass());
    }

    @Override
    public int getItemCount() {
        return passwords.size();
    }


    class PasswordViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView name, pass;

        PasswordViewHolder(View view) {
            super(view);
            icon = view.findViewById(R.id.item_icon);
            name = view.findViewById(R.id.item_name);
            pass = view.findViewById(R.id.item_pass);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Log.i("MyLOG", position + " onClick çalıştı");
                        eventListener.onItemClick(position);
                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Log.i("MyLOG", position + " onLongClick çalıştı");
                        eventListener.onItemLongClick(position);
                    }
                    return true;
                }
            });


            pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventListener.onClick(view);
                }
            });

        }
    }

}
