package com.nicky.domain;

import com.nicky.cache.ercache.ListKeyParam;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Nicky_chin  --Created on 2017-11-13
 */
public class Users implements Serializable, ListKeyParam {

    private static final long serialVersionUID = 7861640934969334690L;

    private Integer id;

    private Date birthday;

    private String name;

    private String telephone;


    /**
     * default constructor
     */
    public Users() {
    }

    /**
     * full constructor
     */
    public Users(Date birthday, String name, String telephone) {
        this.birthday = birthday;
        this.name = name;
        this.telephone = telephone;
    }


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override public String toString() {
        return "Users{" + "id=" + id + ", birthday=" + birthday + ", name='" + name + '\'' + ", telephone='" + telephone
                + '\'' + '}';
    }

    @Override
    public Object getKey() {
        return id+":"+name;
    }
}
