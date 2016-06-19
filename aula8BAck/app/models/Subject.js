'use strict';

/**
 * Module dependencies.
 */

const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const SubjectSchema = new Schema({
  name: { type: String, default: '' },
  description: { type: String, default: '' },
  level: { type: Number, default: 0 },
  url_files: { type: String, default: '' },
  create_at: { type: Date, default: Date.now },
});

module.exports = mongoose.model('Subject', SubjectSchema);

