'use strict';
/*
	Module dependencies.
*/
var service = require('./services');
const mongoose = require('mongoose');
const { wrap: async } = require('co');
const SubjectUser = mongoose.model('SubjectUser');
/*
	Find SubjectUser
*/

exports.findSubjectUserByID = function (req, res) {
	SubjectUser.findOne(req.params.id, function(err, SubjectUser){
		if(!err){
			return res.send({status: 'OK: SubjectUserID:', SubjectUser:_id,
				subject:subject , user: user});
		}else
		{
			res.statusCode = 500
			cosole.log('Internal error : ', res.statucCode,err,message);
			return res.send({error: 'Server error'});
		}
	});	
};

exports.findSectionByUser = function (req, res) {
	SubjectUser.findOne({user: req.params.user}, function(err, SubjectUser){
		if(!user)
		{
			res.statusCode = 404;
			return res.send({error: 'Not found'});
		}
		if(!err){
			return res.send({status: 'OK: SubjectUserID:', SubjectUser:_id,
				subject:subject, user: user});
		}else
		{
			res.statusCode = 500
			console.log('Internal error(%d): %s',res.statusCode,err.message);
			return re.send({error: 'Server error'});
		}
	});	
};


/*
	Insert section in the db
*/
exports.create = async(function* (req, res){
	var student = User.findOne({ username: req.params.user });
	var subject = Subject.findOne({ name: req.params.subject });
	var section = Section.findOne({ subject: subject.id, number: int(req.params.number) });
	const subjectUser= new SubjectUser({
		user: student.id,
		subject: subject.id,
	});
	subjectUser.save(function(err, subjectuser){
		if(!err){
			console.log("Subjectuser created");
			return res.send({ status: 'OK', subjectuser:subjectuser});
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