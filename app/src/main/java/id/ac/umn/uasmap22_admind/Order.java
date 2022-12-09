package id.ac.umn.uasmap22_admind;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;

public class Order implements Serializable {
    private String mHarga;
    private String mRuang;
    private String mDate;
    private String mTime;
    private String mId;
    private String mStatus;
    private DocumentSnapshot mUser;

    Order(String ruang, String harga, String date, String time, String id, String status, DocumentSnapshot user) {
        mRuang = ruang;
        mHarga = harga;
        mDate = date;
        mTime = time;
        mUser = user;
        mId = id;
        mStatus = status;
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

    public String getId() {
        return mId;
    }

    public String getStatus() {
        return mStatus;
    }
}
