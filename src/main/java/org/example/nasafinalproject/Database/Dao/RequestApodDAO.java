package org.example.nasafinalproject.Database.Dao;

import org.example.nasafinalproject.Database.MySQLConnection;
import org.example.nasafinalproject.Models.*;
import javafx.collections.FXCollections;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class RequestApodDAO extends MySQLConnection implements Dao<RequestAPOD> {
    Connection conn = getConnection();
    //    idRequest   int auto_increment,
    //    pictureDate varchar(10) null,
    //    thumbs      tinyint(1)  null,
    //    username    varchar(50) null,
    @Override
    public Optional<RequestAPOD> findById(int id) {
        Optional<RequestAPOD> optRequest = Optional.empty();
        String query = "select * from request_apod where idRequest = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if ( rs.next() )
            {
                RequestAPOD req = new RequestAPOD();
                req.setIdRequest(rs.getInt("idRequest"));
                req.setPictureDate(rs.getDate("pictureDate"));
                req.setThumbs(rs.getInt("thumbs")==1?true:false);
                req.setUsername(rs.getString("username"));
                req.setUser(findUser(req.getUsername()));
                optRequest = Optional.of(req);
            }
        } catch (SQLException e) {

            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return optRequest;
    }

    @Override
    public List<RequestAPOD> findAll() {
        List<RequestAPOD> mediaList = FXCollections.observableArrayList();
        String query = "select * from request_apod";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next())
            {
                RequestAPOD req = new RequestAPOD();
                req.setIdRequest(rs.getInt("idRequest"));
                req.setPictureDate(rs.getDate("pictureDate"));
                req.setThumbs(rs.getInt("thumbs")==1?true:false);
                req.setUsername(rs.getString("username"));
                req.setUser(findUser(req.getUser().getUsername()));
                mediaList.add(req);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return mediaList;
    }

    public Users findUser(String username){
        Users optMedia = null;
        String query = "select * from users where username = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if ( rs.next() )
            {
                Users users = new Users();
                users.setUsername(rs.getString("username"));
                users.setPassword(rs.getString("password"));
                users.setAPIKey(rs.getString("APIKey"));
                users.setIsAdmin(rs.getInt("idAdmin"));
                optMedia = users;
            }
        } catch (SQLException e) {

            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return optMedia;
    }
    @Override
    public boolean save(RequestAPOD record) {
        String query = "insert into request_apod (pictureDate, thumbs, username)" +
                " values (?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setDate(1, (Date)record.getPictureDate());
            ps.setInt(2, record.getThumbs()?1:0);
            ps.setString(3, record.getUsername());
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(RequestAPOD record) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        String query = "delete from request_apod where idRequest = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
