package com.bigbrainiac10.simplehelpop;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

import vg.civcraft.mc.civmodcore.ACivMod;
import vg.civcraft.mc.mercury.MercuryAPI;

import com.bigbrainiac10.simplehelpop.commands.SimpleHelpCommandManager;
import com.bigbrainiac10.simplehelpop.database.Database;
import com.bigbrainiac10.simplehelpop.database.DatabaseManager;
import com.bigbrainiac10.simplehelpop.listeners.MercuryListener;
import com.bigbrainiac10.simplehelpop.listeners.PlayerListener;
import com.bigbrainiac10.simplehelpop.listeners.QuestionListener;

public class SimpleHelpOp extends ACivMod{

	private static Database db;
	private static SimpleHelpOp instance;
	private static Logger logger;
	private static DatabaseManager dbm;
	private static boolean nameLayerEnabled = false;
	private static boolean mercuryEnabled = false;
	
	public void onEnable(){
		handle = new SimpleHelpCommandManager();
		handle.registerCommands();
		super.onEnable();
		instance = this;
		logger = this.getLogger();
		
		if (Bukkit.getPluginManager().isPluginEnabled("NameLayer")) {
			nameLayerEnabled = true;
		}
		if (Bukkit.getPluginManager().isPluginEnabled("Mercury")) {
			mercuryEnabled = true;
			MercuryAPI.registerPluginMessageChannel("SimpleHelpOp");
			Bukkit.getPluginManager().registerEvents(new MercuryListener(), this);
		}
		
		saveDefaultConfig();
		reloadConfig();
		new SHOConfigManager(getConfig());
		
		initializeDatabase();
		dbm = new DatabaseManager(db);
		
		registerListeners();
		registerCommands();
	}
	
	public void onDisable(){
		db.close();
	}
	
	public String getPluginName() {
		return "SimpleHelpOp";
	}
	
	public static void Log(String message){
		Log(Level.INFO, message);
	}

	public static void Log(String message, Object...vars){
		Log(Level.INFO, message, vars);
	}

	public static void Log(Level level, String message, Object...vars){
		logger.log(level, message, vars);
	}

	public static Database getDB(){
		return db;
	}
	
	public static SimpleHelpOp getInstance(){
		return instance;
	}
	
	public static DatabaseManager getHelpOPData(){
		return dbm;
	}
	
	public static boolean isNameLayerEnabled() {
		return nameLayerEnabled;
	}
	
	public static boolean isMercuryEnabled() {
		return mercuryEnabled;
	}
	
	private void initializeDatabase(){
		String host = SHOConfigManager.getHostName();
		String user = SHOConfigManager.getUserName();
		String password = SHOConfigManager.getPassword();
		int port = SHOConfigManager.getPort();
		String dbName = SHOConfigManager.getDBName();
		
		db = new Database(host, port, dbName, user, password, getLogger());
	}
	
	private void registerListeners(){
		Bukkit.getPluginManager().registerEvents(new QuestionListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
	}	
}
