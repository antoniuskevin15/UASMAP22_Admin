package id.ac.umn.uasmap22_admind;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.LinkedList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    private final LinkedList<Order> mOrder;
    private LayoutInflater mInflater;
    private Context mContext;
    private onItemClickListener listener;

    HistoryAdapter(Context context, LinkedList<Order> daftarOrder){
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
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Bentuk per item
        View mItemView = mInflater.inflate(R.layout.history_item,parent, false);
        return new HistoryViewHolder(mItemView, this, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        //Masukin data ke view holder
        Order mCurrent = mOrder.get(position);
        String ruangOrder = mCurrent.getRuang();
        String hargaOrder = mCurrent.getHarga();
        String dateOrder = mCurrent.getDate();
        String timeOrder = mCurrent.getTime();
        DocumentSnapshot userOrder = mCurrent.getUser();
        holder.ruanganItemView.setText(ruangOrder);
        holder.dateItemView.setText(dateOrder);
        holder.timeItemView.setText(timeOrder);
        holder.hargaItemView.setText("Rp. "+hargaOrder);
        holder.userItemView.setText(userOrder.getString("nama"));
    }

    @Override
    public int getItemCount() {
        return mOrder.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView ruanganItemView;
        public final TextView dateItemView;
        public final TextView timeItemView;
        public final TextView hargaItemView;
        public final TextView userItemView;
        final HistoryAdapter mAdapter;

        public HistoryViewHolder(@NonNull View itemView, HistoryAdapter adapter,onItemClickListener listener) {
            super(itemView);
            ruanganItemView = itemView.findViewById(R.id.order_ruangan);
            hargaItemView = itemView.findViewById(R.id.order_harga);
            dateItemView = itemView.findViewById(R.id.order_date);
            timeItemView = itemView.findViewById(R.id.order_time);
            userItemView = itemView.findViewById(R.id.order_user);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, "PENCET!!", Toast.LENGTH_SHORT).show();
        }

    }
}
