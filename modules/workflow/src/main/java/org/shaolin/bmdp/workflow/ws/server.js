/******************Web Socket Solution of Node.js******************/

var DEBUG = true;
var dbhost;
var dbuser;
var dbpassword;
var dbdatabase;

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
});
console.log("dbhost=" + dbhost + ",dbuser=" + dbuser + ",dbpassword=" + dbpassword + ",dbdatabase=" + dbdatabase);

var express = require('express');
var app = express();
app.locals.title = 'Vogerp Online Service';
app.locals.email = '';
var https = require('https');
var fs = require('fs');

var caOptions = {
    key: fs.readFileSync('/opt/uimaster/chat/ca/2_www.vogerp.com.key'),
    cert: fs.readFileSync('/opt/uimaster/chat/ca/1_www.vogerp.com_bundle.crt')
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


var onlineUsers = {}; 
 
io.on('connection', function(socket){ 
    
    socket.on('register', function(obj){ 
        try {
	        socket.partyId = obj.partyId; 
	        // simply replace the existing cache.
	        //if(!onlineUsers.hasOwnProperty(obj.partyId)) { 
            onlineUsers[obj.partyId] = {"obj": obj, "socket": socket};
            
			socket.emit('loginSuccess', obj); 
		    if (DEBUG) {
			  console.log(obj.partyId + " joined!");
			}
		} catch (e) {
		   console.error("registering error: " + e.stack || e);
		}
    }); 
     
    socket.on('unregister', function(){ 
        try {
	        if(onlineUsers.hasOwnProperty(socket.partyId)) { 
	            delete onlineUsers[socket.partyId]; 
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
		  
		  pool.query('SELECT * FROM WF_CHATHISTORY WHERE SESSIONID=? and SENTPARTYID=? and RECEIVEDPARTYID=? ORDER BY createdate ASC', [obj.sessionId, obj.fromPartyId, obj.toPartyId], function(err, results, fields) {
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
        } catch(e){
           console.error("query history error: " + e.stack || e);
        }
    }); 
    
    socket.on('nofityhistory', function(obj){ 
	    if(!onlineUsers.hasOwnProperty(socket.partyId)) { 
		   socket.emit('service_nopermission', obj); 
		   return;
		}
	    try {
		  pool.query('SELECT * FROM wf_notification WHERE partyid=? ORDER BY createdate ASC', [parseInt(obj.partyId)], function(err, results, fields) {
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
			    socket.emit('nofityFrom', results); 
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
	           var desc = "<div><span>"+obj.content+"</span><button onclick=\"javascript:defaultname.showHelp(defaultname.helpIcon,'"+obj.fromPartyId+"','"+obj.sessionId+"');\">\u9A6C\u4E0A\u8054\u7CFB</button></div>";
	           var msg = {"subject": "\u60A8\u6709\u65B0\u7684\u6D88\u606F!", "description": desc, "partyId": obj.toPartyId, "sessionid": obj.sessionId};
	           pool.query('INSERT INTO WF_NOTIFICATION SET ?', msg, function(err, result) {
				if (err) {
					if (err.code === 'PROTOCOL_CONNECTION_LOST') {
					  connectDB()
					} else {
					  console.error(err.stack || err);
					}
				} else {
					if (DEBUG) {
					  console.log("WF_NOTIFICATION inserted: " + result);
					}
				}
			   });
	           var msg = {"taskid":obj.taskId, "sentpartyid":obj.fromPartyId, "receivedpartyid":obj.toPartyId,
						"message":obj.content, "sessionid": obj.sessionId};
			   pool.query('INSERT INTO WF_CHATHISTORY SET ?', msg, function(err, result) {
			      if (err) {
		            console.error(err.stack || err);
		          } else {
					  if (DEBUG) {
						console.log("WF_CHATHISTORY inserted: " + result);
					  }
				  }
			   });
			   socket.emit('user_offline', obj); 
			   return;
			}
			var msg = {"taskid":obj.taskId, "sentpartyid":obj.fromPartyId, "receivedpartyid":obj.toPartyId,
						"message":obj.content, "sessionid": obj.sessionId};
		    pool.query('INSERT INTO WF_CHATHISTORY SET ?', msg, function(err, result) {
		       if (err) {
		          console.error(err.stack || err);
		       } else {
				   if (DEBUG) {
					  console.log("WF_CHATHISTORY inserted: " + result);
				   }
			   }
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
			pool.query('INSERT INTO WF_comment SET (?,?,?)', {A: 'test', B: 'test', }, function(err, result) {
			  // connected! (unless `err` is set)
				if (err) {
					if (err.code === 'PROTOCOL_CONNECTION_LOST') {
					  connectDB()
					} else {
					  console.error(err.stack || err);
					}
				}
				//todo:
				if (DEBUG) {
				  console.log(result);
				}
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
			pool.query('UPDATE FROM WF_comment SET (?,?,?)', {A: 'test', B: 'test', }, function(err, result) {
			  // connected! (unless `err` is set)
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
        } catch(e){
            console.error("hit_comment error: " + e.stack || e);
        }
    });
    
}); 

app.use('/', express.static(__dirname + '/www'));

var notifyHandler = function(req, res){ 
    try {
	    var d = req.parameter;
	    if (d.toAll) {
			io.emit('nofityFrom', d); 
			res.send('sent'); 
			return;
		} 
		
		if(!onlineUsers.hasOwnProperty(d.partyId)) { 
		   res.send('service_nopermission'); 
		   return;
		}
		onlineUsers[d.partyId].socket.emit('nofityFrom', d); 
	    res.send('sent'); 
    } catch(e){
        console.error("notifyHandler error: " + e.stack || e);
        res.send('error');
    }
};
app.get('/', function(req, res){
  res.send('Hi, what can we do for you?');
});
app.get('/uimaster/notify', notifyHandler); 
app.post('/uimaster/notify', notifyHandler); 

server.listen(8090, function(){ 
    console.log('listening on *:8090'); 
});
