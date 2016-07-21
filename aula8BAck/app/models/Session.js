'use strict';

/**
 * Module dependencies.
 */

const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const SessionSchema = new Schema({
  theme: { type: String, default: '' },
  questions: { type: String, default: '' },
  status: { type: String, default: 'active' },
  files_folder: { type: String, default: '' },
  boadr: { type: String, default: '' },
  create_at: { type: Date, default: Date.now },
  subject: {type: mongoose.Schema.Types.ObjectId, ref: 'Subject'},
  users: [{type: mongoose.Schema.Types.ObjectId, ref: 'User'}],
});

module.exports = mongoose.model('Session', SessionSchema);