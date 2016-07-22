'use strict';

/**
 * Module dependencies.
 */

const mongoose = require('mongoose');
const timestamps = require ("mongoose-times");
const Schema = mongoose.Schema;

const SessionSchema = new Schema({
  theme: { type: String, default: '' },
  questions: { type: String, default: '' },
  status: { type: String, default: 'active' },
  files_folder: { type: String, default: '' },
  boadr: { type: String, default: '' },
  subject: {type: mongoose.Schema.Types.ObjectId, ref: 'Subject'},
  users: [{type: mongoose.Schema.Types.ObjectId, ref: 'User'}],
});

SessionSchema.plugin(timestamps, { created: "created_at", lastUpdated: "updated_at" });
module.exports = mongoose.model('Session', SessionSchema);