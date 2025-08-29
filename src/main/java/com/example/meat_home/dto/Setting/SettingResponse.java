package com.example.meat_home.dto.Setting;

public class SettingResponse {
    private String name;
    private String logoUrl;
    private String aboutImageUrl;
    private String aboutDescription;
    private String termsAndConditions;
    private String facebookUrl;
    private String whatsappNumber;
    private String phoneNumber;

    public String getSecondPhoneNumber() {
        return secondPhoneNumber;
    }

    public void setSecondPhoneNumber(String secondPhoneNumber) {
        this.secondPhoneNumber = secondPhoneNumber;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAboutImageUrl() {
        return aboutImageUrl;
    }

    public void setAboutImageUrl(String aboutImageUrl) {
        this.aboutImageUrl = aboutImageUrl;
    }

    public String getAboutDescription() {
        return aboutDescription;
    }

    public void setAboutDescription(String aboutDescription) {
        this.aboutDescription = aboutDescription;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String secondPhoneNumber;

    // getters & setters
}
