'use strict';
/*
	Module dependencies.
*/
var service = require('./services');
const mongoose = require('mongoose');
const { wrap: async } = require('co');
const Question = mongoose.model('Question');
/*
	Find Question
*/

exports.findQuestionByID = function (req, res) {
	Question.findOne(req.params.id, function(err, Question){
		if(!err){
			if(req.params.status=1){ //if the Question have an Answer
				return red.send({status: 'OK: QuestionID:', userQuestionID:_id,
					user_question:user_question , user_response: user_responses,
					Question:question, Answer:responses}); 
			}else //if the Question have not an Answer
			{
				return res.send({status: 'OK: QuestionID:', userQuestionID:_id,
					user_question:user_question, user_response: user_responses,
					Question:question, Status: 'Not Answer'}); 
			}
		}else
		{
			res.statusCode = 500
			console.log('Internal error(%d): %s',res.statusCode,err.message);
			return re.send({error: 'Server error'});
		}
	});	
};

/*
	Insert Question in the db
*/
exports.create = async(function* (req, res){
	const Question = new Question(req.body);
	Question.save(function(err, question){
		if(!err){
			console.log("Question created");
			return res.send({ status: 'OK', section:question});
		}else{
			console.log(err);
			if(err.name == 'ValidationError'){
					res.statusCode=400;
					res.send({error:'ValidationError'});
				}else{
				res.statusCode=500;
				res.send({error: 'Server error'});
				}
				console.log('Internal error(%d): %s',res.statusCode,err.message);
		}
	})
});

//Created by: Ricardo Vasquez 26073680