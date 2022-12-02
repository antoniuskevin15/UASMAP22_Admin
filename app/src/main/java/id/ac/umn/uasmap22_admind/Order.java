package id.ac.umn.uasmap22_admind;

import com.google.firebase.firestore.DocumentSnapshot;

public class Order {
    private String mHarga;
    private String mRuang;
    private String mDate;
    private String mTime;
    private DocumentSnapshot mUser;

    Order(String ruang, String harga, String date, String time, DocumentSnapshot user){
        mRuang = ruang;
        mHarga = harga;
        mDate = date;
        mTime = time;
        mUser = user;
    }

    public String getDate() {
        return mDate;
    }

    public String getTime() {
        return mTime;
    }

    public String getHarga() {
        return mHarga;
    }

    public String getRuang() {
        return mRuang;
    }

    public DocumentSnapshot getUser() {
        return mUser;
    }
}
