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
require('./app/models/User');
require('./app/models/Subject');
require('./app/models/Section');
require('./app/models/Session');
require('./app/models/SubjectUser');
require('./app/models/Question');

const app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));
app.use(cors());

/**
 * Expose
 */

module.exports = app;


// Bootstrap routes
require('./config/express')(app);
require('./config/routes')(app);

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


