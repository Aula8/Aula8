'use strict';

/**
 * Module dependencies.
 */

const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const SubjectUserSchema = new Schema({
  user: [{type: mongoose.Schema.Types.ObjectId, ref: 'User'}],
  subjects: [{type: mongoose.Schema.Types.ObjectId, ref: 'Subject'}],
});

module.exports = mongoose.model('SubjectUser', SubjectUserSchema);
