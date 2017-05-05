package com.javaeedev.util;

import com.javaeedev.annotation.Form;

/**
 * TBean is a "Test Bean" used to test HttpUtil.createFormBean(). Don't use it 
 * for development.
 * 
 * @author Xuefeng
 */
public class TBean {

    private String username;
    private String password;
    private String description;
    private boolean admin;
    private int age;
    private int bonus;
    private long createdDate;

    public String getUsername() {
        return username;
    }

    @Form
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    @Form(htmlEncoding=true)
    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAdmin() {
        return admin;
    }

    @Form
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public int getAge() {
        return age;
    }

    @Form(defaultValue="18")
    public void setAge(int age) {
        this.age = age;
    }

    public int getBonus() {
        return bonus;
    }

    @Form
    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    @Form
    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

}
