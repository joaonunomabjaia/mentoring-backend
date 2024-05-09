package mz.org.fgh.mentoring.repository.question;

import io.micronaut.data.annotation.Repository;
import mz.org.fgh.mentoring.base.AbstaractBaseRepository;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.entity.question.QuestionCategory;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Jose Julai Ritsure
 */
@Repository
public abstract class AbstractQuestionRepository extends AbstaractBaseRepository implements QuestionRepository {

    private final SessionFactory session;

    protected AbstractQuestionRepository(SessionFactory session) {
        this.session = session;
    }

    @Transactional
    @Override
    public List<Question> search(final String code, final String description, final QuestionCategory questionsCategory) {

        String sql = "SELECT DISTINCT(q.id) FROM questions q " +
                    " INNER JOIN question_categories qc ON q.QUESTION_CATEGORY_ID = qc.ID " +
                    "WHERE 1=1 AND q.LIFE_CYCLE_STATUS = 'ACTIVE' ";


        if (code != null) {
            sql += " AND q.code like '%" + code + "%' ";
        }
        if (description != null) {
            sql += " AND q.question like '%" + description + "%' ";
        }
        if (questionsCategory != null) {
            sql += " AND qc.id ="  + questionsCategory.getId() ;
        }

        Query qw = this.session.getCurrentSession().createSQLQuery(sql);
        List<Long> ids = qw.getResultList();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Question> criteria = builder.createQuery(Question.class);
        Root<Question> root = criteria.from(Question.class);
        criteria.select(root).where(root.get("id").in(ids));
        Query q = this.session.getCurrentSession().createQuery(criteria);
        List<Question> questions = q.getResultList();

        return questions;
    }
}
