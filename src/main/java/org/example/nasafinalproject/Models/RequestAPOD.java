package org.example.nasafinalproject.Models;

import java.util.Date;

public class RequestAPOD {
    int idRequest;
    String username;
    Date pictureDate;
    boolean thumbs;

    Users user;

    public RequestAPOD() {
    }

    public RequestAPOD(int idRequest, String idUser, Date pictureDate, boolean thumbs) {
        this.idRequest = idRequest;
        this.username = idUser;
        this.pictureDate = pictureDate;
        this.thumbs = thumbs;
    }

    public RequestAPOD(String username, Date pictureDate, boolean thumbs, Users user) {
        this.username = username;
        this.pictureDate = pictureDate;
        this.thumbs = thumbs;
        this.user = user;
    }

    public int getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(int idRequest) {
        this.idRequest = idRequest;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getPictureDate() {
        return pictureDate;
    }

    public void setPictureDate(Date pictureDate) {
        this.pictureDate = pictureDate;
    }

    public boolean getThumbs() {
        return thumbs;
    }

    public void setThumbs(boolean thumbs) {
        this.thumbs = thumbs;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
