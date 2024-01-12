package com.example.droidtools.LoginAndSignUp;

public class SignupHelperForFirebase {

    private String password;
    private String mail,phonenumber,username;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public SignupHelperForFirebase(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SignupHelperForFirebase(String username, String mail, String phonenumber, String password) {

        this.username = username;
        this.mail = mail;
        this.phonenumber = phonenumber;
        this.password = password;
    }
}
