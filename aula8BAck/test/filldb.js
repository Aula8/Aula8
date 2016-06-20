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

/* Primero Crear los Usuarios y las Materias */
// Crear Usuarios..
test('Create - User', t => {
	request(app)
	.post('/create/user')
	.field('name', 'Estudiante')
	.field('username', 'estudiante')
	.field('email', 'estudent@gmail.com')
	.field('hashed_password', '124')
	.field('type', 'student')  //tipo de usuario (student o professor)
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
	.field('type',  'professor')
	.expect(200)
	.end(t.end());
});



// Crear Materias..
test('Create - Subjects', t => {
	request(app)
	.post('/create/subject')
	.field('name', 'Matemáticas 1')
	.field('description', 'La śe que coje a la mayoría de los nuevos')
	.field('level', '0') //nivel de la materia
	.expect(200)
	.end(t.end());
});



/*

//--->> descomenta luego de haber corrido los usuarios y materias (comentas las lineas de usuarios y materias)
// Crear Secciones..
test('Create - Sections', t => {
	request(app)
	.post('/create/section')
	.field('number', '1') 				//número de la sección
	.field('subject', 'Matemáticas 1')		//el nombre de la materia que hayas creado arriba
	.field('professor', 'profe')	//el username del profesor que hayas creado arriba
	.field('registered', '35')			//cantidad de registrados en la materia
	.expect(200)
	.end(t.end());
});*/





/*

//---> Descomenta luego de haber corrido todo lo anterior (comentas todo lo anterior)
// Unir Estudiantes a una sección de una materia..
test('Create - SubjectUser', t => {
	request(app)
	.post('/create/subjectuser')
	.field('user', 'student') 				//el username del estudiante
	.field('subject', 'Matemáticas 1')		//el nombre de la sección
	.field('number', '1')
	.expect(200)
	.end(t.end());
});
*/