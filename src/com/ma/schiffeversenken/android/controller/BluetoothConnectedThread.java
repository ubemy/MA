package com.ma.schiffeversenken.android.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.badlogic.gdx.Gdx;
import com.ma.schiffeversenken.GameFieldScreen;
import com.ma.schiffeversenken.android.AndroidLauncher;
import com.ma.schiffeversenken.android.BackActivity;
import com.ma.schiffeversenken.android.R;
import com.ma.schiffeversenken.android.model.FieldUnit;
import com.ma.schiffeversenken.android.model.Player;
import com.ma.schiffeversenken.android.view.CreateMultiplayerGame;
import com.ma.schiffeversenken.android.view.VisitMultiplayerGame;

import android.app.Activity;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

/**
 * Wird aufgerufen, wenn eine Verbindung hergestellt wurde.
 * Wird auf Server- und Clientseite verwendet
 * @author Maik Steinborn
 * @author Klaus Schlender
 */
public class BluetoothConnectedThread extends Thread {
	/**Buffer Groesse fuer den Bluetooth Stream Reader*/
	public static final int BUFFER_SIZE = 1024*1;
	/**Bluetooth Socket Objekt*/
	private final BluetoothSocket bluetoothSocket;
	/**Eingehender Stream*/
    private final InputStream inStream;
    /**Ausgehender Stream*/
    private final OutputStream outStream;
    /**Initialisiertes Game Objekt*/
    private Game game;
    /**Initialisiertes VisitMultiplayerGame Objekt*/
    private VisitMultiplayerGame vmgClass;
    /**Initialisiertes CreateMultiplayerGame Objekt*/
    private CreateMultiplayerGame cmgClass;
    /**Bluetooth Adapter Objekt*/
    private BluetoothAdapter bluetoothAdapter;
    /**Für FeldUnits[][]*/
	private String subStringIncomeing="";
    /**Statische Instanz dieser Klasse*/
    private static BluetoothConnectedThread instance;
    /**String, der bei der Bluetooth Kommunikation genutzt wird.
     * HELLO bedeutet Bluetooth Verbindung erfolgreich aufgebaut*/
    public static final String BLUETOOTH_HELLO = "_HELLO_";
    /**String, der bei der Bluetooth Kommunikation genutzt wird.
     * ATTACK bedeutet Attacke auf eine ID*/
    public static final String BLUETOOTH_ATTACK = "_ATTACK_";
    /**String, der bei der Bluetooth Kommunikation genutzt wird.
     * RETURN bedeutet Ergebnis einer Attacke an den Gegner zurueck senden*/
    public static final String BLUETOOTH_RETURN = "_RETURN_";
    public static final String BLUETOOTH_ENEMY_FIELD = "_FIELD_";
    public static final String BLUETOOTH_ENEMY_FIELD_RETURN = "_FRETURN_";
    public static final String BLUETOOTH_ENEMY_SHIPS = "_SHIPS_";
    /** Für Notifikationen außerhalb der App*/
    AndroidLauncher androidLauncher;
	private int FieldMessageCounterToDoDelete;
    
    /**
     * Erstellt ein BluetoothConnectedThread Objekt
     * @param socket Erstellte Bluetooth Socket Verbindung
     * @param vmgClass Initialisiertes VisitMultiplayerGame Objekt
     * @param cmgClass Initialisiertes CreateMultiplayerGame Objekt
     * @param bluetoothAdapter Bluetooth Adapter Objekt
     * @param game Initialisiertes Game Objekt
     */
    public BluetoothConnectedThread(BluetoothSocket socket, VisitMultiplayerGame vmgClass, CreateMultiplayerGame cmgClass, BluetoothAdapter bluetoothAdapter) {
    	bluetoothSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        this.vmgClass = vmgClass;
        this.cmgClass = cmgClass;
        this.bluetoothAdapter = bluetoothAdapter;
        
        //Temporaeres Objekt benutze, da Streams final sind
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }
 
        inStream = tmpIn;
        outStream = tmpOut;
    	sendHello();
    	instance = this;
    }
 
    /**
     * Gibt die statische Instanz dieser Klasse zurueck
     * @return Statische Instanz dieser Klasse
     */
    public static BluetoothConnectedThread getInstance(){
    	return instance;
    }
    
    /**
     * Game Instanz setzen
     * @param game Game Instanz
     */
    public void setGame(Game game){
    	this.game = game;
    }
    
    /**
     * Thread starten
     */
    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];  // buffer store for the stream
        int bytes; // bytes returned from read()
 
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
            	if(bluetoothSocket.isConnected()){
	                /*
	                 * Vom InputStream lesen
	                 * Und dann je nach Art der Message Aktion ausfuehren
	                 */
	                bytes = inStream.read(buffer);
	                
	                String readMsg = new String(buffer, 0, bytes);
	                

	                if(readMsg.startsWith(BLUETOOTH_ATTACK)){
	                	if(game.getPrimaryBTGame()){
	                		game.secondGamerAttack(Integer.parseInt(readMsg.substring(readMsg.indexOf(BLUETOOTH_ATTACK) + BLUETOOTH_ATTACK.length())));
	                	}
	                	else if(game.getSecondaryBTGame()){
	                		game.firstGamerAttack(Integer.parseInt(readMsg.substring(readMsg.indexOf(BLUETOOTH_ATTACK) + BLUETOOTH_ATTACK.length())));
	                	}
	                }
	                else if(readMsg.startsWith(BLUETOOTH_HELLO)){
	                	String helloString;
	                	if(vmgClass != null){
	                		helloString = vmgClass.getString(R.string.successfully_connected_with) + readMsg.substring(BLUETOOTH_HELLO.length());
	                		this.vmgClass.showToast(helloString);
	                	}
	                	else{
	                		helloString = cmgClass.getString(R.string.successfully_connected_with) + readMsg.substring(BLUETOOTH_HELLO.length());
	                		this.cmgClass.showToast(helloString);
	                	}
	                }
	                else if(readMsg.startsWith(BLUETOOTH_RETURN)){
	                	String returnAttackHitString = readMsg.substring(readMsg.indexOf(BLUETOOTH_RETURN) + BLUETOOTH_RETURN.length(), readMsg.indexOf("_", readMsg.indexOf(BLUETOOTH_RETURN) + BLUETOOTH_RETURN.length()));
	                	boolean returnAttackHit = Boolean.parseBoolean(returnAttackHitString);                	
	                	
	                	String returnShipDestroyedString = readMsg.substring(readMsg.indexOf(returnAttackHitString) + returnAttackHitString.length() + 1, readMsg.indexOf("_", readMsg.indexOf(returnAttackHitString) + returnAttackHitString.length() + 1));
	                	boolean returnShipDestroyed = Boolean.parseBoolean(returnShipDestroyedString);
	                	
	                	int ind = readMsg.indexOf(returnAttackHitString) + returnAttackHitString.length() + 1 + returnShipDestroyedString.length() + 1;
	                	String returnShipDestroyedIDs = readMsg.substring(ind);
	                	
	                	game.setReturnValues(returnAttackHit, returnShipDestroyed, returnShipDestroyedIDs);
	                }
//	                else if(readMsg.startsWith(BLUETOOTH_ENEMY_FIELD_RETURN)){
//	                	game.getFirstFieldPlayer().setFeldUebertragen(true);
//	                	game.getSecondFieldEnemy().setFeldUebertragen(true);
//	                	AndroidLauncher.notificationToUser("RETURN "+" received:"+readMsg.length(),androidLauncher,androidLauncher.getClass(),BackActivity.class,androidLauncher.getNotificationManager() ,FieldMessageCounterToDoDelete+1);
//	                }
//	                else if(readMsg.startsWith(BLUETOOTH_ENEMY_FIELD)){
//        	
////	                	Gdx.app.log("BLUETOOTH_ENEMY_FIELD", "received:"+readMsg.length());
//	                	FieldMessageCounterToDoDelete=1234567;
//
//	                	AndroidLauncher.notificationToUser(readMsg.substring(BLUETOOTH_ENEMY_FIELD.length())+" received:"+readMsg.length(),androidLauncher,androidLauncher.getClass(),BackActivity.class,androidLauncher.getNotificationManager() ,FieldMessageCounterToDoDelete);
//						//Deserialisierung
//	                	byte[] fieldBytes;
//
//	                	//Return Schreiben
//						fieldBytes = (BluetoothConnectedThread.BLUETOOTH_ENEMY_FIELD_RETURN).getBytes();
//						write(fieldBytes);
//	                	
////                		AndroidLauncher.notificationToUser(readMsg.substring(BLUETOOTH_ENEMY_FIELD.length())+" received:"+readMsg.length(),androidLauncher,androidLauncher.getClass(),BackActivity.class,androidLauncher.getNotificationManager() ,1234568);
//                		
////                		FieldUnit[][] tmpFieldUnits = (FieldUnit[][])Player.fromString(readMsg.substring(BLUETOOTH_ENEMY_FIELD.length()));
////                		game.setEnemyFieldUnits(tmpFieldUnits);
//	    	          	
//	                	//TODO löschen
////	                	byte[] readBuf = new byte[83402]; // this array will hold the bytes for the image, this value better be not hardcoded in your code
////
////	                	int start = 0;
////	                	while(readMsg) {
////	                	    readBuf.copyOfRange((byte[]) msg.obj, start, start + 1024); // copy received 1024 bytes
////	                	    start += 1024; //increment so that we don't overwrite previous bytes
////	                	}
////
////	                	/*After everything is read...*/
////	                	Bitmap bmp=BitmapFactory.decodeByteArray(readBuf,0,readBuf.length);
//	                	//TODO löschen bis hier
//	                	
////	                	//TODO Erste gehversuche:
////	                	if(readMsg.equals(BLUETOOTH_ENEMY_FIELD+BLUETOOTH_ENEMY_FIELD)){
////	                		//Ende vom Stream
//////	                		AndroidLauncher.notificationToUser("FieldUnits.",androidLauncher,androidLauncher.getClass(),BackActivity.class,androidLauncher.getNotificationManager() ,1234568);
////	                		FieldUnit[][] tmpFieldUnits = (FieldUnit[][])Player.fromString(subStringIncomeing);
////	                		game.setEnemyFieldUnits(tmpFieldUnits);
////	                		subStringIncomeing="";
////	                	}else{
////	                		//Stream zu String zusammenführen
////	                		subStringIncomeing= subStringIncomeing+readMsg.substring(BLUETOOTH_ENEMY_FIELD.length());
////	                	}
//	                
////	                //TODO ALTES
////	                else if(readMsg.startsWith(BLUETOOTH_ENEMY_SHIPS)){
////	                	String msgSubstring = readMsg.substring(BLUETOOTH_ENEMY_SHIPS.length());
////	                	Ship[] ships = null;
////	                	
////	                	try{
////	                		ships = (Ship[]) Player.deserialize(msgSubstring.getBytes());
////	                	} catch (ClassNotFoundException e) {
////							e.printStackTrace();
////						}
////	                	
////	                	game.setEnemyFieldShips(ships);
//	                
//	                }
            	}
            } catch (Exception e){
                break;
			} 
        }
    }
    
    /**
     * Bei Bluetooth Verbindungsabbruch neu verbinden
     */
    public void reconnect(){
    	try{
	    	if(vmgClass != null){
	    		vmgClass.connectToServer(true);
			}
			else{
				cmgClass.startServer(true);
			}
    	}
    	catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    
    /**
     * Bei erfolgreich aufgebauter Verbindung wird der Name dieses Geraets an
     * das Remote Geraet gesendet. Dieser erscheint dort dann als Toast
     */
    private void sendHello(){
    	String sendString = new String(BLUETOOTH_HELLO + " " + bluetoothAdapter.getName());
    	write(sendString.getBytes());
    }
    
    /**
     * Daten ueber Bluetooth Socket senden
     */
    public void write(byte[] bytes) {
        try {
        	outStream.write(bytes);
        } catch (IOException e) { 
        	e.printStackTrace();
        }
    }
    
    public void setAndroidLauncher(AndroidLauncher a){
    	this.androidLauncher=a;
    }
}
