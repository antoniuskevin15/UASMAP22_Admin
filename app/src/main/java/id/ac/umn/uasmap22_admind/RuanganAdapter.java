package id.ac.umn.uasmap22_admind;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RuanganAdapter extends RecyclerView.Adapter<RuanganAdapter.RuanganViewHolder>{
    private final LinkedList<Ruangan> mRuangan;
    private LayoutInflater mInflater;
    private Context mContext;
    private onItemClickListener listener;

    RuanganAdapter(Context context, LinkedList<Ruangan> daftarRuangan){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mRuangan = daftarRuangan;
    }

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener clickListener) {
        listener = clickListener;
    }

    @NonNull
    @Override
    public RuanganAdapter.RuanganViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Bentuk per item
        View mItemView = mInflater.inflate(R.layout.input_item,parent, false);
        return new RuanganViewHolder(mItemView, this, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RuanganAdapter.RuanganViewHolder holder, int position) {
        //Masukin data ke view holder
        Ruangan mCurrent = mRuangan.get(position);
        String namaRuangan = mCurrent.getNama();
        int hargaRuangan = mCurrent.getHarga();
        holder.namaItemView.setText(namaRuangan);
        holder.hargaItemView.setText(String.valueOf(hargaRuangan));
    }

    @Override
    public int getItemCount() {
        return mRuangan.size();
    }

    Map<String, Object> ruang = new HashMap<>();

    class RuanganViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView namaItemView;
        public final TextView hargaItemView;
        final RuanganAdapter mAdapter;

        public RuanganViewHolder(@NonNull View itemView, RuanganAdapter adapter,onItemClickListener listener) {
            super(itemView);
            namaItemView = itemView.findViewById(R.id.customize_ruangan);
            hargaItemView = itemView.findViewById(R.id.customize_harga);
            ruang.put(namaItemView.getText().toString(), hargaItemView);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, "Hubungi CS RentBow untuk mengubah harga ruangan", Toast.LENGTH_SHORT).show();
        }



    }

    public Map<String, Object> getRuang(){
        return ruang;
    }


}
