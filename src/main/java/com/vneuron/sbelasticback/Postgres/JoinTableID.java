package com.vneuron.sbelasticback.Postgres;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

@Embeddable
public class JoinTableID implements Serializable {
    private Long customer_id;
    private String watchlist_id;

    // default constructor

    public JoinTableID(Long customer_id, String watchlist_id) {
        this.customer_id = customer_id;
        this.watchlist_id = watchlist_id;
    }

    public JoinTableID() {

    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public String getWatchlist_id() {
        return watchlist_id;
    }

    public void setWatchlist_id(String watchlist_id) {
        this.watchlist_id = watchlist_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JoinTableID that = (JoinTableID) o;

        if (customer_id != null ? !customer_id.equals(that.customer_id) : that.customer_id != null) return false;
        return watchlist_id != null ? watchlist_id.equals(that.watchlist_id) : that.watchlist_id == null;
    }

    @Override
    public int hashCode() {
        int result = customer_id != null ? customer_id.hashCode() : 0;
        result = 31 * result + (watchlist_id != null ? watchlist_id.hashCode() : 0);
        return result;
    }


// equals() and hashCode()
}