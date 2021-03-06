package de.mreturkey.authyou.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import de.mreturkey.authyou.AuthYou;

public final class Config extends YamlConfiguration {

	public final File configFile;
	public final YamlConfiguration instance;
	
	public static Database getDatabase;
	public static String getSQLColumnUsername, getSQLColumnUUID, getSQLTableName, getSQLColumnLastLogin,
		getSQLColumnIp, getSQLColumnPassword, getSQLColumnLastLocX, getSQLColumnLastLocY, getSQLColumnLastLocZ,
		getSQLColumnLastLocWorld, getSQLColumnId, getSQLColumnLogged;
	public static boolean getSessionsEnabled, getRegisterEnabled, getSessionExpireOnIpChange;
	public static Date getSessionTimeOut;
	public static boolean allowChat, kickNonRegistered, kickOnWrongPassword, kickViaBungeeCord, allowMovement, stopServerOnSQLError;
	public static List<String> allowCommands;
	public static int maxRegPerIp, maxNicknameLength, minNicknameLength, timeout, minPasswordLength;
	public static Pattern allowedNicknameCharacters, allowedPasswordCharacters;
	
	
	public Config() {
//		super();
		
		File file = new File(AuthYou.getInstance().getDataFolder(), "config.yml");
		
        try {
            this.load(file);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file , ex);
        } finally {
        	configFile = file;
        	instance = this;
        	loadConfig();
        }
	}
	
	public void reload() {
		try {
			this.load(configFile);
			this.loadConfig();
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void loadConfig() {
		getDatabase = new Database(
				instance.getString("DataSource.mySQLHost", "localhost"),
				instance.getInt("DataSource.mySQLPort", 3306),
				instance.getString("DataSource.mySQLDatabase", "authyou"),
				instance.getString("DataSource.mySQLUsername", "username"),
				instance.getString("DataSource.mySQLPassword", "pass1234"));
        getSQLTableName = instance.getString("DataSource.mySQLTablename", "authyou");
        getSQLColumnUsername = instance.getString("DataSource.mySQLColumnName", "username");
        getSQLColumnUUID = instance.getString("DataSource.mySQLColumnUUID", "uuid");
        getSQLColumnPassword = instance.getString("DataSource.mySQLColumnPassword", "password");
        getSQLColumnIp = instance.getString("DataSource.mySQLColumnIp", "ip");
        getSQLColumnLastLogin = instance.getString("DataSource.mySQLColumnLastLogin", "last_login");
        getSQLColumnLastLocX = instance.getString("DataSource.mySQLlastlocX", "x");
        getSQLColumnLastLocY = instance.getString("DataSource.mySQLlastlocY", "y");
        getSQLColumnLastLocZ = instance.getString("DataSource.mySQLlastlocZ", "z");
        getSQLColumnLastLocWorld = instance.getString("DataSource.mySQLlastlocWorld", "world");
        getSQLColumnId = instance.getString("DataSource.mySQLColumnId", "id");
        getSQLColumnLogged = instance.getString("DataSource.mySQLColumnLogged", "is_logged");
        
        getSessionsEnabled = instance.getBoolean("settings.sessions.enabled", true);
        final int sessionTimeout = instance.getInt("settings.sessions.timeout", 3);
        final TimeUnit sessionTimeUnit = convertTimeUnit(instance.getString("settings.sessions.TimeUnit", "DAYS"), TimeUnit.DAYS);
        getSessionTimeOut = new Date(sessionTimeUnit.toMillis(sessionTimeout));
        getSessionExpireOnIpChange = instance.getBoolean("settings.sessions.sessionExpireOnIpChange", true);
        
        allowChat = instance.getBoolean("settings.restrictions.allowChat", false);
        allowCommands = instance.getStringList("settings.restrictions.allowCommands");
        if(allowCommands.isEmpty()) {
        	allowCommands = new ArrayList<>(Arrays.asList("/login", "/l", "/register", "/reg"));
        }
        maxRegPerIp = instance.getInt("settings.restrictions.maxRegPerIp", 1);
        maxNicknameLength = instance.getInt("settings.restrictions.maxNicknameLength", 20);
        kickNonRegistered = instance.getBoolean("settings.restrictions.kickNonRegistered", false);
        kickOnWrongPassword = instance.getBoolean("settings.restrictions.kickOnWrongPassword", false);
        kickViaBungeeCord = instance.getBoolean("settings.restrictions.kickViaBungeeCord", false);
        minNicknameLength = instance.getInt("settings.restrictions.minNicknameLength", 3);
        allowMovement = instance.getBoolean("settings.restrictions.allowMovement", false);
        timeout = instance.getInt("settings.restrictions.timeout", 30);
        allowedNicknameCharacters = Pattern.compile(instance.getString("settings.restrictions.allowedNicknameCharacters", "[a-zA-Z0-9_]*"));
        allowedPasswordCharacters = Pattern.compile(instance.getString("settings.restrictions.allowedPasswordCharacters", "[\\x21-\\x7E]*"));
        
        getRegisterEnabled = instance.getBoolean("settings.registration.enabled", true);
        
        minPasswordLength = instance.getInt("settings.security.minPasswordLength", 4);
        
        stopServerOnSQLError = instance.getBoolean("Security.SQLProblem.stopServer", true);
        if(!configFile.exists())
			try {
				instance.set("DataSource.mySQLHost", "localhost");
				instance.set("DataSource.mySQLPort", 3306);
				instance.set("DataSource.mySQLDatabase", "authyou");
				instance.set("DataSource.mySQLUsername", "username");
				instance.set("DataSource.mySQLColumnUUID", "uuid");
				instance.set("DataSource.mySQLPassword", "pass1234");
				instance.set("DataSource.mySQLTablename", "authyou");
				instance.set("DataSource.mySQLColumnName", "username");
				instance.set("DataSource.mySQLColumnPassword", "password");
				instance.set("DataSource.mySQLColumnIp", "ip");
				instance.set("DataSource.mySQLColumnLastLogin", "last_login");
				instance.set("DataSource.mySQLlastlocX", "x");
				instance.set("DataSource.mySQLlastlocY", "y");
				instance.set("DataSource.mySQLlastlocZ", "z");
				instance.set("DataSource.mySQLlastlocWorld", "world");
				instance.set("DataSource.mySQLColumnId", "id");
		        instance.set("DataSource.mySQLColumnLogged", "is_logged");
				
				instance.set("settings.sessions.enabled", true);
		        instance.set("settings.sessions.timeout", 3);
		        instance.set("settings.sessions.TimeUnit", "DAYS");
		        
		        instance.set("settings.sessions.sessionExpireOnIpChange", true);
		        
		        instance.set("settings.restrictions.allowChat", false);
		        List<String> cmds = new ArrayList<>(Arrays.asList("/login", "/l", "/register", "/reg"));
		        instance.set("settings.restrictions.allowCommands", cmds);
		        
		        instance.set("settings.restrictions.maxRegPerIp", 1);
		        instance.set("settings.restrictions.maxNicknameLength", 20);
		        instance.set("settings.restrictions.kickNonRegistered", false);
		        instance.set("settings.restrictions.kickOnWrongPassword", false);
		        instance.set("settings.restrictions.kickViaBungeeCord", false);
		        instance.set("settings.restrictions.minNicknameLength", 3);
		        instance.set("settings.restrictions.allowMovement", false);
		        instance.set("settings.restrictions.timeout", 30);
		        instance.set("settings.restrictions.allowedNicknameCharacters", "[a-zA-Z0-9_]*");
		        instance.set("settings.restrictions.allowedPasswordCharacters", "[\\x21-\\x7E]*");
		        instance.set("settings.registration.enabled", true);
		        instance.set("settings.security.minPasswordLength", 4);
		        
		        instance.set("Security.SQLProblem.stopServer", true);
				
				instance.save(configFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	private static TimeUnit convertTimeUnit(String val, TimeUnit defaultUnit) {
		switch (val.toUpperCase()) {
		case "DAYS":
			return TimeUnit.DAYS;
		case "HOURS":
			return TimeUnit.HOURS;
		case "MICROSECONDS":
			return TimeUnit.MICROSECONDS;
		case "MILLISECONDS":
			return TimeUnit.MILLISECONDS;
		case "MINUTES":
			return TimeUnit.MINUTES;
		case "NANOSECONDS":
			return TimeUnit.NANOSECONDS;
		case "SECONDS":
			return TimeUnit.SECONDS;
		default:
			throw new RuntimeException("");
		}
	}
	
	
}
