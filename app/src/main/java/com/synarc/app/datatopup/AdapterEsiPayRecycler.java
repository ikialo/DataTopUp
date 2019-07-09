package com.synarc.app.datatopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdapterEsiPayRecycler extends   RecyclerView.Adapter <AdapterEsiPayRecycler.EsiPayViewHolder> {


    Context mContext;
    List<EsiPayMeterDetail> mNameDB;
    private OnItemClickListener mListener;

    public AdapterEsiPayRecycler(Context context, List<EsiPayMeterDetail> NameDB) {
        mContext = context;
        mNameDB = NameDB;
    }


    @NonNull
    @Override
    public EsiPayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_esipay_rec, viewGroup, false);
        return new EsiPayViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull EsiPayViewHolder esiPayViewHolder, int i) {

        esiPayViewHolder.meterNumber.setText(mNameDB.get(i).getMeterNumber());
        esiPayViewHolder.name.setText(mNameDB.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return mNameDB.size();
    }

    public class  EsiPayViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name, meterNumber;
        public EsiPayViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.Metername);
            meterNumber = itemView.findViewById(R.id.meterNumber);

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
