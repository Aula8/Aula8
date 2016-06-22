'use strict';

/**
 * Module dependencies.
 */

const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const SubjectUserSchema = new Schema({
  user: {type: mongoose.Schema.Types.ObjectId, ref: 'User'},
  subject: {type: mongoose.Schema.Types.ObjectId, ref: 'Subject'},
  section: {type: String},
});

module.exports = mongoose.model('SubjectUser', SubjectUserSchema);
