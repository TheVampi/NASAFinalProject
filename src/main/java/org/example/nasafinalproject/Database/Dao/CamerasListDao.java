package org.example.nasafinalproject.Database.Dao;

import org.example.nasafinalproject.Database.MySQLConnection;
import org.example.nasafinalproject.Models.RoverCameras;
import org.example.nasafinalproject.Models.Users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CamerasListDao extends MySQLConnection implements Dao<RoverCameras> {
    Connection conn = getConnection();

    @Override
    public Optional<RoverCameras> findById(int idRover) {
        return Optional.empty();
    }

    public List<String> findCamerasByRover(String roverName) {
        List<String> cameras = new ArrayList<>();
        String query = "SELECT c.idCamera, c.description FROM cameras_list cl JOIN camera c ON cl.idCamera = c.idCamera JOIN rover r ON cl.idRover = r.idRover WHERE r.nameRover = "+roverName;
        try{
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                cameras.add(rs.getString("idCamera")+" "+rs.getString("description"));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return cameras;
    }

    public List<RoverCameras> getRoverCamerasInfo(String roverName) {
        String query = "SELECT r.idRover, c.idCamera, c.description " +
                "FROM rover r " +
                "JOIN cameras_list cl ON r.idRover = cl.idRover " +
                "JOIN camera c ON cl.idCamera = c.idCamera " +
                "WHERE r.nameRover = ?";

        List<RoverCameras> camerasInfoList = new ArrayList<>();

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, roverName);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int idRover = rs.getInt("idRover");
                String idCamera = rs.getString("idCamera");
                String description = rs.getString("description");
                camerasInfoList.add(new RoverCameras(idCamera, idRover, description));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return camerasInfoList;
    }


    @Override
    public List<RoverCameras> findAll() {
        return List.of();
    }

    @Override
    public boolean save(RoverCameras record) {
        return false;
    }

    @Override
    public boolean update(RoverCameras record) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
