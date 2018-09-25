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
public class ImportationGun {
    
    public final static String DRIVER = "com.mysql.cj.jdbc.Driver";
    public final static String SERVERNAME= "localhost";
    public final static String PORT = "3306";
    public final static String SCHEMA = "prog_web";
    public final static String PARAMETER = "?serverTimezone=UTC";
    public final static String USERNAME = "root";
    public final static String PASSWORD = "";
    
    public static JSONArray importGunsFromId(int idType)
    {
        
        JSONArray json = new JSONArray();
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
        //Select
        
        String Where = "where types.idtype = " + String.valueOf(idType);
        String Requete = "SELECT * FROM guns inner join types on guns.idguns = types.idtype"+ Where + ";";
        PreparedStatement pst=null;
        ResultSet rs = null;
        try{
            pst = conn.prepareStatement(Requete, 1005, 1008);
            pst.clearParameters();
            
            rs = pst.executeQuery();
            //Create Json
           while(rs.next())
           {
               JSONObject jgun = new JSONObject();
               jgun.put("gun_idguns",rs.getString("guns.idguns"));
               jgun.put("gun_description",rs.getString("guns.description"));
               jgun.put("gun.imageUrl",rs.getString("guns.imageUrl"));
               jgun.put("types_description", rs.getString("types.description"));
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