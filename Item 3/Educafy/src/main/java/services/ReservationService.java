
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.ReservationRepository;
import domain.Reservation;

@Service
@Transactional
public class ReservationService {

	@Autowired
	private ReservationRepository	reservationRepository;


	public Collection<Reservation> findAllReservationByLesson(final int lessonId) {
		final Collection<Reservation> res = this.reservationRepository.findAllReservationByLesson(lessonId);
		return res;
	}

	public Collection<Reservation> findAllReservationByStudent(final int studentId) {
		final Collection<Reservation> res = this.reservationRepository.findAllReservationByStudent(studentId);
		return res;
	}
}
