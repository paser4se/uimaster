/******************Web Socket Solution of Node.js******************/

var DEBUG = true;
var dbhost;
var dbuser;
var dbpassword;
var dbdatabase;
var keyStorePass;

process.argv.forEach(function (val, index, array) {
  console.log(index + ': ' + val);
  if (val.indexOf("debug=false") != -1) {
  	 DEBUG = false;
  }
  if (val.indexOf("dbhost=") != -1) {
  	 dbhost = val.substring(val.indexOf("dbhost=") + 7);
  }
  if (val.indexOf("dbuser=") != -1) {
  	 dbuser = val.substring(val.indexOf("dbuser=") + 7);
  }
  if (val.indexOf("dbpassword=") != -1) {
  	 dbpassword = val.substring(val.indexOf("dbpassword=") + 11);
  }
  if (val.indexOf("dbdatabase=") != -1) {
  	 dbdatabase = val.substring(val.indexOf("dbdatabase=") + 11);
  }
  if (val.indexOf("keyStorePass=") != -1) {
  	 keyStorePass = val.substring(val.indexOf("keyStorePass=") + 13);
  }
});
//console.log("dbhost=" + dbhost + ",dbuser=" + dbuser + ",dbpassword=" + dbpassword + ",dbdatabase=" + dbdatabase);

var express = require('express');
var app = express();
app.locals.title = 'Vogerp Online Service';
app.locals.email = '';
var https = require('https');
var fs = require('fs');
var bodyParser = require('body-parser');
var frouter = express.Router();
var multer  = require('multer');

var caOptions = {
    key: fs.readFileSync('/opt/uimaster/apache-tomcat-8.0.9/conf/aliyu/vogerp.key'),
    cert: fs.readFileSync('/opt/uimaster/apache-tomcat-8.0.9/conf/aliyu/vogerp.pem'),
	passphrase: keyStorePass
};
var server = https.createServer(caOptions, app);
var io = require('socket.io').listen(server);

var mysql = require('mysql');
function connectDB() {
	var pool  = mysql.createPool({
	    host     : dbhost,
		user     : dbuser,
		password : dbpassword,
		database : dbdatabase,
		supportBigNumbers:true,
		bigNumberStrings:true
	});
	return pool;
}
var pool  = connectDB();
pool.on('enqueue', function () {
  console.log('Waiting for available connection slot!');
});

// this is a distributed cache which shared for all of our systems.
var onlineUsers = {}; 
 
io.on('connection', function(socket){ 
    
    socket.on('register', function(obj){ 
        try {
	        socket.partyId = obj.partyId; 
	        if(onlineUsers.hasOwnProperty(obj.partyId)) { 
		        // just updated the socket reference.
	           onlineUsers[obj.partyId].socket = socket;
	        } else {
            	onlineUsers[obj.partyId] = {"obj": obj, "socket": socket};
            }
			socket.emit('loginSuccess', obj); 
		    if (DEBUG) {
			  console.log(obj.partyId + " registered websocket capability!");
			}
		} catch (e) {
		   console.error("registering error: " + e.stack || e);
		}
    }); 
     
    socket.on('unregister', function(obj){ 
        try {
	        if(onlineUsers.hasOwnProperty(obj.partyId)) { 
	            delete onlineUsers[obj.partyId]; 
	            if (DEBUG) {
	              console.log(obj.partyId+' exit!'); 
	            }
	        } 
	    } catch (e) {
		   console.error("unregister error: " + e.stack || e);
		}
    }); 
	
	socket.on('isUserOnline', function(obj){ 
	    try {
	        if(onlineUsers.hasOwnProperty(obj.partyId)) { 
			    obj.state = "yes";
	            socket.emit('isUserOnline', obj); 
	        } else {
				obj.state = "no";
				socket.emit('isUserOnline', obj); 
			}	
		} catch (e) {
		   console.error("isUserOnline error: " + e.stack || e);
		}
    }); 
    
    socket.on('history', function(obj){ 
	    if(!onlineUsers.hasOwnProperty(socket.partyId)) { 
		   socket.emit('service_nopermission', obj); 
		   return;
		}
	    try {
		  //{fromPartyId: fromPartyId, toPartyId: toPartyId, sessionId: sessionId}
		  if (!obj.sessionId || obj.sessionId == "null" || obj.sessionId == "") {
		     return;
		  }
		  
		  if (DEBUG) {
		     console.log("query history: " + JSON.stringify(obj));
		  }
		  pool.getConnection(function(err, connection) {
			  connection.query("SELECT ID, SESSIONID, SENTPARTYID, RECEIVEDPARTYID, MESSAGE, DATE_FORMAT(CREATEDATE, '%Y-%m-%d %h:%i:%s %p') as CREATEDATE FROM WF_CHATHISTORY WHERE SESSIONID=? ORDER BY createdate ASC", [obj.sessionId], function(err, results, fields) {
					connection.release();
					if (err) {
						if (err.code === 'PROTOCOL_CONNECTION_LOST') {
						  connectDB()
						} else {
						  console.error(err.stack || err);
						}
					}
					if (DEBUG) {
					  console.log(results);
					}
					//for(var i=0;i<results.length; i++) { 
						//var row = results[i]; 
					//} 
				    socket.emit('history', results); 
			   });
		   });
		   
        } catch(e){
           console.error("query history error: " + e.stack || e);
        }
    }); 
    
    socket.on('notifihistory', function(obj){ 
	    if(!onlineUsers.hasOwnProperty(socket.partyId)) { 
		   socket.emit('service_nopermission', obj); 
		   return;
		}
	    try {
	      pool.getConnection(function(err, connection) {
			  connection.query("SELECT ID,PARTYID,SESSIONID,SUBJECT,DESCRIPTION,DATE_FORMAT(CREATEDATE, '%Y-%m-%d %h:%i:%s %p') as CREATEDATE FROM wf_notification WHERE partyid=? ORDER BY createdate DESC", [parseInt(obj.partyId)], function(err, results, fields) {
					connection.release();
					if (err) {
						if (err.code === 'PROTOCOL_CONNECTION_LOST') {
						  connectDB()
						} else {
						  console.error(err.stack || err);
						}
					}
					if (DEBUG) {
					  console.log(results);
					}
				    socket.emit('notifyhistory', results); 
			   });
		  });
		   
        } catch(e){
           console.error("query history error: " + e.stack || e);
        }
    }); 
    socket.on('notifyCountHistory', function(obj){ 
	    if(!onlineUsers.hasOwnProperty(socket.partyId)) { 
		   socket.emit('service_nopermission', obj); 
		   return;
		}
		try {
	      pool.getConnection(function(err, connection) {
			  connection.query('SELECT count(*) as count FROM wf_notification WHERE partyid=?', [parseInt(obj.partyId)], function(err, results, fields) {
					connection.release();
					if (err) {
						if (err.code === 'PROTOCOL_CONNECTION_LOST') {
						  connectDB()
						} else {
						  console.error(err.stack || err);
						}
					}
					if (DEBUG) {
					  console.log(results);
					}
				    socket.emit('notifyCount', results[0].count); 
			   });
		  });
		   
        } catch(e){
           console.error("query history error: " + e.stack || e);
        }
    });
    
	socket.on('chatTo', function(obj){ 
	    if(!onlineUsers.hasOwnProperty(socket.partyId)) { 
		   socket.emit('service_nopermission', obj); 
		   return;
		}
		try {
		    if (DEBUG) {
		      console.log("chating to PartyId: " + obj.toPartyId + ",from id: " + obj.fromPartyId+ ",session id: " + obj.sessionId);
		    }
			if(!onlineUsers.hasOwnProperty(obj.toPartyId)) { 
			   // leave a word.
			   var hint = obj.content;
			   if(obj.content.indexOf("[/audio]:") != -1) {
				   hint = "\u8BED\u97F3\u7559\u8A00";
			   }
	           var desc = "<div><span>"+hint+"</span><button onclick=\"javascript:defaultname.showHelp(defaultname.helpIcon,'"+obj.fromPartyId+"','"+obj.sessionId+"');\">\u9A6C\u4E0A\u8054\u7CFB</button></div>";
	           var msg0 = {"subject": "\u60A8\u6709\u65B0\u7684\u6D88\u606F!", "description": desc, "partyId": obj.toPartyId, "sessionid": obj.sessionId};
	           pool.getConnection(function(err, connection) {
		           connection.query('INSERT INTO WF_NOTIFICATION SET ?', msg0, function(err, result) {
		            connection.release();
					if (err) {
						if (err.code === 'PROTOCOL_CONNECTION_LOST') {
						  connectDB()
						} else {
						  console.error(err.stack || err);
						}
					} else {
						if (DEBUG) {
						  console.log("WF_NOTIFICATION inserted: " + JSON.stringify(msg0));
						}
					}
				   });
			   });
			   
	           var msg = {"sentpartyid":obj.fromPartyId, "receivedpartyid":obj.toPartyId,
						"message":obj.content, "sessionid": obj.sessionId};
			   pool.getConnection(function(err, connection) {
				   connection.query('INSERT INTO WF_CHATHISTORY SET ?', msg, function(err, result) {
				      connection.release();
				      if (err) {
			            console.error(err.stack || err);
			          } else {
						  if (DEBUG) {
							console.log("WF_CHATHISTORY inserted: " + JSON.stringify(msg));
						  }
					  }
				   });
			   });
			   
			   socket.emit('user_offline', obj); 
			   if (DEBUG) {
			       console.log('user_offline: '+ JSON.stringify(obj));
			   }
			   return;
			}
			var msg1 = {"sentpartyid":obj.fromPartyId, "receivedpartyid":obj.toPartyId,
						"message":obj.content, "sessionid": obj.sessionId};
			pool.getConnection(function(err, connection) {
			    connection.query('INSERT INTO WF_CHATHISTORY SET ?', msg1, function(err, result) {
		           connection.release();
			       if (err) {
			          console.error(err.stack || err);
			       } else {
					   if (DEBUG) {
						  console.log("WF_CHATHISTORY inserted: " + JSON.stringify(msg1));
					   }
				   }
			    });
			});
			
		    onlineUsers[obj.fromPartyId].socket.emit('chatTo', obj); 
		    if (obj.fromPartyId != obj.toPartyId) {
		        onlineUsers[obj.toPartyId].socket.emit('chatTo', obj); 
		    }
	        if (DEBUG) {
	           console.log(obj.fromPartyId +' says: '+obj.content); 
	        }
        } catch(e){
            console.error("chatTo error: " + e.stack || e);
        }
    }); 
	
	socket.on('comment', function(obj){ 
	    if(!onlineUsers.hasOwnProperty(socket.partyId)) { 
		   socket.emit('service_nopermission', obj); 
		   return;
		}
		try {
		    pool.getConnection(function(err, connection) {
				connection.query('INSERT INTO WF_comment SET (?,?,?)', {A: 'test', B: 'test', }, function(err, result) {
					connection.release();
					if (err) {
						if (err.code === 'PROTOCOL_CONNECTION_LOST') {
						  connectDB()
						} else {
						  console.error(err.stack || err);
						}
					}
					if (DEBUG) {
					  console.log(result);
					}
				});
			});
        } catch(e){
            console.error("comment error: " + e.stack || e);
        }
    });
	socket.on('hit_comment', function(obj){ 
	    if(!onlineUsers.hasOwnProperty(socket.partyId)) { 
		   socket.emit('service_nopermission', {data:obj}); 
		   return;
		}
		try {
		    pool.getConnection(function(err, connection) {
				connection.query('UPDATE FROM WF_comment SET (?,?,?)', {A: 'test', B: 'test', }, function(err, result) {
				    connection.release();
					if (err) {
						if (err.code === 'PROTOCOL_CONNECTION_LOST') {
						  connectDB()
						} else {
						  console.error(err.stack || err);
						}
					}
					//todo:
					console.log(result);
				});
			});
        } catch(e){
            console.error("hit_comment error: " + e.stack || e);
        }
    });
    
}); 

app.use('/', express.static(__dirname + '/www'));
app.use(function(req, res, next){
  delete req.headers['content-encoding'];
  next();
});
app.use(bodyParser.json()); // for parsing application/json
app.use(bodyParser.urlencoded({ extended: true })); // for parsing application/x-www-form-urlencoded
var reqparams = function(req){
  var q=req.url.split('?'),result={};
  if(q.length>=2){
      q[1].split('&').forEach((item)=>{
           try {
             result[item.split('=')[0]]=item.split('=')[1];
           } catch (e) {
             result[item.split('=')[0]]='';
           }
      })
  }
  return result;
};

var notifyHandler = function(req, res){ 
    try {
	    var d = req.body;
	    if (DEBUG) {
	      console.log("received request: " + d);
	    }
	    if (d.hasOwnProperty("toAll")) {
			io.emit('notifySingleItem', d); 
			io.emit('notifyCount', {v: 1}); 
			res.send('sent'); 
			return;
		} 
		
		if(!onlineUsers.hasOwnProperty(d.partyId)) { 
		   res.send('service_nopermission'); 
		   return;
		}
		onlineUsers[d.partyId].socket.emit('notifySingleItem', [d]); 
		if (d.hasOwnProperty("latitude")) {
		   //TODO: notify all online user in the same login area!
		}
		
	    res.send('sent'); 
    } catch(e){
        console.error("notifyHandler error: " + e.stack || e);
        res.send('error');
    }
};

var onlineInfoHandler = function(req, res){ 
    try {
	    var params = reqparams(req);
	    if (DEBUG) {
	      console.log("received request parameters: " + Object.keys(params));
	    }
		if (params.type) {
		   if (params.type == "register") {
		      try {
	            onlineUsers[params.partyId] = {"obj": params};
			    if (DEBUG) {
				  console.log(params.partyId + " joined online! request json: " + JSON.stringify(params));
				}
			  } catch (e) {
			    console.error("registering error: " + e.stack || e);
			  }
		      res.send("1"); 
		   } else if (params.type == "userCount") {
		      res.send(Object.keys(onlineUsers).length + ""); 
		   } else if (params.type == "checkUserOnline") {
		      res.send(onlineUsers.hasOwnProperty(params.userId) + "");
		   } else if (params.type == "logout" && onlineUsers.hasOwnProperty(params.userId)) {
		      try {
	            delete onlineUsers[params.userId]; 
	            if (DEBUG) {
	              console.log(params.userId+' exit!'); 
	            }
			  } catch (e) {
			    console.error("unregister error: " + e.stack || e);
			  }
		      res.send("1");
		   } else {
		      res.send("fail");
		   }
		} else {
		   res.send("fail");
		}
    } catch(e){
        console.error("notifyHandler error: " + e.stack || e);
        res.send('fail');
    }
};
app.get('/', function(req, res){
  res.send('Hi, what can we do for you?');
});
app.get('/uimaster/notify', notifyHandler); 
app.post('/uimaster/notify', notifyHandler);
app.get('/uimaster/onlineinfo', onlineInfoHandler); 

var createFolder = function(folder){  
   try{   
      fs.accessSync(folder);    
   }catch(e){   
      fs.mkdirSync(folder, 0777);  
   }
};  
var audioRoot = process.cwd() + '/upload/audio/';     
createFolder(audioRoot);
   
var storage = multer.diskStorage({
    destination: function (req, file, cb) {
	    var params = reqparams(req);
		var savePath = audioRoot + params.uid;
		if (!fs.existsSync(savePath)) {
           fs.mkdirSync(savePath, 0777);
		}
		//it's unsupported recursive folder.
		savePath = savePath +"/"+ params.sid;
        if (!fs.existsSync(savePath)) {
           fs.mkdirSync(savePath, 0777);
		}		
        cb(null, savePath);  
		console.log('savePath：%s', savePath);
    },   
    filename: function(req, file, cb) { 
	    if (DEBUG) {
		console.log('file.mimetype：%s', file.mimetype);
	    console.log('file.filename%s', file.filename);
		console.log('file.originalname：%s', file.originalname);
	    console.log('file.size：%s', file.size);
		}
	    req.originalname = file.originalname;
        cb(null, file.originalname); 
    },
	fileFilter: function(req, file, cb) {
	  if (file.originalname.indexOf(".amr") == -1) {  
	    // To reject this file pass `false`, like so:
	    cb(null, false);
	  } else {
		cb(null, true);
	  }
	},
	limits: {"files": 1}
}); 

var audioupload = multer({ storage: storage });
//var audioupload = multer({ dest: 'upload/audio/'}); 
app.post('/audio/add', audioupload.any(), function(req, res, next){  
   var params = reqparams(req);
   var savePath = audioRoot + params.uid +"/"+ params.sid + "/" + req.originalname;
   var savePath1 = audioRoot + params.uid +"/"+ params.sid + "/" + req.originalname.replace(".amr",".mp3");
   var exec = require('child_process').exec; 
   var cmdStr = "ffmpeg -i " + savePath + " " + savePath1;
   console.log(cmdStr);
   exec(cmdStr, function(err,stdout,stderr){
	   if(err) {
		   console.log('execute command error:'+stderr);
	   } else {
		   //console.log('executed command:'+stdout);
	   }
   }); 	
   res.send({ret_code: '1'});
}); 

server.listen(8090, function(){ 
    console.log('listening on *:8090'); 
});
