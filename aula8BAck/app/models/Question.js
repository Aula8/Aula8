'use strict';

/**
 * Module dependencies.
 */

const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const QuestionSchema = new Schema({
  user_question: {type: mongoose.Schema.Types.ObjectId, ref: 'User'},
  user_responses: [{type: mongoose.Schema.Types.ObjectId, ref: 'User'}],
  question: { type: String, default: '' },
  responses: [{ type: String, default: '' }],
  status: { type: Boolean },
});

module.exports = mongoose.model('Question', QuestionSchema);