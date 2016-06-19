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


// Crear Usuarios..
test('Create - User', t => {
	request(app)
	.post('/create/user')
	.field('name', 'Estudiante')
	.field('username', 'student')
	.field('email', 'estudent@gmail.com')
	.field('hashed_password', '124')
	.expect(200)
	.end(t.end());
});

test('Create - User', t => {
	request(app)
	.post('/create/user')
	.field('name', 'Profesor Machete')
	.field('username', 'profe')
	.field('email', 'profe@example.com')
	.field('hashed_password', '1234')
	.expect(200)
	.end(t.end());
});


// Crear Materias..
test('Create - Subjects', t => {
	request(app)
	.post('/create/subject')
	.field('name', 'Matemáticas 1')
	.field('description', 'La śe que coje a la mayoría de los nuevos')
	.field('level', '0')
	.expect(200)
	.end(t.end());
});


// Crear Secciones..
var professor1 = User.findOne({ username: 'profe' }); //buscas el profesor
var subject = Subject.findOne({ name: 'Matemáticas 1' }); //buscas la materia
test('Create - Sections', t => {
	request(app)
	.post('/create/section')
	.field('number', '1') 				//número de la sección
	.field('subject', 'Matemáticas 1')		//el ID de la materia
	.field('professor', 'profe')	//el ID del profesor
	.field('registered', '35')			//cantidad de registrados en la materia
	.expect(200)
	.end(t.end());
});


// Unir Estudiantes a una sección de una materia..
var student = User.findOne({ username: 'student' }); //buscas el estudiante
var subject = Subject.findOne({ name: 'Matemáticas 1' }); //buscas la materia
var section = Section.findOne({ subject: subject.id, number: 1 }); //buscas la seccion por la materia y el numero de seccion
test('Create - Sections', t => {
	request(app)
	.post('/create/subjectuser')
	.field('user', 'student') 				//el ID del estudiante
	.field('subject', 'Matemáticas 1')		//el ID de la sección
	.field('number', '1')
	.expect(200)
	.end(t.end());
});