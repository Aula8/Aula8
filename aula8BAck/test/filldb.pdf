'use strict';

/**
 * Module dependencies.
 */

const mongoose = require('mongoose');
const test = require('tape');
const request = require('supertest');
const app = require('../server');
const { cleanup } = require('./helper');
const User = mongoose.model('User');
const Subject = mongoose.model('Subject');
const Section = mongoose.model('Section');
const Session = mongoose.model('Session');
const SubjectUser = mongoose.model('SubjectUser');
const Question = mongoose.model('Question');


//Crear Estudiantes
const students = [
	{namee: "Wuilkys Becerra", username: "wuilkysb", email: "wbecerra@gmail.com", hashed_password: '1234', type:"student"},
]

var professors = [
	{namee: "Jesus Rondon", username: "jrondon", email: "jrondon@gmail.com", hashed_password: '1234', type:"professor"},
	{namee: "Jannelys Bello", username: "jbello", email: "jbello@gmail.com", hashed_password: '1234', type:"professor"},
]

var subjects = [
	{namee: "Tecnicas de Programación 2", description: "Descripcion para Programación 2", level: "1",},
	{namee: "Ingeniería del Software 1", description: "Descripcion para Software", level: "5",},
]

var sessions = [
	{theme: "Introduccion a la POO", subject: "Tecnicas de Programación 2", status: "inactive",},
	{theme: "Herencias", subject: "Tecnicas de Programación 2", status: "active",},
	{theme: "DFD", subject: "Ingeniería del Software 1", status: "inactive",},
]

var sections = [
	{number: 2, subject: "Ingeniería del Software 1", professor: "jrondon", registered: "35"},
	{number: 3, subject: "Tecnicas de Programación 2", professor: "jbello", registered: "35"},
]

var subjectusers = [
	{user: "wuilkysb", subject: "Ingeniería del Software 1", number: "1"},
	{user: "wuilkysb", subject: "Tecnicas de Programación 2", number: "1"},
	{user: "jrondon", subject: "Ingeniería del Software 1", number: "1"},
	{user: "jbello", subject: "Tecnicas de Programación 2", number: "1"},
]

/*
test('Create - Estudents', t => {
	for (var i =0; i < students.length; i++){
		request(app)
		.post('/create/user')
		.field('name', students[i].namee)
		.field('username', students[i].username)
		.field('email', students[i].email)
		.field('hashed_password', students[i].hashed_password)
		.field('type', students[i].type)
		.expect(200)
		.end(t.end());
	}
});


// Crear profesores
test('Create - Professors', t => {
	for (var i =0; i < professors.length; i++){
		request(app)
		.post('/create/user')
		.field('name', professors[i].namee)
		.field('username', professors[i].username)
		.field('email', professors[i].email)
		.field('hashed_password', professors[i].hashed_password)
		.field('type', professors[i].type)
		.expect(200)
		.end(t.end());
	}
});*/

/*
//Crear materias
test('Create - Subjects', t => {
	for (var i =0; i < subjects.length; i++){
		request(app)
		.post('/create/subject')
		.field('name', subjects[i].namee)
		.field('description', subjects[i].description)
		.field('level', subjects[i].level) //nivel de la materia
		.expect(200)
		.end(t.end());
	}
});
*/


/*
// Crear secciones de la materia
test('Create - Sections', t => {
	for (var i =0; i < sections.length; i++){	
		request(app)
		.post('/create/section')
		.field('number', sections[i].number) 				//número de la sección
		.field('subject', sections[i].subject)		//el nombre de la materia que hayas creado arriba
		.field('professor', sections[i].professor)	//el username del profesor que hayas creado arriba
		.field('registered', sections[i].registered)			//cantidad de registrados en la materia
		.expect(200)
		.end(t.end());
	}
});
*/


//---> Descomenta luego de haber corrido todo lo anterior (comentas todo lo anterior)
/*test('Create - Sessions', t => {
	for (var i =0; i < sessions.length; i++){	
		request(app)
		.post('/create/session')
		.field('theme', sessions[i].theme) 				//el username del estudiante
		.field('subject', sessions[i].subject)		//el nombre de la sección
		.field('status', sessions[i].status)
		.expect(200)
		.end(t.end());
	}	
});*/



//---> Descomenta luego de haber corrido todo lo anterior (comentas todo lo anterior)
test('Create - SubjectUser', t => {
	for (var i =0; i < subjectusers.length; i++){	
		request(app)
		.post('/create/subjectuser')
		.field('user', subjectusers[i].user) 				//el username del estudiante
		.field('subject', subjectusers[i].subject)		//el nombre de la sección
		.field('number', subjectusers[i].number)
		.expect(200)
		.end(t.end());
	}	
});
