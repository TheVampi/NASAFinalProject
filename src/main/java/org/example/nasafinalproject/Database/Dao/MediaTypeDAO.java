package org.example.nasafinalproject.Database.Dao;


import org.example.nasafinalproject.Database.MySQLConnection;
import org.example.nasafinalproject.Models.*;

import javafx.collections.FXCollections;

import java.sql.*;
import java.util.List;
import java.util.Optional;


public class MediaTypeDAO extends MySQLConnection implements Dao<MediaType>{
    Connection conn = getConnection();
    @Override
    public Optional<MediaType> findById(int id) {
        Optional<MediaType> optMedia = Optional.empty();
        String query = "select * from media_type where idMediaType = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if ( rs.next() )
            {
                MediaType cop = new MediaType();
                cop.setTypeID(rs.getInt("idMediaType"));
                cop.setName(rs.getString("name"));
                optMedia = Optional.of(cop);
            }
        } catch (SQLException e) {

            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return optMedia;
    }

    public Optional<MediaType> findByName(String name) {
        Optional<MediaType> optMedia = Optional.empty();
        String query = "select * from media_type where name = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if ( rs.next() )
            {
                MediaType cop = new MediaType();
                cop.setTypeID(rs.getInt("idMediaType"));
                cop.setName(rs.getString("name"));
                optMedia = Optional.of(cop);
            }
        } catch (SQLException e) {

            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return optMedia;
    }

    @Override
    public List<MediaType> findAll() {
        List<MediaType> mediaList = FXCollections.observableArrayList();
        String query = "select * from media_type";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next())
            {
                MediaType copy = new MediaType();
                copy.setTypeID(rs.getInt("idMediaType"));
                copy.setName(rs.getString("name"));
                mediaList.add(copy);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return mediaList;
    }

    public int getMediaId(String name){
        String query= "select idMediaType from media_type where name = ? limit 1";
        int id=0;
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                id = rs.getInt("idMediaType");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    @Override
    public boolean save(MediaType record) {
        String query = "insert into media_type (name)" +
                " values (?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, record.getName());
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(MediaType record) {
        String query = "update media_type set name=? where idMediaType = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, record.getName());
            ps.setInt(2, record.getTypeID());
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        String query = "delete from media_type where idMediaType = ?";
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
