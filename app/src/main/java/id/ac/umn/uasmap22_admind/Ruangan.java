package id.ac.umn.uasmap22_admind;

public class Ruangan {
    private String mNama;
    private int mHarga;

    Ruangan(String nama, int harga){
        mNama = nama;
        mHarga = harga;
    }

    public int getHarga() {
        return mHarga;
    }

    public String getNama() {
        return mNama;
    }
}
