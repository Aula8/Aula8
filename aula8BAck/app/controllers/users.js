'use strict';

/**
 * Module dependencies.
 */

var service = require('./services');
const mongoose = require('mongoose');
const { wrap: async } = require('co');
const User = mongoose.model('User');
const Subject = mongoose.model('Subject');
const SubjectUser = mongoose.model('SubjectUser');
const Section = mongoose.model('Section');
const Session = mongoose.model('Session');
const asyncc = require('async');


/**
 * Load
 */

exports.load = async(function* (req, res, next, username) {
  const criteria = { username };
  try {
    req.profile = yield User.load({ criteria });
    if (!req.profile) return res.send(new Error('Usuario encontrado'));
  } catch (err) {
    return res.send(err);
  }
  next();
});

/**
 * Create user
 */


exports.create = async(function* (req, res) {
  var user = new User(req.body);
  user.save(function(err) {
      res.statusCode = 200;
      res.send({ status: '"User created"' });
  });
});


exports.findUser = function (req, res) {
  User.findOne({ username: req.params.username }, function(err, username) {
      if(!username) {
        res.statusCode = 404;
        return res.send({ error: 'Not found' });
      }
      if(!err) {
        // Send { status:OK, tshirt { tshirt values }}
        return res.send({ status: 'OK', user:username });
        // Send {tshirt values}
        // return res.send(tshirt);
      } else {
        res.statusCode = 500;
        console.log('Internal error(%d): %s',res.statusCode,err.message);
        return res.send({ error: 'Server error' });
      }
  });
};


var filterValuePart = function(arr, part) {
    return arr.filter(function(obj) {
        return Object.keys(obj)
                     .some(function(k) { 
                               return obj[k].indexOf(part) !== -1; 
                           });
    });
};


exports.login = async(function* (req, res) {
  User.findOne({username: req.body.username.toLowerCase()}, function(err, user) {
    if(err)
      res.send({error: true, message: "Error: "})
    else if (!user)
      res.send({error: true, message: "Usuario no encontrado"})
    else if (user){
      if (user.hashed_password != req.body.hashed_password)
        res.send({error: true, message: "Contraseña incorrecta"})
      else {
        var student = [];
        var subjectaux = [];
        var subjects = [];
        var sections = [];
        var sessions = [];
        var professors = [];

        asyncc.parallel({
            subjectUser : function (cb){ SubjectUser.find({user: user.id}).exec(cb); },
            subject : function (cb){ Subject.find({}).exec(cb); },
            section : function (cb){ Section.find({}).exec(cb); },
            professor : function (cb){ User.find({}).exec(cb); },
            session : function (cb){ Session.find({}).exec(cb); },
        }, function(err, result){

            for(var i=0; i < result.subjectUser.length; i++){
              sections.push( result.section.filter( function(sect){
                return String(sect.subject) === String(result.subjectUser[i].subject);
              }));
              subjects.push( result.subject.filter(function(sub){
                return String(sub._id) === String(result.subjectUser[i].subject)
              }));
            }
            subjects = [].concat.apply([], subjects)

            for(var i=0 ; i<subjects.length; i++){
                sessions.push( result.session.filter(function(sess){
                    return String(sess.subject) === String(result.subject[i]._id);
                }));
            }
            sessions = [].concat.apply([], sessions)

            sections = [].concat.apply([], sections)
            for(var i=0 ; i<sections.length; i++){
                professors.push( result.professor.filter(function(prof){
                    return String(prof._id) === String(result.section[i].professor);
                }));
            }
            professors = [].concat.apply([], professors)


            return res
                  .status(200)
                  .send({user:user, subject:subjects, professor:professors, section:sections, session:sessions, token: service.createToken(user)});
        });

      }
    }
  });
});

exports.Subjects = function(req, res){
  console.log(req.body);
  User.findOne({username: req.body.username.toLowerCase()}, function(err, user) {
    if(err)
      res.send({error: true, message: "Error: "})
    else if (!user)
      res.send({error: true, message: "Usuario no encontrado"})
    else if (user){
      SubjectUser.findOne({user: user.id}, function(err, subjectUser){
          console.log("[Subject] --> ", subjectUser);
      });
    }
  });
}
