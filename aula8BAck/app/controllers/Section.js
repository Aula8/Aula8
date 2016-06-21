'use strict';
/*
	Module dependencies.
*/
var service = require('./services');
const mongoose = require('mongoose');
const { wrap: async } = require('co');
const Section = mongoose.model('Section');
const User = mongoose.model('User');
const Subject = mongoose.model('Subject');
/*
	Find Section
*/

exports.findSectionByID = function (req, res) {
	Section.findOne(req.params.id, function(err, Section){
		if(!err){
			return res.send({status: 'OK: SectionID:', Section:_id,
				subject:subject, professor: professor});
		}else
		{
			res.statusCode = 500
			console.log('Internal error(%d): %s',res.statusCode,err.message);
			return res.send({error: 'Server error'});
		}
	});	
};

exports.findSectionByProfessor = function (req, res) {
	Section.findOne({professor: req.params.professor}, function(err, Section){
		if(!professor)
		{
			res.statusCode = 404;
			return res.send({error: 'Not found'});
		}
		if(!err){
			return res.send({status: 'OK: SectionID:', Section:_id,
				subject:subject, professor: professor});
		}else
		{
			res.statusCode = 500
			console.log('Internal error(%d): %s',res.statusCode,err.message);
			return res.send({error: 'Server error'});
		}
	});	
};


/*
	Insert section in the db
*/
exports.create = async(function* (req, res){
	console.log("professor ", req.body.professor);
	User.findOne({username: req.body.professor}, function(err, user){
		Subject.findOne({ name: req.body.subject }, function(err, subject){
			console.log(req.body.subject, subject);
			const section = new Section({
				number: req.params.number,
				subject: subject.id,
				professor: user.id,
				registered: req.params.registered,
			});
			section.save(function(err, section){
				if(!err){
					console.log("Section created");
					res.send({ status: 'OK', section:section});
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
});
//Created by: Ricardo Vasquez 26073680