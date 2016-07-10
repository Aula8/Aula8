'use strict';

/*
 * Module dependencies.
 */

// Note: We can require users, articles and other cotrollers because we have
// set the NODE_PATH to be ./app/controllers (package.json # scripts # start)

const users = require('../app/controllers/users');
const sections = require('../app/controllers/Section');
const sessions = require('../app/controllers/Session');
const subjects = require('../app/controllers/Subject');
const subjectusers = require('../app/controllers/SubjectUser');
const question = require('../app/controllers/Question');
const auth = require('./middlewares/authorization');
const port = process.env.PORT || 3000;

const mongoose = require('mongoose');
const Session = mongoose.model('Session');
const Subject = mongoose.model('Subject');

const path = require('path');

var mkdirp = require('mkdirp');
var fs = require('fs');
var numUsuarios = 0;
var ROOM;
var CLIENTS = {};


/**
 * Expose routes
 */

module.exports = function (app, passport) {

  // user routes
  const io = require('socket.io').listen(app.listen(port));

  app.get('/check_connection', function(req, res){
    res.statusCode=200;
    return res.send();
  });

  app.post('/create/user', users.create);
  app.post('/create/section', sections.create);
  app.post('/sessions/create', sessions.create);
  app.post('/create/subject', subjects.create);
  app.post('/create/subjectuser', subjectusers.create);
  app.post('/auth/login', users.login);
  app.post('/users/subjects', users.Subjects);
  
  app.post('/', function(req, res){ res.send({success: "Ok"})});
  app.get('/private', auth.ensureAuthenticated);
  //app.get('/users/subject', users.subject);
  app.get('/users/:username', users.findUser)
  app.get('/sessions/:subject', sessions.findSessionBySubject);

  console.log('Express app started on port ' + port);


  io.on('connection', function(socket) {  
    console.log('Un cliente se ha conectado');

    socket.on('credentials', function(data) {  
        console.log(data);
    });

    var usuarioAñadido = false;

    socket.on('room', function (room)
      {
        if(String(room.split("\"")[1]) === "undefined")
        {
          console.log("user conectado en la sala ", room );
          socket.room = room;
        }else{
          console.log("user conectado en la sala ", room.split("\"")[1] );

        }
        socket.join (room);
      });

    socket.on('send_audio', function(data)
    {
        socket.in(socket.room).emit('get_audio', data);
        console.log('recibiendo audio');
    });

    socket.on('nuevo mensaje', function (data) {
      console.log("nuevo mensaje de ", socket.username, data, "en la sala ", socket.room);
      socket.in(socket.room).emit('nuevo mensaje', {
        nombre_Usuario: socket.username,
        mensaje: data
      });
    });


    socket.on('agregar usuario', function (nombre_Usuario) {
      if (usuarioAñadido) return;


      socket.username = nombre_Usuario;
      ++numUsuarios;
      usuarioAñadido = true;
      socket.to(data.room).emit('iniciar sesion', {
        numUsuarios: numUsuarios
      });

      socket.in(socket.room).emit('usuario unido', {
        nombre_Usuario: socket.username,
        numUsuarios: numUsuarios
      });

      console.log('Alguien se conectó con Aula 8', socket.request.connection._peername);
    });


    socket.on('escribiendo', function ()
    {
      socket.in(socket.room).emit('escribiendo', {
        nombre_Usuario: socket.username
      });
    });


    socket.on('no escribiendo', function () {
      socket.in(socket.room).emit('no escribiendo', {
        nombre_Usuario: socket.username
      });
    });

    socket.on('enviar imagen', function (data) {
      socket.in(socket.room).emit('enviar imagen', {
        nombre_Usuario: socket.username,
        img_Codificada: data
      });
    });

    socket.on("pintar", function(data)
    {
        socket.in(socket.room).emit("pintar",data);
        console.log("pintando en ", socket.room);
    });

    socket.on("borrar todo", function()
    {
        socket.in(socket.room).emit("borrar todo");
        console.log("Borrar todo");
    });


    socket.on('disconnect', function () {
      if (usuarioAñadido) {
        --numUsuarios;

        console.log('Alguien se desconectó de Aula 8', socket.request.connection._peername);

        socket.in(socket.room).emit('usuario desconectado', {
          nombre_Usuario: socket.username,
          numUsuarios: numUsuarios
        });
      }
    });

    socket.on('getSessions', function (data) {
          Subject.findOne({name: data}, function(error, subject){
              Session.findOne({subject: subject.id}, function(err, session){
                  if(!session)
                  {
                    res.statusCode = 404;
                    socket.emit("setSessions", {error: 'Not found'});
                  }
                  if(session){
                    console.log("Session Enviada");
                    socket.emit("setSessions", {session: session});
                  }else
                  {
                    res.statusCode = 500
                    console.log('Internal error(%d): %s',res.statusCode,err.message);
                    socket.emit("setSessions", {error: 'Server error'});
                  }
              }); 
          });
    });

    socket.on("enviar archivo", function(data)
    {
        base64_decode(data.data,data.nombre,data.direccion);
    });

    socket.on("bajar archivo", function(data)
    {
      console.log("recibido");
        fs.exists(data, function(exists) 
        {
            console.log("exists?");
            if (exists) 
            {   
              console.log("exists true");
                fs.readFile(data, function (err, file) 
                {
                  if (err) 
                  {
                    socket.emit("error 404");
                  }
                  else
                  {
                    
                    console.log(path.basename(data));
                    socket.emit("archivo enviado", 
                    {
                        archivo: file.toString('base64'),
                        nombre: path.basename(data)
                    });
                  }

                });
            }
            else
                socket.emit("error 404");
            
        });
    });

  });


  function base64_decode(file,name,direccion) 
  {
      // create buffer object from base64 encoded string, it is important to tell the constructor that the string is base64 encoded
      
      mkdirp(direccion, function (err) 
      {
          if (err) 
          {
              console.log("NWBNA DE ERROR ..");
          }    
          else 
          {   
              var bitmap = new Buffer(file, 'base64');
              fs.writeFileSync(direccion+name, bitmap);
          } 
      });

      //
      console.log('******** File created from base64 encoded string ********');
  }

};
