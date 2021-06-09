package com.vneuron.sbelasticback.Postgres;

import javax.persistence.*;

@Table(name = "cwj_table") // So the problem was a badly named table, because it had - in them?
// Customer Watchlist Join Table
@Entity
//@IdClass(JoinTableID.class)
public class CWJTable {

    @EmbeddedId private JoinTableID primary_key;
    private float score;

    public CWJTable(JoinTableID primary_key, float score) {
        this.primary_key = primary_key;
        this.score = score;
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
// Just do this table, and store both fields...
}


