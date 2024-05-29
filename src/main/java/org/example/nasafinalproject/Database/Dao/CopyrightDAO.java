package org.example.nasafinalproject.Database.Dao;

import org.example.nasafinalproject.Database.MySQLConnection;

import org.example.nasafinalproject.Models.*;

import javafx.collections.FXCollections;

import java.sql.*;
import java.util.List;
import java.util.Optional;


public class CopyrightDAO extends MySQLConnection implements Dao<Copyright>{

    Connection  conn = getConnection();
    @Override
    public Optional<Copyright> findById(int id) {
        Optional<Copyright> optCopyright = Optional.empty();
        String query = "select * from copyright where idCopyright = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if ( rs.next() )
            {
                Copyright cop = new Copyright();
                cop.setCopyrightID(rs.getInt("idCopyright"));
                cop.setName(rs.getString("name"));
                optCopyright = Optional.of(cop);
            }
        } catch (SQLException e) {

            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return optCopyright;
    }

    public Optional<Copyright> findByName(String name) {
        Optional<Copyright> optCopyright = Optional.empty();
        String query = "select * from copyright where name = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if ( rs.next() )
            {
                Copyright cop = new Copyright();
                cop.setCopyrightID(rs.getInt("idCopyright"));
                cop.setName(rs.getString("name"));
                optCopyright = Optional.of(cop);
            }
        } catch (SQLException e) {

            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return optCopyright;
    }

    @Override
    public List<Copyright> findAll() {
        List<Copyright> cprList = FXCollections.observableArrayList();
        String query = "select * from copyright";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next())
            {
                Copyright copy = new Copyright();
                copy.setCopyrightID(rs.getInt("idCopyright"));
                copy.setName(rs.getString("name"));
                cprList.add(copy);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return cprList;
    }

    @Override
    public boolean save(Copyright record) {
        String query = "insert into copyright (name)" +
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

    public int getCopyrightId(String name){
        String query= "select idCopyright from copyright where name = ? limit 1";
        int id=0;
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                id = rs.getInt("idCopyright");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    @Override
    public boolean update(Copyright record) {
        String query = "update copyright set name=? where idCopyright = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, record.getName());
            ps.setInt(2, record.getCopyrightID());
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        String query = "delete from copyright where idCopyright = ?";
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
