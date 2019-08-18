package com.synarc.app.datatopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

   Context mContext;
    List<String> mNameDB;
    private OnItemClickListener mListener;

    public ResultAdapter(Context context, List<String> NameDB) {
        mContext = context;
        mNameDB = NameDB;
    }
    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.camera_simple_spinner_item, viewGroup, false);
        return new ResultAdapter.ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder resultViewHolder, int i) {

        resultViewHolder.ScanOption.setText(mNameDB.get(i));

    }

    @Override
    public int getItemCount() {
        return mNameDB.size();
    }

    public class  ResultViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        Button ScanOption;
        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);

            ScanOption = itemView.findViewById(R.id.scnaOptions);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (mListener != null){
                int position = getAdapterPosition();

                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick (int position);

//        void onJourneyInfoClick(int position);
//
//        void onDeleteClick(int position);
//
//        void onAmendClick(int position);
//
//        void onAcknowledgeClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
