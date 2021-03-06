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

const mongoose = require('mongoose');
const Session = mongoose.model('Session');
const Subject = mongoose.model('Subject');

var mkdirp = require('mkdirp');
var fs = require('fs');
var dir = require('node-dir');
const path = require('path');
const asyncc = require('async');

var numUsuarios = 0;
var ROOMS = {};
var CLIENTS = {};

for (var i = 0; i < 3; i++)
{
  ROOMS["room"+i] = "room" + i;
}
ROOMS["room"+3] = "room" + 3;

/**
 * Expose routes
 */

module.exports = function (app, io, passport) 
{

  // user routes
  app.post('/create/user', users.create);
  app.post('/create/section', sections.create);
  app.post('/sessions/create', sessions.create);
  app.post('/create/subject', subjects.create);
  app.post('/create/subjectuser', subjectusers.create);
  app.post('/create/question', question.create);
  app.post('/auth/login', users.login);
  app.post('/users/subjects', users.Subjects);
  
  app.post('/', function(req, res){ res.send({success: "Ok"})});
  app.get('/private', auth.ensureAuthenticated);
  //app.get('/users/subject', users.subject);
  app.get('/users/:username', users.findUser)
  app.get('/sessions/:subject', sessions.findSessionBySubject);

  //Agregue ESTO #Mota 
  app.get('/:file(*)', function(req, res, next)
  {
    var file = req.params.file
    , path = file;
     console.log("enviando archivo a cliente");
     res.download(path);
  });



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
          socket.room = room.split("\"")[1];
        }
        socket.join (room);
      }

    );

    socket.on('send_audio', function(data)
    {
        socket.in(socket.room).emit('get_audio', data);
        console.log('recibiendo audio');
    });

    socket.on('nuevo mensaje', function (data) {
      question.create(data, socket);
      console.log("nuevo mensaje de ", socket.username, data, "en la sala ", socket.room);
      /*socket.in(socket.room).emit('nuevo mensaje', {
        nombre_Usuario: socket.username,
        mensaje: data
      });
      socket.emit('nuevo mensaje', {
        nombre_Usuario: socket.username,
        mensaje: data
      });*/
    });

    socket.on('nueva respuesta', function (data) {
      console.log("Nueva respuesta.. ", data);
      question.update(socket, data);
    });


    socket.on('agregar usuario', function (nombre_Usuario) {
      if (usuarioAñadido) return;


      socket.username = nombre_Usuario;
      ++numUsuarios;
      usuarioAñadido = true;
      
      /*socket.emit('iniciar sesion', {
        numUsuarios: numUsuarios
      });

      socket.in(socket.room).to(socket.room).emit('usuario unido', {
        nombre_Usuario: socket.username,
        numUsuarios: numUsuarios
      });*/

      console.log('Alguien se conectó con Aula 8', socket.request.connection._peername);
    });


    socket.on('escribiendo', function ()
    {
      console.log("Usuario Escribiendo");
      socket.in(socket.room).emit('escribiendo', {
        nombre_Usuario: socket.username
      });
    });


    socket.on('no escribiendo', function () {
      socket.in(socket.room).to(socket.room).emit('no escribiendo', {
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
        console.log("pintando en la sala : ", socket.room);
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

        socket.in(socket.room).to(socket.room).emit('usuario desconectado', {
          nombre_Usuario: socket.username,
          numUsuarios: numUsuarios
        });
      }
    });

    socket.on("pagina", function (data) 
    {
        socket.in(socket.room).emit("pagina",data);
        console.log("Cambiando de Pagina");
    });
    
    socket.on("zoom_in", function () 
    {
        socket.in(socket.room).emit("zoom_in");
        console.log("Zoom++");
    });

    socket.on("zoom_out", function () 
    {
        socket.in(socket.room).emit("zoom_out");
        console.log("Zoom--");
    });


    socket.on("rotar", function () 
    {
        socket.in(socket.room).emit("rotar");
        console.log("rotar!");
    });

    socket.on("enviar archivo", function(data)
    {
        base64_decode(data.data,data.nombre,data.direccion);
    });

    socket.on("closeSession", function(data){
      console.log("Cerrando... ", data);
      sessions.closed(data);
      socket.in(socket.room).emit("closedSession");
    });

    socket.on("getFilesSubject", function(data){
      asyncc.parallel({
          subject : function (cb){ Subject.findOne({name: data.subject}).exec(cb); },
          sess : function (cb){ Session.findOne({theme: socket.room}).exec(cb); },
      }, function(err, result){
          var dir = "FILES/" + result.subject._id + "/" + data.section + "/" + result.sess._id;
          var results = walk(dir);
          dir = path.resolve(dir);
          dir = dir.split("/").slice(-3, dir.length).join("/") + "/" + result.sess._id;
          console.log("ResolverPatch -->>", result.sess, socket.room);
          socket.emit("getFilesSubject", {result : results, link: dir});
      });
    });



  });

var walk = function(dir) {
    var results = []
    try{
      var list = fs.readdirSync(dir);
      list.forEach(function(file) {
          file = dir + '/' + file
          var stat = fs.statSync(file)
          if (stat && stat.isDirectory()) results = results.concat(walk(file))
          else results.push(file)
      })
      return results;
    } catch (e) {
      return "No se encontraron archivos";
    }
}

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
            fs.writeFileSync(direccion+name.trim(), bitmap);
        } 
    });

    //
    console.log('******** File created from base64 encoded string ********');
}
};