/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.io.JSONParser;
import com.codename1.ui.events.ActionListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mycompany.myapp.entities.Users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author ouertani
 */
public class UserService {

    ArrayList<Users> listEvents = new ArrayList<>();
    public String pass;
    Users u = new Users();

    public Users getUserbyEmail(String email) {

        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://127.0.0.1:8000/api/users/email/" + email);
        con.setPost(false);
        //con.setHttpMethod("GET");
        con.addResponseListener(new ActionListener<NetworkEvent>() {

            @Override
            public void actionPerformed(NetworkEvent evt) {
                UserService es = new UserService();
                listEvents = es.getUsers(new String(con.getResponseData()));
                System.out.println("listEvents");
                System.out.println(listEvents);
                con.removeResponseListener(this);

            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        if (listEvents.isEmpty()) {
            return null;
        } else {
            System.out.println(0);
            return listEvents.get(0);

        }
    }

    public ArrayList<Users> getUsers(String json) {

        ArrayList<Users> listEvt = new ArrayList<>();
        try {
            JSONParser j = new JSONParser();
            Map<String, Object> evt = j.parseJSON(new CharArrayReader(json.toCharArray()));
            System.out.println(json);

            System.out.println("this evt");
            System.out.println(evt);

            List<Map<String, Object>> list = (List<Map<String, Object>>) evt.get("root");
            System.out.println("this list");
            System.out.println(list);

            for (Map<String, Object> obj : list) {

                Users u = new Users();
                float id = Float.parseFloat(obj.get("id").toString());
                //float enabled = Float.parseFloat(obj.get("enabled").toString());

                u.setId((int) id);
                u.setUsername(obj.get("username").toString());
                u.setEmail(obj.get("email").toString());
                //u.setEnabled((int) enabled);
                if (obj.get("roles") != null) {
                    u.setRoles(obj.get("roles").toString());
                }

                u.setPassword(obj.get("password").toString());

                System.out.println("this UserService 73" + u.getUsername());
                listEvt.add(u);

            }

        } catch (IOException ex) {
        }
        System.out.println("i'm here");
        System.out.println(listEvt);
        return listEvt;

    }

    public String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    /*
    public String genpass(String password) {
        ConnectionRequest r = new ConnectionRequest();
        r.setUrl("http://localhost/genpass.php?password=" + password);
        r.setPost(false);
        r.addResponseListener(new ActionListener<NetworkEvent>() {

            @Override
            public void actionPerformed(NetworkEvent evt) {
                try {
                            NetworkEvent event = (NetworkEvent) ev;
                            byte[] data = (byte[]) event.getMetaData();
                            String decodedData = new String(data, "UTF-8");
                            System.out.println(decodedData);
                            if (decodedData.equals("nice")) {
                                LoginController.status="good";

                            } else {
                                LoginController.status="notgood";
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                pass = new String(r.getResponseData());
                //System.err.println(status);

            }

        });

        NetworkManager.getInstance().addToQueueAndWait(r);
        return pass;
    }
     */
    public void updatepasswordUser(Users user) {

        ConnectionRequest r = new ConnectionRequest();
        r.setUrl("http://localhost/editpass.php?id=" + user.getId() + "&password=" + user.getPassword());
        r.setPost(false);
        NetworkManager.getInstance().addToQueueAndWait(r);
    }

    public ArrayList<Users> getAllUsers() {

        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://127.0.0.1:8000/api/users/all");
        con.setPost(false);
        //con.setHttpMethod("GET");
        con.addResponseListener(new ActionListener<NetworkEvent>() {

            @Override
            public void actionPerformed(NetworkEvent evt) {

                UserService es = new UserService();
                listEvents = es.getUsers(new String(con.getResponseData()));
                con.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listEvents;
    }

    public Users getUserInfoFB(String token) {
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("https://graph.facebook.com/v2.12/me?fields=first_name,email&access_token=" + token);
        con.setPost(false);

        con.setHttpMethod("GET");

        con.addResponseListener(new ActionListener<NetworkEvent>() {

            @Override
            public void actionPerformed(NetworkEvent evt) {
                UserService es = new UserService();
                u = es.getFBGraph(new String(con.getResponseData()));

            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return u;
    }

    public Users getFBGraph(String json) {

        System.out.println(json);
        JSONParser j = new JSONParser();

        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

//System.out.println(jsonObject.get("email").getAsString()); //John
        Users u = new Users();

        if (jsonObject.get("email").getAsString() != null) {
            u.setEmail(jsonObject.get("email").getAsString());

        }
        if (jsonObject.get("first_name").getAsString() != null) {
            u.setUsername(jsonObject.get("first_name").getAsString());

        }

        return u;

    }
    String status;

    public void addUser(Users user) {

        ConnectionRequest con = new ConnectionRequest();
     
con.setUrl("http://localhost/register.php?password=" + user.getPassword() + "&username=" + user.getUsername() + "&email=" + user.getEmail()+"&nom="+user.getNom()+"&prenom="+user.getPrenom());
        con.setPost(true);
        con.addResponseListener(new ActionListener<NetworkEvent>() {

            @Override
            public void actionPerformed(NetworkEvent evt) {

                status = register(new String(con.getResponseData()));

            }

        });

        NetworkManager.getInstance().addToQueueAndWait(con);
        System.out.println(user);

    }

    public String register(String status) {
        if (status.equals("nice")) {
            return "good";
        } else {
            return "notgood";
        }
    }

    public void updateUser(Users user) {

        ConnectionRequest r = new ConnectionRequest();
        r.setUrl("http://localhost/edituser.php?id=" + user.getId() + "&username=" + user.getUsername() + "&email=" + user.getEmail());
        r.setPost(false);
        NetworkManager.getInstance().addToQueueAndWait(r);
        System.err.println(r.getResponseCode());
    }

}
