package Guns;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author admin
 */
public class SQLOrderGun {
    
    public final static String DRIVER = "com.mysql.cj.jdbc.Driver";
    public final static String SERVERNAME= "10.2.0.116";
    public final static String PORT = "80";
    public final static String SCHEMA = "prog_web";
    public final static String PARAMETER = "?serverTimezone=UTC";
    public final static String USERNAME = "root";
    public final static String PASSWORD = "%18adm_mysql";
    
    private static Connection connectionBd(){
        Connection conn = null; 
        //Server url
        String url = "jdbc:mysql://" + SERVERNAME + ":" + PORT + "/" + SCHEMA + PARAMETER;
        
        //propreties for server
        Properties properties = new Properties();
        properties.setProperty("user", USERNAME);
        properties.setProperty("password", PASSWORD);
        properties.setProperty("useSSL", "false");
        properties.setProperty("verifyServerCertificate", "true");
        properties.setProperty("requireSSL", "false");

        //Connect
        try{
            Class.forName(DRIVER).newInstance();
            conn = DriverManager.getConnection(url, properties);
        }
        catch(SQLException |ClassNotFoundException | IllegalAccessException | InstantiationException e){
            System.out.println(e);
            System.exit(-1);
        }
        
        return conn;
    }
    
    public static JSONArray importGunsFromId(int idType)
    {
        JSONArray json = new JSONArray();
        Connection conn = connectionBd();
        
        //Select
        String Requete = "SELECT * FROM guns inner join types on guns.typeId = types.idtype";
        
        PreparedStatement pst=null;
        ResultSet rs = null;
        try{
            if(idType != 0)
            {
                Requete += " where types.idtype = ?";
                pst = conn.prepareStatement(Requete, 1005, 1008);
                pst.setInt(1, idType);
            }
            else 
                pst = conn.prepareStatement(Requete, 1005, 1008);            
            
            rs = pst.executeQuery();
            pst.clearParameters();
            
            //Create Json
           while(rs.next())
           {
                JSONObject jgun = new JSONObject();
                
                jgun.put("gun_idguns",rs.getString("guns.idguns"));
                jgun.put("gun_description",rs.getString("guns.description"));
                jgun.put("gun_imageUrl",rs.getString("guns.imageUrl"));
                jgun.put("types_description", rs.getString("types.description"));
                jgun.put("types_idtype", rs.getString("types.idtype"));
                jgun.put("gun_calibre", rs.getString("guns.calibre"));
                jgun.put("gun_action", rs.getString("guns.action"));
                jgun.put("gun_poids", rs.getString("guns.poids"));

               json.put(jgun);
           }
           
          // Close ResultSet and PreparedStatement
         rs.close();
         pst.close();
       
        }
        catch(JSONException | SQLException e){
            System.out.println(e);
            System.exit(-1);
        }               
        finally{
           try{
              conn.close();
           }catch(SQLException e){
               System.out.println(e);
               System.exit(-1);
           }
        }
            return json;
    }
    
    public static JSONArray importGunsFromName(String name)
    {
        
        JSONArray json = new JSONArray();
        Connection conn = connectionBd();
        
        //Select
        String Requete = "SELECT * FROM guns inner join types on guns.typeId = types.idtype where guns.description like ? ;";
        
        PreparedStatement pst=null;
        ResultSet rs = null;
        try{
           
            
            pst = conn.prepareStatement(Requete, 1005, 1008);            
            pst.setString(1, "%"+name+"%");
            rs = pst.executeQuery();
            pst.clearParameters();
            
            //Create Json
           while(rs.next())
           {
                JSONObject jgun = new JSONObject();
                
                jgun.put("gun_idguns",rs.getString("guns.idguns"));
                jgun.put("gun_description",rs.getString("guns.description"));
                jgun.put("gun_imageUrl",rs.getString("guns.imageUrl"));
                jgun.put("types_description", rs.getString("types.description"));
                jgun.put("types_idtype", rs.getString("types.idtype"));
                jgun.put("gun_calibre", rs.getString("guns.calibre"));
                jgun.put("gun_action", rs.getString("guns.action"));
                jgun.put("gun_poids", rs.getString("guns.poids"));

                json.put(jgun);

           }
           
          // Close ResultSet and PreparedStatement
         rs.close();
         pst.close();
       
        }
        catch(JSONException | SQLException e){
            System.out.println(e);
            System.exit(-1);
        }               
        finally{
           try{
              conn.close();
           }catch(SQLException e){
               System.out.println(e);
               System.exit(-1);
           }
        }
            return json;
    }
    public static boolean DeleteGun(int idGun)
    {
        JSONArray json = new JSONArray();
        Connection conn = connectionBd();
        
        //Delete 
        String requete = "DELETE FROM guns where idguns = ? ;";
        PreparedStatement pst=null;
        try{
           
            
            pst = conn.prepareStatement(requete, 1005, 1008);            
            pst.setInt(1, idGun);
            pst.executeUpdate();
            pst.clearParameters();
        }catch(SQLException e)
        {
           return false;
        }finally
        {
           try{
              conn.close();
           }catch(SQLException e){
               return false;
 
           }
        }
        return true; 
    }
    
    public static boolean AddGun(Gun gun)
    {
        JSONArray json = new JSONArray();
        Connection conn = connectionBd();
        
        //ADD
        String requete = "INSERT INTO `guns` (`description`, `typeId`, `imageUrl`, `calibre`, `action`, `poids`) "
                + "VALUES (?, ?, ?, ?, ?,?)";
        PreparedStatement pst=null;
        try{
            
            pst = conn.prepareStatement(requete, 1005, 1008);            
            pst.setString(1, gun.getDescription());
            pst.setInt(2, gun.getTypeId());
            pst.setString(3, gun.getImageUrl());
            pst.setString(4, gun.getCalibre());
            pst.setString(5, gun.getAction());
            pst.setFloat(6, gun.getPoids());
            pst.executeUpdate();
        }catch(SQLException e)
        {
           return false;
        }finally
        {
           try{
              conn.close();
           }catch(SQLException e){
               return false;
 
           }
        }
        return true;
    }
    
        public static boolean UpdateGun(Gun gun)
    {
        JSONArray json = new JSONArray();
        Connection conn = connectionBd();
        
        //ADD
        String requete = "UPDATE `guns` SET `description`= ?, `typeId`=?, `imageUrl`=?, `calibre`=?, `action`=?, `poids`=? "
                +"WHERE idguns = ?";
        PreparedStatement pst=null;
        try{
            
            pst = conn.prepareStatement(requete, 1005, 1008);            
            pst.setString(1, gun.getDescription());
            pst.setInt(2, gun.getTypeId());
            pst.setString(3, gun.getImageUrl());
            pst.setString(4, gun.getCalibre());
            pst.setString(5, gun.getAction());
            pst.setFloat(6, gun.getPoids());
            pst.setInt(7, gun.getId());
            pst.executeUpdate();
        }catch(SQLException e)
        {
           return false;
        }finally
        {
           try{
              conn.close();
           }catch(SQLException e){
               return false;
 
           }
        }
        return true;
    }
        
    public static JSONArray GetType()
    {
        JSONArray json = new JSONArray();
        Connection conn = connectionBd();
        
        //Select
        String Requete = "SELECT idtype, description FROM types;";
        
        PreparedStatement pst=null;
        ResultSet rs = null;
        try{
            
            pst = conn.prepareStatement(Requete, 1005, 1008);            
            rs = pst.executeQuery();
            pst.clearParameters();
            
            //Create Json
           while(rs.next())
           {
                JSONObject jgun = new JSONObject();
                
                jgun.put("types_idtype",rs.getString("idtype"));
                jgun.put("types_description",rs.getString("description"));
                
                json.put(jgun);
           }
           
          // Close ResultSet and PreparedStatement
         rs.close();
         pst.close();
       
        }
        catch(JSONException | SQLException e){
            System.out.println(e);
            System.exit(-1);
        }               
        finally{
           try{
              conn.close();
           }catch(SQLException e){
               System.out.println(e);
               System.exit(-1);
           }
        }
        return json;
    }
}


