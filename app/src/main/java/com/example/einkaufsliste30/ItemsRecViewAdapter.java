package com.example.einkaufsliste30;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;

import java.util.ArrayList;

public class ItemsRecViewAdapter extends RecyclerView.Adapter<ItemsRecViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Item> items;
    public final String sharedPreferencesKey = "grocery list";


    public ItemsRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, items.get(holder.getAdapterPosition()).getName()+" selected.", Toast.LENGTH_SHORT).show();
            }
        });
        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = items.get(holder.getAdapterPosition());
                item.setCounter(item.getCounter()+1);
                holder.counter.setText(String.valueOf(item.getCounter()));
                saveData();
            }
        });
        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = items.get(holder.getAdapterPosition());
                if (item.getCounter() == 1){
                    Toast.makeText(context, item.getName()+" deleted.", Toast.LENGTH_SHORT).show();
                    items.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }
                else{
                    item.setCounter(item.getCounter()-1);
                    holder.counter.setText(String.valueOf(item.getCounter()));
                }
                saveData();
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = items.get(holder.getAdapterPosition());
                Toast.makeText(context, item.getName()+" deleted.", Toast.LENGTH_SHORT).show();
                items.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                saveData();
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.groceryItem.setText(item.getName());
        holder.counter.setText(String.valueOf(item.getCounter()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView groceryItem, counter;
        private LinearLayout parent;
        private ImageButton plusButton, minusButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groceryItem = itemView.findViewById(R.id.groceryItem);
            counter = itemView.findViewById(R.id.counter);
            plusButton = itemView.findViewById(R.id.plusButton);
            minusButton = itemView.findViewById(R.id.minusButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            parent = itemView.findViewById(R.id.parent);
        }
    }
    private void saveData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(items);
        editor.putString(sharedPreferencesKey, json);
        editor.apply();
    }
}
