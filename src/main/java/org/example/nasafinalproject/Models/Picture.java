package org.example.nasafinalproject.Models;

public class Picture {

    String pictureID, explanation, hdurl, url, title, serviceVersion, thumbnail;
    int requestID, copyrightID, mediaTypeID;
    String date;
    RequestAPOD requestAPOD;
    Copyright copyright;

    MediaType mediaType;

    public Picture() {
    }

    public String getPictureID() {
        return pictureID;
    }

    public Picture(String pictureID, String explanation, String hdurl, String url, String title, String serviceVersion, int requestID, int copyrightID, int mediaTypeID, String date, RequestAPOD requestAPOD, Copyright copyright, MediaType mediaType) {
        this.pictureID = pictureID;
        this.explanation = explanation;
        this.hdurl = hdurl;
        this.url = url;
        this.title = title;
        this.serviceVersion = serviceVersion;
        this.requestID = requestID;
        this.copyrightID = copyrightID;
        this.mediaTypeID = mediaTypeID;
        this.date = date;
        this.requestAPOD = requestAPOD;
        this.copyright = copyright;
        this.mediaType = mediaType;
    }

    public void setPictureID(String pictureID) {
        this.pictureID = pictureID;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getHdurl() {
        return hdurl;
    }

    public void setHdurl(String hdurl) {
        this.hdurl = hdurl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getCopyrightID() {
        return copyrightID;
    }

    public void setCopyrightID(int copyrightID) {
        this.copyrightID = copyrightID;
    }

    public int getMediaTypeID() {
        return mediaTypeID;
    }

    public void setMediaTypeID(int mediaTypeID) {
        this.mediaTypeID = mediaTypeID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RequestAPOD getRequestAPOD() {
        return requestAPOD;
    }

    public void setRequestAPOD(RequestAPOD requestAPOD) {
        this.requestAPOD = requestAPOD;
    }

    public Copyright getCopyright() {
        return copyright;
    }

    public void setCopyright(Copyright copyright) {
        this.copyright = copyright;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", copyright=" + copyright +
                '}';
    }
}
