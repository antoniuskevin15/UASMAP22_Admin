package id.ac.umn.uasmap22_admind;

import com.google.firebase.firestore.DocumentSnapshot;

public class Order {
    private DocumentSnapshot mCompany;
    private String mDate;
    private String mTime;
    private DocumentSnapshot mUser;

    Order(DocumentSnapshot company, String date, String time, DocumentSnapshot user){
        mCompany = company;
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

    public DocumentSnapshot getCompany() {
        return mCompany;
    }

    public DocumentSnapshot getUser() {
        return mUser;
    }
}
