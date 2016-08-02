package com.cloudprint;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
/**
 * Created by Ankush on 10/17/15.
 */
@SuppressWarnings("serial") //with this annotation we are going to hide compiler warning
public class User implements Serializable{

    private String _id;
    private String token;
    @Expose private String email;
    @Expose private String firstName;
    @Expose private String lastName;
    @Expose private String password;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}

