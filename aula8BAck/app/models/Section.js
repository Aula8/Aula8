'use strict';

/**
 * Module dependencies.
 */

const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const SectionSchema = new Schema({
  number: {type: Number},
  subject: {type: mongoose.Schema.Types.ObjectId, ref: 'Subject'},
  professor: {type: mongoose.Schema.Types.ObjectId, ref: 'User'},
  registered : {type: Number, default: 0}
});

module.exports = mongoose.model('Section', SectionSchema);