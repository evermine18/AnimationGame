package com.evermine.animgame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.WebSockets;

import java.util.ArrayList;
import java.util.Map;

public class ServerConnection {
    private WebSocket socket;
    private String address;
    private int port;
    private String id="";

    public ServerConnection(String address,int port){
        this.address=address;
        this.port=port;
        //Creating websocket connection
        if(Gdx.app.getType()== Application.ApplicationType.Android )
            // en Android el host és accessible per 10.0.2.2
            this.address = "10.0.2.2";
        socket = WebSockets.newSocket(WebSockets.toWebSocketUrl(this.address, this.port));
        socket.setSendGracefully(false);
        socket.addListener((WebSocketListener) new MyWSListener());
        socket.connect();
        if(socket!=null){
            MyGdxGame.gameLogs.add("INFO","Successfuly connected to the server");
        }else{
            MyGdxGame.gameLogs.add("ERROR","We cannot create the connection");
        }

    }
    private void updateAllPlayers(Object data){
        Map<String, Object> players = (Map<String, Object>) data;
        MyGdxGame.players = new ArrayList<Player>();
        for(Map.Entry<String, Object> player : players.entrySet()) {
            Map<String, Object> playerData = (Map<String, Object>) player.getValue();
            int x = (int) playerData.get("x");
            int y = (int) playerData.get("y");
            if(!id.equals(player.getKey())) {
                MyGdxGame.players.add(new Player(MyGdxGame.txt, x,y));
            }
        }
    }
    public void sendPlayerPos(int x, int y, int state) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = mapper.createObjectNode();
            rootNode.put("action", "sendPos");
            rootNode.put("x", x);
            rootNode.put("y", y);
            rootNode.put("state", state);
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
            socket.send(jsonString);
        }catch (Exception e){
            MyGdxGame.gameLogs.add("ERROR", "Error sending coords to server");
        }
    }
    public void setID(String id){
        this.id=id;
    }
    public void close(){
        this.socket.close();
    }

    class MyWSListener implements WebSocketListener {

        @Override
        public boolean onOpen(WebSocket webSocket) {
            System.out.println("Opening...");
            return false;
        }

        @Override
        public boolean onClose(WebSocket webSocket, int closeCode, String reason) {
            System.out.println("Closing...");
            return false;
        }

        @Override
        public boolean onMessage(WebSocket webSocket, String packet) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> reciv = objectMapper.readValue(packet, Map.class);
                if(reciv.get("action").equals("usersData")){
                    updateAllPlayers(reciv.get("data"));
                }else if(reciv.get("action").equals("setID")){
                    setID(String.valueOf(reciv.get("id")));
                }
                else if(reciv.get("action").equals("alert")){
                    MyGdxGame.gameLogs.add("INFO", String.valueOf(reciv.get("text")));
                }
            }catch (Exception e){
                MyGdxGame.gameLogs.add("ERROR", "Error reading the incomming message");
            }

            return false;
        }

        @Override
        public boolean onMessage(WebSocket webSocket, byte[] packet) {
            System.out.println("Message:");
            return false;
        }

        @Override
        public boolean onError(WebSocket webSocket, Throwable error) {
            System.out.println("ERROR:"+error.toString());
            MyGdxGame.gameLogs.add("ERROR", "We cannot create the server connection");
            return false;
        }
    }
}
