package com.vneuron.sbelasticback.Postgres;

import javax.persistence.*;

@Table(name = "cwj_table") // So the problem was a badly named table, because it had - in them?
// Customer Watchlist Join Table
@Entity
//@IdClass(JoinTableID.class)
public class CWJTable {

    @EmbeddedId private JoinTableID primary_key;
    private float score;
    private String watchlistName;
    private String clientName;

    public CWJTable(JoinTableID primary_key, float score, String watchlistName, String clientName) {
        this.primary_key = primary_key;
        this.score = score;
        this.watchlistName = watchlistName;
        this.clientName = clientName;
    }

    public CWJTable() {

    }

    @Id
    public JoinTableID getPrimary_key() {
        return primary_key;
    }

    public void setPrimary_key(JoinTableID primary_key) {
        this.primary_key = primary_key;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getWatchlistName() {
        return watchlistName;
    }

    public void setWatchlistName(String watchlistName) {
        this.watchlistName = watchlistName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
// Just do this table, and store both fields...
}


