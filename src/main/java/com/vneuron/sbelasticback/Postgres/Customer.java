package com.vneuron.sbelasticback.Postgres;
// There are probably some annotations that should be placed here...

import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    private Long id;
    private String business_name;
    private String first_name;
    private String last_name;
    private String maiden_name;
    private String manager_name;
    private String whole_name;
    private String gazette_ref;


    public String getGazette_ref() {
        return gazette_ref;
    }

    public void setGazette_ref(String gazette_ref) {
        this.gazette_ref = gazette_ref;
    }


    @javax.persistence.Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMaiden_name() {
        return maiden_name;
    }

    public void setMaiden_name(String maiden_name) {
        this.maiden_name = maiden_name;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

    public String getWhole_name() {
        return whole_name;
    }

    public void setWhole_name(String whole_name) {
        this.whole_name = whole_name;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", business_name='" + business_name + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", maiden_name='" + maiden_name + '\'' +
                ", manager_name='" + manager_name + '\'' +
                ", whole_name='" + whole_name + '\'' +
                ", gazette_ref='" + gazette_ref + '\'' +
                '}' + '\n';
    }
}