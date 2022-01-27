package com.github.ukraine1449.home;

import com.github.ukraine1449.home.commands.goHome;
import com.github.ukraine1449.home.commands.playerJoinEvent;
import com.github.ukraine1449.home.commands.setHome;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public final class Home extends JavaPlugin {
public boolean preferStored = true; // if set to true in plugin.yml this will make the block bed preffered if there is block bed and /sethome set.
    @Override
    public void onEnable() {
        updatePreferredStore();
        // Plugin startup logic
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        try {
            createTableUserStats();
        } catch (Exception e) {
            e.printStackTrace();
        }//Creates table in the SQL DB if not already existing
        getCommand("home").setExecutor(new goHome(this));
        getCommand("setHome").setExecutor(new setHome(this));
       getServer().getPluginManager().registerEvents(new playerJoinEvent(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void updatePreferredStore(){
        String input = getConfig().getString("preferStoredBed");
        if(input.equals("true")&&input.equals("t")&&input.equals("True")){
            preferStored = true;
        }else{
            preferStored = false;
        }
    }
    public Connection getConnection() throws Exception{
        String ip = getConfig().getString("ip");
        String password = getConfig().getString("password");
        String username = getConfig().getString("username");
        String dbn = getConfig().getString("database name");//these 4 strings get the login info from config.yml file, and use that for DB connections
        try{
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://"+ ip + ":3306/" + dbn;
            System.out.println(url);
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected");
            return conn;
        }catch(Exception e){
            System.out.println("Unable to connect to SQL server.");
        }
        return null;
    }
    public void createTableUserStats()throws Exception{
        try{
            Connection con = getConnection();
            PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS userHomes(UUID varchar(255),bedLoc varchar(255),worldName varchar(255),hasBed varchar(255), PRIMARY KEY (UUID))");
            create.executeUpdate();
            con.close();// create table named userStats, with UUID as a string and also primary ID, along with wonGames, lostGames, kills and deaths as ints
        }catch(Exception e){}
    }
    public void updateBedPos(Player player, Location bedLoc){
        String UUID = player.getUniqueId().toString();
        try{
            Connection con = getConnection();
            PreparedStatement posted = con.prepareStatement("UPDATE userHomes SET bedLoc='"+bedLoc.toString()+"' WHERE UUID='"+UUID+"'");
            posted.executeUpdate();
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }try{
            Connection con = getConnection();
            PreparedStatement posted = con.prepareStatement("UPDATE userHomes SET hasBed='true' WHERE UUID='"+UUID+"'");
            posted.executeUpdate();
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }try{
            Connection con = getConnection();
            PreparedStatement posted = con.prepareStatement("UPDATE userHomes SET world='"+bedLoc.getWorld().getName()+"' WHERE UUID='"+UUID+"'");
            posted.executeUpdate();
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void onPlayerJoinSQL(Player player){
        String UUID = player.getUniqueId().toString();
        try{//executed when a player is joining in playerJoinEvent, basically if the players UUID isnt already in the database it adds it with all stats of 0
            Connection con = getConnection();
            PreparedStatement posted = con.prepareStatement("INSERT INTO userHomes(UUID, bedLoc, worldName, hasBed) VALUES ('"+UUID+"', false, false, false)ON DUPLICATE KEY UPDATE UUID='"+UUID+"'");
            posted.executeUpdate();
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }   public ArrayList<String> selectCD(String UUID) throws Exception {
        ArrayList<String> retuns = new ArrayList<String>();
        Connection con = getConnection();
        PreparedStatement statement = con.prepareStatement("SELECT bedLoc,hasBed,worldName FROM userHomes WHERE UUID='"+UUID+"'");
        ResultSet result = statement.executeQuery();
        while(result.next()){
            retuns.add(result.getString("hasBed"));
            retuns.add(result.getString("bedLoc"));
            retuns.add(result.getString("worldName"));
        }
        con.close();
        return retuns;
    }
    public Location getBedLoc(Player player){
        Location homeLoc = player.getLocation();
        ArrayList<String> rawSQL = new ArrayList<String>();
        try {
            rawSQL = selectCD(player.getUniqueId().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            homeLoc.setWorld(getServer().getWorld(rawSQL.get(2)));
            String[] split = rawSQL.get(1).split(",");
            homeLoc.setX(Double.parseDouble(split[1]));
            homeLoc.setY(Double.parseDouble(split[2]));
            homeLoc.setZ(Double.parseDouble(split[3]));
            System.out.println(Double.parseDouble(split[3]));
            System.out.println(Double.parseDouble(split[2]));
            System.out.println(Double.parseDouble(split[1]));
            System.out.println(homeLoc);
            System.out.println(rawSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return homeLoc;
    }
}
