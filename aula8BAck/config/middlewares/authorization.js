'use strict';

/*
 *  Generic require login routing middleware
 */

var jwt = require('jwt-simple');
var moment = require('moment');
var config = require('../config');

/*
 *  User authorization routing middleware
 */

exports.ensureAuthenticated = function(req, res, next) {
  if(!req.headers.token) {
    res
      .status(403)
      .send({message: "Tu petición no tiene cabecera de autorización"});
  }

  var token = req.headers.token;
  var payload = jwt.decode(token, config.TOKEN_SECRET);


  if(payload.exp <= moment().unix()) {
     res
        .status(401)
        .send({message: "El token ha expirado"});
  }

  req.user = payload.sub;
  next();
}
