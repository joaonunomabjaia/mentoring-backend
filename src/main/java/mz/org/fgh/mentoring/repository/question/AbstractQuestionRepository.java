package mz.org.fgh.mentoring.repository.question;

import io.micronaut.data.annotation.Repository;
import mz.org.fgh.mentoring.base.AbstaractBaseRepository;
import mz.org.fgh.mentoring.entity.question.QuestionsCategory;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
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
    public List<Long> search(final String code, final String description, final QuestionsCategory questionsCategory) {

        String sql = "SELECT DISTINCT(q.id) FROM questions q " +
                " INNER JOIN question_categories qc ON q.QUESTION_CATEGORY_ID = qc.ID ";

        if(code != null || description != null || questionsCategory != null) {
            sql += " WHERE 1=1 ";
        }
        if (code != null) {
            sql += " AND q.code like '%" + code + "%' ";
        }
        if (description != null) {
            sql += " AND q.question like '%" + description + "%' ";
        }
        if (questionsCategory != null) {
            sql += " AND qc.id like '%" + questionsCategory.getId() + "%' ";
        }

        Query qw = this.session.getCurrentSession().createSQLQuery(sql);
        List<Long> ids = qw.getResultList();
        return ids;
    }
}
