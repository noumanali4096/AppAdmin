package com.example.nouman.appadmin;

public class MosquenTimming {
    private Mosque mosque;

    private PrayerTimmings prayerTimmings;

    public MosquenTimming(){}

    public MosquenTimming(Mosque mosque, PrayerTimmings prayerTimmings) {
        this.mosque = mosque;
        this.prayerTimmings = prayerTimmings;
    }

    public Mosque getMosque() {
        return mosque;
    }

    public void setMosque(Mosque mosque) {
        this.mosque = mosque;
    }

    public PrayerTimmings getPrayerTimmings() {
        return prayerTimmings;
    }

    public void setPrayerTimmings(PrayerTimmings prayerTimmings) {
        this.prayerTimmings = prayerTimmings;
    }
}
