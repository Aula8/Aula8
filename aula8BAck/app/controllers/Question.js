'use strict';
/*
	Module dependencies.
*/
var service = require('./services');
const mongoose = require('mongoose');
const { wrap: async } = require('co');
const Question = mongoose.model('Question');
const User = mongoose.model('User');
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
exports.create = function (req, socket){
	console.log(req, socket.username);
	User.findOne({name: socket.username}, function(err, user){
		const Q = new Question({
			user_question: user._id,
			question: req,
		});
		Q.save(function(err, question){
			if(!err){
				//return res.send({ status: 'OK', question: question});
				socket.in(socket.room).emit('nuevo mensaje', {
			        nombre_Usuario: socket.username,
			        mensaje: req,
			        id_question: question._id
			    });
			    socket.emit('nuevo mensaje', {
			        nombre_Usuario: socket.username,
			        mensaje: req,
			        id_question: question._id
			    });
			}
		});
	});
};


exports.update = function (socket, data){
	Question.update({id: data.id}, {$push: {responses: [data.response]}}, function(err){
                console.log("Successfully added");
                socket.in(socket.room).emit('nueva respuesta',{
                	response: data.response,
                	user_response: data.user_response,
                	id_question: data.id
                });
	});	
};

//Created by: Ricardo Vasquez 26073680