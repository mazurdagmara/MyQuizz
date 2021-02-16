package com.example.myquizz;

//informqacje na temat pytań ich treści, odpowiedzi, prawidłowej odpoweidzi. poziomu trudności oraz kategorii
//Czyli te same informacje co w bazie danych

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable { //Parcelable umożliwia wysyłanie obiektów pomiędzy klasami
    public static final String POZIOM_LATWY = "Łatwy";
    public static final String POZIOM_SREDNI = "Średni";
    public static final String POZIOM_TRUDNY = "Trudny";

    private int id;
    private String pytanie;
    private String OdpA;
    private String OdpB;
    private String OdpC;
    private String OdpD;
    private int PoprOdp;
    private String trudnosc;
    private int IDkategorii;

    public Question(){}
    public Question(String pytanie, String odpA, String odpB, String odpC, String odpD,
                    int PoprOdp, String trudnosc, int IDkategorii) {
        this.pytanie = pytanie;
        this.OdpA = odpA;
        this.OdpB = odpB;
        this.OdpC = odpC;
        this.OdpD = odpD;
        this.PoprOdp = PoprOdp;
        this.trudnosc = trudnosc;
        this.IDkategorii = IDkategorii;
    }

    protected Question(Parcel in) {
        id = in.readInt();
        pytanie = in.readString();
        OdpA = in.readString();
        OdpB = in.readString();
        OdpC = in.readString();
        OdpD = in.readString();
        PoprOdp = in.readInt();
        trudnosc = in.readString();
        IDkategorii = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(pytanie);
        dest.writeString(OdpA);
        dest.writeString(OdpB);
        dest.writeString(OdpC);
        dest.writeString(OdpD);
        dest.writeInt(PoprOdp);
        dest.writeString(trudnosc);
        dest.writeInt(IDkategorii);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return pytanie;
    }

    public void setQuestion(String question) {
        this.pytanie = question;
    }

    public String getOdpA() {
        return OdpA;
    }

    public void setOdpA(String odpA) {
        this.OdpA = odpA;
    }

    public String getOdpB() {
        return OdpB;
    }

    public void setOdpB(String odpB) {
        this.OdpB = odpB;
    }

    public String getOdpC() {
        return OdpC;
    }

    public void setOdpC(String odpC) {
        this.OdpC = odpC;
    }

    public String getOdpD() {
        return OdpD;
    }

    public void setOdpD(String odpD) {
        this.OdpD = odpD;
    }

    public int getPoprOdp() {
        return PoprOdp;
    }

    public void setPoprOdp(int poprOdp) {
        this.PoprOdp = poprOdp;
    }

    public String getTrudnosc() {
        return trudnosc;
    }

    public void setTrudnosc(String trudnosc) {
        this.trudnosc = trudnosc;
    }

    public int getIDkategorii() {
        return IDkategorii;
    }

    public void setIDkategorii(int IDkategorii) {
        this.IDkategorii = IDkategorii;
    }

    public static String[] getAllDifficultyLevels(){ //tablica string
        return new String[]{POZIOM_LATWY, POZIOM_SREDNI, POZIOM_TRUDNY};
    }
}
