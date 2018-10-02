<%@page import="Guns.Gun"%>
<%@page import="org.json.JSONException"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.util.Properties"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="Guns.SQLOrderGun"%>

<%@page contentType="application/json"%>
<%
    JSONObject json = new JSONObject(request.getParameter("gun"));
    
    //Valeur de gun
    int idGun =0;
    String description = null;
    String imageUrl= null;
    int typeId= 0;
    String calibre= null;
    String action= null;
    float poids= 0;
    
    
    try
    {
        idGun = json.getInt("gun_idguns");
        description = json.getString("gun_description");
        imageUrl = json.getString("gun_imageUrl");
        calibre = json.getString("gun_calibre");
        action = json.getString("gun_action");
        poids = (float)json.getLong("gun_poids");
        typeId = json.getInt("gun_typeId");
    }catch(JSONException e)
    {
        System.out.println(e);
        System.exit(-1);
    }
    Gun gunToUpdate = new Gun(idGun,description, typeId, imageUrl, calibre, action, poids);
    SQLOrderGun.UpdateGun(gunToUpdate);
    
 
%>