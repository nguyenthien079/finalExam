package com.example.finalexam.Domain;

public class UserModel {
    private String uid;
    private String username; // đổi từ displayName thành username
    private String bio;
    private String contact;
    private String profilePic; // đổi từ profileImage thành profilePic
    private double rating;
    private boolean deactivated;

    public UserModel() {}

    public UserModel(String uid, String username, String bio, String contact, String profilePic, double rating, boolean deactivated) {
        this.uid = uid;
        this.username = username;
        this.bio = bio;
        this.contact = contact;
        this.profilePic = profilePic;
        this.rating = rating;
        this.deactivated = deactivated;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getProfilePic() { return profilePic; }
    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public boolean isDeactivated() { return deactivated; }
    public void setDeactivated(boolean deactivated) { this.deactivated = deactivated; }
}
