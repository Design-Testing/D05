
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SubjectRepository;
import security.Authority;
import domain.Actor;
import domain.Lesson;
import domain.Subject;

@Service
@Transactional
public class SubjectService {

	@Autowired
	private SubjectRepository	subjectRepository;

	@Autowired
	private LessonService		lessonService;

	@Autowired
	private ActorService		actorService;


	public Subject create() {
		return new Subject();
	}

	public Subject findOne(final int subjectId) {
		Assert.isTrue(subjectId != 0);
		final Subject subject = this.subjectRepository.findOne(subjectId);
		Assert.notNull(subject);
		return subject;
	}

	public Collection<Subject> findAll() {
		Collection<Subject> subjects = new ArrayList<>();
		subjects = this.subjectRepository.findAll();
		Assert.notNull(subjects);
		return subjects;
	}

	public Subject save(final Subject subject) {
		Assert.notNull(subject);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, Authority.ADMIN));
		final Subject result;
		result = this.subjectRepository.save(subject);
		return result;
	}
	public void delete(final Subject subject) {
		Assert.notNull(subject);
		Assert.isTrue(subject.getId() != 0);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(this.actorService.checkAuthority(principal, Authority.ADMIN));
		final Subject result = this.findOne(subject.getId());
		final Collection<Lesson> lessons = this.lessonService.findAllBySubject(subject.getId());
		Assert.isTrue(lessons.isEmpty(), "No puedes eliminar una asignatura que tenga una clase asginada.");
		this.subjectRepository.delete(result);
	}

	/**
	 * Incluye una cadena vacia en la primera posición. Está ideado para mostrar las opciones de clasificación poosibles en el buscardor o finder
	 * */
	public Collection<String> subjectLevels() {
		final Collection<String> res = new ArrayList<>();
		res.add("");
		res.addAll(this.subjectRepository.subjectLevels());
		Assert.notNull(res);
		return res;
	}

	/**
	 * Incluye una cadena vacia en la primera posición. Está ideado para mostrar las opciones de clasificación poosibles en el buscardor o finder
	 * */
	public Collection<String> subjectNamesEs() {
		final Collection<String> res = new ArrayList<>();
		res.add("");
		res.addAll(this.subjectRepository.subjectNamesEs());
		Assert.notNull(res);
		return res;
	}

	/**
	 * Incluye una cadena vacia en la primera posición. Está ideado para mostrar las opciones de clasificación poosibles en el buscardor o finder
	 * */
	public Collection<String> subjectNamesEn() {
		final Collection<String> res = new ArrayList<>();
		res.add("");
		res.addAll(this.subjectRepository.subjectNamesEn());
		Assert.notNull(res);
		return res;
	}
}
