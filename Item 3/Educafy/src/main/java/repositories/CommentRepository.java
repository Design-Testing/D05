
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Query("select c from Comment c join c.assesment a join a.lesson l where l.teacher.userAccount.id=?1")
	Collection<Comment> findAllCommentsByTeacherPpal(int teacherUAId);

	@Query("select c from Comment c where c.assesment.id=?1")
	Collection<Comment> findAllCommentsByAssesment(int assesmentId);

}
