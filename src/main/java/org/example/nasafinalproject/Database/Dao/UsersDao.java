package org.example.nasafinalproject.Database.Dao;

import javafx.collections.FXCollections;
import org.example.nasafinalproject.Database.MySQLConnection;
import org.example.nasafinalproject.Models.Users;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.*;




public class UsersDao extends MySQLConnection implements Dao<Users>  {
    Connection conn = getConnection();

    @Override
    public Optional<Users> findById(int id) {
        return Optional.empty();
    }

    public Users getUserByUsername(String username) {
        String query = "SELECT username, password, APIKey, isAdmin FROM users WHERE username = ?";
        Users user = null;

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String password = rs.getString("password");
                String APIKey = rs.getString("APIKey");
                int isAdmin = rs.getInt("isAdmin");
//                int isAdminId;
//                if (isAdmin){
//                    isAdminId = 1;
//                }else {
//                    isAdminId = 0;
//                }
                user = new Users(username,password,APIKey, isAdmin);
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public boolean isAdmin(String username, String password) {
        String query = "SELECT isAdmin FROM users WHERE username = ? AND password = ?";
        boolean isAdmin = false;

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                isAdmin = rs.getBoolean("isAdmin");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return isAdmin;
    }


    @Override
    public List<Users> findAll() {
        List<Users> usersList = FXCollections.observableArrayList();
        String query = "SELECT * FROM users";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Users user = new Users();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setAPIKey(rs.getString("APIKey"));
                user.setIsAdmin(rs.getInt("isAdmin"));
                usersList.add(user);
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return usersList;
    }

    @Override
    public boolean save(Users users) {

        String query = "insert into users (username, password, APIkey, isAdmin) values (?,?,?,?)";
        try{
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, users.getUsername());
            ps.setString(2, users.getPassword());
            ps.setString(3, users.getAPIKey());
            ps.setInt( 4, users.getIsAdmin());
            ps.execute();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean doesUserExist(String username) {
        boolean exists = false;
        String query = "SELECT COUNT(*) AS user_count FROM users WHERE username = ?";

        try{
            PreparedStatement pst = conn.prepareStatement(query);

            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("user_count");
                exists = (count > 0);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return exists;
    }

    public boolean authenticate(String username, String password) {
        String query = "SELECT COUNT(*) AS user_count FROM users WHERE username = ? AND password = ?";
        boolean exists = false;

        try {
             PreparedStatement pst = conn.prepareStatement(query);

            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("user_count");
                exists = (count > 0);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return exists;
    }

    public String getUserInfoInCSV(String username, String password) {
        String query = "SELECT username, password, APIKey, isAdmin FROM users WHERE username = ? AND password = ?";
        StringBuilder userInfo = new StringBuilder();

        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                userInfo.append(rs.getString("username")).append(",");
                userInfo.append(rs.getString("password")).append(",");
                userInfo.append(rs.getString("APIKey")).append(",");
                userInfo.append(rs.getBoolean("isAdmin"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return userInfo.toString();
    }

    @Override
    public boolean update(Users record) {
        return false;
    }

    @Override
    public boolean delete(int idMarca) {
        String query = "delete from users where username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, idMarca);
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteUser(String usrname) {
        String query = "delete from users where username = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, usrname);
            ps.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
