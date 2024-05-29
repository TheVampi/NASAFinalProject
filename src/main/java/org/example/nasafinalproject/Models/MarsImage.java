package org.example.nasafinalproject.Models;

public class MarsImage {
    private String imageURL;
    private String earthDate;
    private String roverName;

    public MarsImage() {
    }

    public MarsImage(String imageURL, String earthDate, String roverName) {
        this.imageURL = imageURL;
        this.earthDate = earthDate;
        this.roverName = roverName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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
