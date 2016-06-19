'use strict';
/*
	Module dependencies.
*/
var service = require('./services');
const mongoose = require('mongoose');
const { wrap: async } = require('co');
const Subject = mongoose.model('Subject');

/*
	Find Subject
*/

exports.findSubjectByID = function (req, res) {
	Subject.findOne(req.params.id, function(err, Subject){
		if(!err){
			return res.send({status: 'OK', name:name, Subject:_id});
		}else
		{
			res.statusCode = 500
			console.log('Internal error(%d): %s',res.statusCode,err.message);
			return res.send({error: 'Server error'});
		}
	});	
};

exports.findSubjectByName = function (req, res) {
	Subject.findOne({name: req.params.name}, function(err, Subject){
		if(!name)
		{
			res.statusCode = 404;
			return res.send({error: 'Not found'});
		}
		if(!err){
			return res.send({status: 'OK', name:name, Subject:_id});
		}else
		{
			res.statusCode = 500
			console.log('Internal error(%d): %s',res.statusCode,err.message);
			return res.send({error: 'Server error'});
		}
	});	
};

/*
	Insert subject in the db
*/
exports.create = async(function* (req, res){

	const subject = new Subject(req.body);
	subject.save(function(err, subject){
		if(!err){
			console.log("Subject created");
			return res.send({ status: 'OK', subject:subject});
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