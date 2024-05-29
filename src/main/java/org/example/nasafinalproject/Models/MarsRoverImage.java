package org.example.nasafinalproject.Models;

public class MarsRoverImage {
    private String imageUrl;
    private String earthDate;
    private String roverName;

    public MarsRoverImage() {
    }

    public MarsRoverImage(String imageUrl, String earthDate, String roverName) {
        this.imageUrl = imageUrl;
        this.earthDate = earthDate;
        this.roverName = roverName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEarthDate() {
        return earthDate;
    }

    public void setEarthDate(String earthDate) {
        this.earthDate = earthDate;
    }

    public String getRoverName() {
        return roverName;
    }

    public void setRoverName(String roverName) {
        this.roverName = roverName;
    }
}
