'use strict';

/*
 * nodejs-express-mongoose-demo
 * Copyright(c) 2013 Madhusudhan Srinivasa <madhums8@gmail.com>
 * MIT Licensed
 */

/**
 * Module dependencies
 */

require('dotenv').config();

const fs = require('fs');
const join = require('path').join;
const express = require('express');
const mongoose = require('mongoose');
const passport = require('passport');
const config = require('./config');
const bodyParser = require('body-parser');
const cors = require('cors');
var mkdirp = require('mkdirp');
const path = require('path');
require('./app/models/User');
require('./app/models/Subject');
require('./app/models/Section');
require('./app/models/Session');
require('./app/models/SubjectUser');
require('./app/models/Question');

const app = express();
app.use(cors());

/**
 * Expose
 */

module.exports = app;
const port = process.env.PORT || 3000;

// Bootstrap routes
const io = require('socket.io').listen(app.listen(port));

console.log('Express app started on port ' + port);

//Agregue ESTO TAMBIEN #Mota
app.post('/upload', function(req, res) 
{
    console.log("\n\nBinary Upload Request from: " + req.ip);

    var filename = req.headers["file-name"];
    var room = req.headers["room"];
    console.log("Started binary upload of: " + filename);
    console.log(room, req.headers["room"], io.room);
        
    mkdirp(req.headers["file-folder"], function (err) 
    {
        if (err) 
        {
            console.log("Error ..");
        }    
        else 
        {   
            var filepath = path.resolve(req.headers["file-folder"], filename);
            var out = fs.createWriteStream(filepath, { flags: 'w', encoding: 'binary', fd: null, mode: '644' });
            req.pipe(out);
            req.on('end', function() 
            {
                console.log("Finished binary upload of: " + filename + "\n  in: " + filepath);
                res.sendStatus(200);
                if(room != null)
                {
                  console.log("enviando evento");
                  io.in(room).emit('descargar pdf', filepath);
                }
            });
        } 
    });
});

app.post('/uploadMaterial', function(req, res) 
{
    console.log("\n\nBinary Upload Request from: " + req.ip);

    var filename = req.headers["file-name"];
    var subject  = req.headers["subject"],
        section  = req.headers["section"],
        session  = req.headers["session"],
        folder   = req.headers["file-folder"];
    var dirPath  = folder + "/" + subject + "/" + section + "/" + session;
    console.log("Started binary upload of: " + filename);
        
    mkdirp(dirPath, function (err) 
    {
        if (err) 
        {
            console.log("Error ..");
        }    
        else 
        {   
            var filepath = path.resolve(dirPath, filename);
            var out = fs.createWriteStream(filepath, { flags: 'w', encoding: 'binary', fd: null, mode: '644' });
            req.pipe(out);
            req.on('end', function() 
            {
                console.log("Finished binary upload of: " + filename + "\n  in: " + filepath);
                res.sendStatus(200);
            });
        } 
    });
});
require('./config/express')(app);
require('./config/routes')(app, io);

connect()
  .on('error', console.log)
  .on('disconnected', connect)
  .once('open', listen);

function listen () {
  if (app.get('env') === 'filldb') return;
}

function connect () {
  var options = { server: { socketOptions: { keepAlive: 1 } } };
  return mongoose.connect(config.db, options).connection;
}


