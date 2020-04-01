package com.LetsCrack.jiapplicationportal;

public class User {
    public String email,roomNo,UcollegeId,Uyear,phoneNO,name,Uaddress,Uph1,Uph2,password;
    public String etRoomNum,etPhoneNum,etYear;
    public  String d1,d2,rN;
    public String fname,fphone;

    public User() {
    }

    public User(String email, String roomNo,String UcollegeId,String Uyear,String phoneNO, String name,String Uaddress,String Uph1,String Uph2, String password) {
        this.email = email;
        this.roomNo = roomNo;
        this.UcollegeId = UcollegeId;
        this.Uyear = Uyear;
        this.Uaddress = Uaddress;
        this.Uph1 = Uph1;
        this.Uph2 = Uph2;
        this.phoneNO = phoneNO;
        this.name = name;
        this.password = password;
    }

    public String getD1() {
        return d1;
    }

    public void setD1(String d1) {
        this.d1 = d1;
    }

    public String getD2() {
        return d2;
    }

    public void setD2(String d2) {
        this.d2 = d2;
    }

    public String getrN() {
        return rN;
    }

    public void setrN(String rN) {
        this.rN = rN;
    }

    public User(String etRoomNum, String etPhoneNum, String etYear) {
        this.etRoomNum = etRoomNum;
        this.etPhoneNum = etPhoneNum;
        this.etYear = etYear;
    }
}
