package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav on 7/21/2016.
 */
public class TemporaryUser {
    @SerializedName("RegistrationChannelTypeID")
   private int registrationChannelTypeID;
    @SerializedName("Cell")
    private  String cell;
    @SerializedName("CellVerified")
    private int cellVerified;
    @SerializedName("FirstName")
    private String firstName;
    @SerializedName("LastName")
    private String lastName;
    @SerializedName("UserName")
    private String userName;
    @SerializedName("SocialMediaUserID")
    private String socialMediaUserId;
    @SerializedName("Sex")
    private int sex;
    @SerializedName("EmailID")
    private String emailId;
    @SerializedName("DeviceRegistrationID")
    private String DeviceRegistrationIDDeviceRegistrationID;

    public int getRegistrationChannelTypeID() {
        return registrationChannelTypeID;
    }

    public void setRegistrationChannelTypeID(int registrationChannelTypeID) {
        this.registrationChannelTypeID = registrationChannelTypeID;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSocialMediaUserId() {
        return socialMediaUserId;
    }

    public void setSocialMediaUserId(String socialMediaUserId) {
        this.socialMediaUserId = socialMediaUserId;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDeviceRegistrationIDDeviceRegistrationID() {
        return DeviceRegistrationIDDeviceRegistrationID;
    }

    public void setDeviceRegistrationIDDeviceRegistrationID(String deviceRegistrationIDDeviceRegistrationID) {
        DeviceRegistrationIDDeviceRegistrationID = deviceRegistrationIDDeviceRegistrationID;
    }

    public int getCellVerified() {
        return cellVerified;
    }

    public void setCellVerified(int cellVerified) {
        this.cellVerified = cellVerified;
    }
}
