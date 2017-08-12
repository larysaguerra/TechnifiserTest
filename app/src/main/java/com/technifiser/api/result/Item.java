package com.technifiser.api.result;

import java.io.Serializable;

public class Item implements Serializable {

    private Venue venue;
    private String referralId;

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getReferralId() {
        return referralId;
    }

    public void setReferralId(String referralId) {
        this.referralId = referralId;
    }

}
