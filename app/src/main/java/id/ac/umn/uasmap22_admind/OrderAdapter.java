package id.ac.umn.uasmap22_admind;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.LinkedList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    private final LinkedList<Order> mOrder;
    private LayoutInflater mInflater;
    private Context mContext;
    private onItemClickListener listener;

    OrderAdapter(Context context, LinkedList<Order> daftarOrder){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mOrder = daftarOrder;
    }

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener clickListener) {
        listener = clickListener;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Bentuk per item
        View mItemView = mInflater.inflate(R.layout.order_item,parent, false);
        return new OrderViewHolder(mItemView, this, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        //Masukin data ke view holder
        Order mCurrent = mOrder.get(position);
        String ruangOrder = mCurrent.getRuang();
        String hargaOrder = mCurrent.getHarga();
        String dateOrder = mCurrent.getDate();
        String timeOrder = mCurrent.getTime();
        String statusOrder = mCurrent.getStatus();
        DocumentSnapshot userOrder = mCurrent.getUser();
        holder.ruanganItemView.setText(ruangOrder);
        holder.dateItemView.setText(dateOrder);
        holder.timeItemView.setText(timeOrder);
        holder.hargaItemView.setText("Rp. "+hargaOrder);
        holder.userItemView.setText(userOrder.getString("nama"));
        holder.statusItemView.setText(statusOrder);
        Log.d("ASD", statusOrder);
        if(statusOrder.equalsIgnoreCase("pending")){
            holder.statusItemView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext,R.color.primary)));
        }else if(statusOrder.equalsIgnoreCase("approved")){
            holder.statusItemView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext,R.color.success)));
        }else if(statusOrder.equalsIgnoreCase("declined")){
            holder.statusItemView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mContext,R.color.danger)));
        }

    }

    @Override
    public int getItemCount() {
        return mOrder.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView ruanganItemView;
        public final TextView dateItemView;
        public final TextView timeItemView;
        public final TextView hargaItemView;
        public final TextView userItemView;
        public final TextView statusItemView;
        final OrderAdapter mAdapter;

        public OrderViewHolder(@NonNull View itemView, OrderAdapter adapter,onItemClickListener listener) {
            super(itemView);
            ruanganItemView = itemView.findViewById(R.id.order_ruangan);
            hargaItemView = itemView.findViewById(R.id.order_harga);
            dateItemView = itemView.findViewById(R.id.order_date);
            timeItemView = itemView.findViewById(R.id.order_time);
            userItemView = itemView.findViewById(R.id.order_user);
            statusItemView = itemView.findViewById(R.id.order_status);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            Toast.makeText(mContext, "PENCET!!", Toast.LENGTH_SHORT).show();
            Order order = mOrder.get(getAdapterPosition());
            Log.d("ORDER", order.getRuang());
            String orderID = order.getId();
            Intent approval = new Intent(mContext, ApproveActivity.class);
            approval.putExtra("ORDER-ID", orderID);
//            approval.putExtra("ORDER", order);
            mContext.startActivity(approval);
        }

    }


}
