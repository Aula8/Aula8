'use strict';
/*
	Module dependencies.
*/
var service = require('./services');
const mongoose = require('mongoose');
const { wrap: async } = require('co');
const Session = mongoose.model('Session');
const Subject = mongoose.model('Subject');

/*
	Find Session
*/

exports.findSessionByID = function (req, res) {
	Session.findOne(req.params.id, function(err, Session){
		if(!err){
			return res.send({status: 'OK ', theme:theme,
				create_at:create_at, Session:_id});
		}else
		{
			res.statusCode = 500
			console.log('Internal error(%d): %s',res.statusCode,err.message);
			return res.send({error: 'Server error'});
		}
	});	
};

exports.findSessionByTheme = async(function* (req, res) {
	Session.findOne({theme: req.params.theme}, function(err, session){
		if(!session)
		{
			res.statusCode = 404;
			return res.send({error: 'Not found'});
		}
		if(session){
			return res.send({session: session});
		}else
		{
			res.statusCode = 500
			console.log('Internal error(%d): %s',res.statusCode,err.message);
			return res.send({error: 'Server error'});
		}
	});	
});

exports.findSessionBySubject = function (req, res) {
	Subject.findOne({name: req.params.subject}, function(error, subject){
		Session.find({subject: subject.id}).sort({created_at: -1}).exec(function(err, session){
			if(!session)
			{
				res.statusCode = 404;
				return res.send({error: 'Not found'});
			}
			if(session){
				console.log(session);
				return res.send(session);
			}else
			{
				res.statusCode = 500
				console.log('Internal error(%d): %s',res.statusCode,err.message);
				return res.send({error: 'Server error'});
			}
		});	
	});
};

exports.findSessionByDate = function (req, res) {
	Session.findOne({create_at: req.params.create_at}, function(err, Session){
		if(!create_at)
		{
			res.statusCode = 404;
			return res.send({error: 'Not found'});
		}
		if(!err){
			return res.send({status: 'OK ', theme:theme,
				create_at:create_at, Session:_id});
		}else
		{
			res.statusCode = 500
			cconsole.log('Internal error(%d): %s',res.statusCode,err.message);
			return res.send({error: 'Server error'});
		}
	});	
};

/*
	Insert ssesion in the db
*/
exports.create = async(function* (req, res){
	//const Session= new Session(req.body);
	console.log(req.body.subject, req.body.session);
	Subject.findOne({name: req.body.subject}, function(error, subject){
		const session = new Session({
			theme: req.body.session,
			subject: subject.id,
		});

		session.save(function(err, session){
			if(!err){
				console.log("Session created");
				return res.send({ status: 'OK', session:session});
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
		});
	});

});

exports.closed = function(data){
	Subject.findOne({name: data.subject}, function(error, subject){
		console.log("Buscando Cerrar-->>", subject._id);
		console.log(subject._id, data.session);
		Session.update({theme: data.session, subject: subject._id}, {$set: {status: "inactive"}}, function(err,s){
			console.log(s.status);
		});
	});
}

//Created by: Ricardo Vasquez 26073680