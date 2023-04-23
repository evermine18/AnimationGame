    import { createServer } from 'http';
    import { WebSocketServer } from 'ws';
    import { v4 as uuidv4 } from 'uuid';
     
    const server = createServer();
    const wss = new WebSocketServer({ server });

    var users = {}

    wss.on('connection', function connection(ws) {
      const id = uuidv4();
      users[id]={"x":200,"y":100,"state":0}
      console.log("[LOG] New user connected");
      ws.send(JSON.stringify({"action":"setID","id":id}));
      ws.send(JSON.stringify({"action":"alert","text":"A new player has joined"}));
      ws.send(JSON.stringify({"action":"usersData","data":users}));
     
      ws.on('error', console.error);
     
      ws.on('message', function message(data) {
        //console.log('received: %s', data);
        let recv = JSON.parse(data);
        if(recv.action=="sendPos"){
          users[id]={"x":recv.x,"y":recv.y,"state":recv.state}
          // Sending the new position to all clients
          wss.clients.forEach(function each(client) {
            client.send(JSON.stringify({"action":"usersData","data":users}));
            if (client.readyState === WebSocketServer.OPEN) {
              
              
            }
          });
        }
      });
     
      ws.on('close', function close() {
        console.log("Tancant connexió.")
        delete users[id];
      })
     
    });
     
    server.listen( 8888, function() {
      console.log("listening...");
    });

