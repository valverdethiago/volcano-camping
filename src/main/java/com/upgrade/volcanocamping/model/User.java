package com.upgrade.volcanocamping.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by valve on 03/02/2019.
 */
@Embeddable
public class User implements Serializable{

    @Column(name = "user_email")
    private String email;
    @Column(name = "user_full_name")
    private String fullName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
