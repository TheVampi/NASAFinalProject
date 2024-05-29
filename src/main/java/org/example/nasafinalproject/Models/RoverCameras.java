package org.example.nasafinalproject.Models;

public class RoverCameras {
    private String idCamera;
    private int idRover;
    private String description;


    public RoverCameras() {
    }

    public RoverCameras(String idCamera, int idRover, String description) {
        this.idCamera = idCamera;
        this.idRover = idRover;
        this.description = description;
    }

    public String getIdCamera() {
        return idCamera;
    }

    public void setIdCamera(String idCamera) {
        this.idCamera = idCamera;
    }

    public int getIdRover() {
        return idRover;
    }

    public void setIdRover(int idRover) {
        this.idRover = idRover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
