    import { createServer } from 'http';
    import { WebSocketServer } from 'ws';
    import { v4 as uuidv4 } from 'uuid';
     
    const server = createServer();
    const wss = new WebSocketServer({ server });

    var users = {}

    wss.on('connection', function connection(ws) {
      const id = uuidv4();
      users[id]={"x":200,"y":100,"state":"idle"}
      console.log("[LOG] New user connected");
      ws.send(JSON.stringify({"action":"usersData","data":users}));
     
      ws.on('error', console.error);
     
      ws.on('message', function message(data) {
        //console.log('received: %s', data);
      });
     
      ws.on('close', function close() {
        console.log("Tancant connexió.")
      })
     
    });
     
    server.listen( 8888, function() {
      console.log("listening...");
    });

