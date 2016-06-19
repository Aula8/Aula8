'use strict';
/*
	Module dependencies.
*/
var service = require('./services');
const mongoose = require('mongoose');
const { wrap: async } = require('co');
const Session = mongoose.model('Session');

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

exports.findSessionByTheme = function (req, res) {
	Session.findOne({theme: req.params.theme}, function(err, Session){
		if(!theme)
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
			console.log('Internal error(%d): %s',res.statusCode,err.message);
			return res.send({error: 'Server error'});
		}
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
	const Session= new Session(req.body);
	Session.save(function(err, session){
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
	})
});

//Created by: Ricardo Vasquez 26073680