package org.example.nasafinalproject.Database.Dao;

import org.example.nasafinalproject.Database.MySQLConnection;
import org.example.nasafinalproject.Models.*;
import javafx.collections.FXCollections;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class PictureDAO extends MySQLConnection implements Dao<Picture>{

    Connection conn = getConnection();


    public Optional<Picture> findById(String id) {
        Optional<Picture> optCopyright = Optional.empty();
        String query = "select * from picture where pictureID = '"+id+"'";
        try {
            PreparedStatement statement = conn.prepareStatement(query);

            ResultSet rs = statement.executeQuery();
            if ( rs.next() )
            {
                Picture pic = new Picture();
                pic.setPictureID(rs.getString("pictureID"));
                pic.setTitle(rs.getString("title"));
                pic.setUrl(rs.getString("url"));
                pic.setHdurl(rs.getString("hdurl"));
                pic.setExplanation(rs.getString("explanation"));
                pic.setDate(rs.getString("date"));
                pic.setCopyrightID(rs.getInt("copyright"));
                pic.setCopyright(findCopyright(pic.getCopyrightID()));
                pic.setMediaTypeID(rs.getInt("mediaType"));
                pic.setMediaType(findMediaType(pic.getMediaTypeID()));
                //pic.setRequestID(rs.getInt("idRequest"));
                //pic.setRequestAPOD(findRequestAPOD(pic.getRequestID()));
                pic.setServiceVersion(rs.getString("serviceVersion"));
                optCopyright = Optional.of(pic);
            }
        } catch (SQLException e) {

            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return optCopyright;
    }

    @Override
    public Optional<Picture> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Picture> findAll() {

        List<Picture> picList = FXCollections.observableArrayList();
        String query = "select * from picture";
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next())
            {
                Picture pic = new Picture();
                pic.setPictureID(rs.getString("pictureID"));
                pic.setTitle(rs.getString("title"));
                pic.setUrl(rs.getString("url"));
                pic.setHdurl(rs.getString("hdurl"));
                pic.setExplanation(rs.getString("explanation"));
                pic.setDate(rs.getString("date"));
                pic.setCopyrightID(rs.getInt("copyright"));
                pic.setCopyright(findCopyright(pic.getCopyrightID()));
                pic.setMediaTypeID(rs.getInt("mediaType"));
                pic.setMediaType(findMediaType(pic.getMediaTypeID()));
                //pic.setRequestID(rs.getInt("idRequest"));
                //pic.setRequestAPOD(findRequestAPOD(pic.getRequestID()));
                pic.setServiceVersion(rs.getString("serviceVersion"));
                picList.add(pic);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return picList;
    }

    public MediaType findMediaType(int id){
        MediaType optMedia = null;
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
                optMedia = cop;
            }
        } catch (SQLException e) {

            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return optMedia;
    }

    public Copyright findCopyright(int id){
        Copyright optCopyright = null;
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
                optCopyright = cop;
            }
        } catch (SQLException e) {

            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return optCopyright;
    }

    public RequestAPOD findRequestAPOD(int id){
        RequestApodDAO apodDAO = new RequestApodDAO();

        RequestAPOD req =  apodDAO.findById(id).get();

        return req;
    }
    //    pictureID      varchar(10) not null
    //                        primary key,
    //    idRequest      int         null,
    //    copyright      int         null,
    //    date           varchar(10) null,
    //    explanation    text        null,
    //    hdurl          text        null,
    //    mediaType      int         null,
    //    serviceVersion varchar(4)  null,
    //    url            text        null,
    //    title          varchar(20) null,
    @Override
    public boolean save(Picture record) {
        //String query = "insert into picture (pictureID, idRequest, copyright, date, explanation, hdurl, mediaType, serviceVersion, url, title)" +
        //        " values (?,?,?,?,?,?,?,?,?,?)";

        String query = "insert into picture (pictureID, copyright, date, explanation, hdurl, mediaType, serviceVersion, url, title)" +
                " values (?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, record.getPictureID());
            //ps.setInt(2, record.getRequestID());
            ps.setInt(2, record.getCopyrightID());
            ps.setString(3, record.getDate());
            ps.setString(4, record.getExplanation());
            ps.setString(5, record.getHdurl());
            ps.setInt(6, record.getMediaTypeID());
            ps.setString(7, record.getServiceVersion());
            ps.setString(8, record.getUrl());
            ps.setString(9, record.getTitle());
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean saveNoCopyright(Picture record) {
        //String query = "insert into picture (pictureID, idRequest, copyright, date, explanation, hdurl, mediaType, serviceVersion, url, title)" +
        //        " values (?,?,?,?,?,?,?,?,?,?)";

        String query = "insert into picture (pictureID, date, explanation, hdurl, mediaType, serviceVersion, url, title)" +
                " values (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, record.getPictureID());
            //ps.setInt(2, record.getRequestID());
            ps.setString(2, record.getDate());
            ps.setString(3, record.getExplanation());
            ps.setString(4, record.getHdurl());
            ps.setInt(5, record.getMediaTypeID());
            ps.setString(6, record.getServiceVersion());
            ps.setString(7, record.getUrl());
            ps.setString(8, record.getTitle());
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public boolean delete(String id){
        String query = "delete from picture where pictureID = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Picture record) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
