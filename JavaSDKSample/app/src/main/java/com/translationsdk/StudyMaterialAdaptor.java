package com.translationsdk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

class dummyAdapter extends RecyclerView.Adapter<dummyAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> studyList;

    public dummyAdapter(Context context, ArrayList<String> studyList) {
        this.context = context;
        this.studyList = studyList;
    }

    @Override
    public dummyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_study_material, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(dummyAdapter.ViewHolder holder, int position) {
        holder.title.setText(studyList.get(position));
        holder.download.setText(studyList.get(position));

    }

    @Override
    public int getItemCount() {
        return studyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, download;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_title);
            download = itemView.findViewById(R.id.txt_download);
        }
    }
}
