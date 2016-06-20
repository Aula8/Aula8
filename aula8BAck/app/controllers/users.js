'use strict';

/**
 * Module dependencies.
 */

var service = require('./services');
const mongoose = require('mongoose');
const { wrap: async } = require('co');
const User = mongoose.model('User');


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


exports.login = function (req, res) {
  console.log("login", req.body);
  User.findOne({username: req.body.username.toLowerCase()}, function(err, user) {
    if(err)
      res.send({error: true, message: "Error: "})
    else if (!user)
      res.send({error: true, message: "Usuario no encontrado"})
    else if (user){
      if (user.hashed_password != req.body.hashed_password)
        res.send({error: true, message: "Contraseña incorrecta"})
      else {
        return res
            .status(200)
            .send(user);
      }
    }
  });
};

exports.subject = function(req, res){
  console.log(req.body);
}
