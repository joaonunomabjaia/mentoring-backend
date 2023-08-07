package mz.org.fgh.mentoring.repository.mentorship;

import io.micronaut.data.annotation.Repository;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.entity.mentorship.IterationType;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.PerformedSession;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public abstract class MentorshipRepositoryImpl implements MentorshipRepository{

    @Inject
    private EntityManager entityManager;

    @Override
    public List<Mentorship> findAll() {
        return null;
    }

    @Override
    public Optional<Mentorship> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<PerformedSession> getSelectedOfFilterPMQTRList(Date startDate, Date endDate) {
        String sql =    "SELECT d.DISTRICT AS 'districtName', " +
                "h.HEALTH_FACILITY AS 'healthFacility', " +
                "DATE_FORMAT(s.PERFORMED_DATE, '%d-%m-%Y') AS 'performedDate', " +
                "CONCAT(t.NAME, ' ', t.SURNAME) AS 'tutorName', " +
                "CONCAT(tr.NAME, ' ', tr.SURNAME) AS 'tutoredName', " +
                "c.NAME AS 'cabinet', " +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000751' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000751'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000752' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000752'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000753' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000753'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000754' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000754'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000755' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000755'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000756' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000756'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000757' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000757'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000758' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000758'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000759' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000759'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000760' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000760'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000761' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000761'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000762' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000762'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000763' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000763'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000764' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000764'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000765' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000765'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000766' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000766'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000767' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000767'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000768' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000768'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000769' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000769'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000770' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000770'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000771' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000771'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000772' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000772'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000773' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000773'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000774' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000774'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000775' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000775'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000776' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000776'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000777' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000777'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000778' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000778'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000779' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000779'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000780' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000780'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000781' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000781'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000782' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000782'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000783' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000783'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000784' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000784'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000785' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000785'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000786' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000786'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000787' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000787'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000788' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000788'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000789' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000789'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000790' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000790'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000791' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000791'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000792' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000792'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000793' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000793'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000794' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000794'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000795' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000795'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000796' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000796'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000797' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000797'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000798' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000798'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000799' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000799'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000800' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000800'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000801' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000801'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000802' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000802'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000803' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000803'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000804' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000804'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000805' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000805'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000806' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000806'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000807' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000807'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000808' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000808'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000809' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000809'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000810' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000810'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000811' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000811'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000812' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000812'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000813' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000813'," +
                "(SELECT TEXT_VALUE FROM answers a, questions q WHERE a.QUESTION_ID = q.ID AND q.CODE = 'MTQ00000814' AND MENTORSHIP_ID=m.ID) AS 'MTQ00000814', " +
                "DATE_FORMAT(s.CREATED_AT, '%d-%m-%Y %H:%i:%s') AS 'createdAt',	 " +
                "m.ID AS 'MENTORSHIP_ID'	 " +
                "FROM SESSIONS s    INNER JOIN mentorships m ON s.ID = m.SESSION_ID " +
                "                   INNER JOIN health_facilities h ON m.HEALTH_FACILITY_ID = h.ID " +
                "                   INNER JOIN cabinets c ON m.CABINET_ID = c.ID " +
                "                   INNER JOIN tutors t ON m.TUTOR_ID = t.ID " +
                "                   INNER JOIN tutoreds tr ON m.TUTORED_ID = tr.ID " +
                "                   INNER JOIN forms f ON m.FORM_ID = f.ID " +
                "                   INNER JOIN districts d ON h.DISTRICT_ID = d.ID " +
                "WHERE s.LIFE_CYCLE_STATUS = 'ACTIVE' " +
                "       AND m.LIFE_CYCLE_STATUS = 'ACTIVE' " +
                "       AND s.PERFORMED_DATE >= '"+ DateUtils.formatToYYYYMMDD(startDate) +"' " +
                "       AND s.PERFORMED_DATE <= '"+ DateUtils.formatToYYYYMMDD(endDate) +"' " +
                "       AND f.CODE = 'MT00000048' " +
                "ORDER BY 1, 2, 3 ASC";

        Query query = entityManager.createNativeQuery(sql);

        List<Object[]> performedSessionsHTS = query.getResultList();
        List<PerformedSession> performedSessions= new ArrayList<>();

        for (Object[] ps : performedSessionsHTS) {
            PerformedSession p= new PerformedSession(

                    ps[0].toString(),
                    ps[1].toString(),
                    ps[2].toString(),
                    ps[3].toString(),
                    ps[4].toString(),
                    ps[5].toString(),
                    ps[6].toString(),
                    ps[7].toString(),
                    ps[8].toString(),
                    ps[9].toString(),
                    ps[10].toString(),
                    ps[11].toString(),
                    ps[12].toString(),
                    ps[13].toString(),
                    ps[14].toString(),
                    ps[15].toString(),
                    ps[16].toString(),
                    ps[17].toString(),
                    ps[18].toString(),
                    ps[19].toString(),
                    ps[20].toString(),
                    ps[21].toString(),
                    ps[22].toString(),
                    ps[23].toString(),
                    ps[24].toString(),
                    ps[25].toString(),
                    ps[26].toString(),
                    ps[27].toString(),
                    ps[28].toString(),
                    ps[29].toString(),
                    ps[30].toString(),
                    ps[31].toString(),
                    ps[32].toString(),
                    ps[33].toString(),
                    ps[34].toString(),
                    ps[35].toString(),
                    ps[36].toString(),
                    ps[37].toString(),
                    ps[38].toString(),
                    ps[39].toString(),
                    ps[40].toString(),
                    ps[41].toString(),
                    ps[42].toString(),
                    ps[43].toString(),
                    ps[44].toString(),
                    ps[45].toString(),
                    ps[46].toString(),
                    ps[47].toString(),
                    ps[48].toString(),
                    ps[49].toString(),
                    ps[50].toString(),
                    ps[51].toString(),
                    ps[52].toString(),
                    ps[53].toString(),
                    ps[54].toString(),
                    ps[55].toString(),
                    ps[56].toString(),
                    ps[57].toString(),
                    ps[58].toString(),
                    ps[59].toString(),
                    ps[60].toString(),
                    ps[61].toString(),
                    ps[62].toString(),
                    ps[63].toString(),
                    ps[64].toString(),
                    ps[65].toString(),
                    ps[66].toString(),
                    ps[67].toString(),
                    ps[68].toString(),
                    ps[69].toString(),
                    ps[70].toString(),
                    ps[71].toString()

            );

            performedSessions.add(p);

        }

        return performedSessions;
    }

    @Override
    public List<Mentorship> fetchBySelectedFilter(String code, String tutor, String tutored, String formName, String healthFacility, IterationType type, Integer iterationNumber, LifeCycleStatus lfStatus, Date performedStartDate, Date performedEndDate) {
        final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<Mentorship> createQuery = criteriaBuilder.createQuery(Mentorship.class);
        final Root<Mentorship> root = createQuery.from(Mentorship.class);

        root.fetch("tutor").fetch("career");
        root.fetch("tutored").fetch("career");
        root.fetch("form").fetch("programmaticArea");
        root.fetch("healthFacility").fetch("district");
        root.fetch("session", JoinType.LEFT);
        root.fetch("cabinet", JoinType.LEFT);

        createQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (code != null) {
            predicates.add(criteriaBuilder.like(root.get("code"), "%" + code + "%"));
        }

        if (tutor != null) {
            predicates.add(criteriaBuilder.like(root.get("tutor").get("name"), "%" + tutor + "%"));
        }

        if (tutored != null) {
            predicates.add(criteriaBuilder.like(root.get("tutored").get("name"), "%" + tutored + "%"));
        }

        if (formName != null) {
            predicates.add(criteriaBuilder.like(root.get("form").get("name"), "%" + formName + "%"));
        }

        if (healthFacility != null) {
            predicates.add(
                    criteriaBuilder.like(root.get("healthFacility").get("healthFacility"), "%" + healthFacility + "%"));
        }

        if(type != null) {
            predicates.add(criteriaBuilder.equal(root.get("iterationType"), type));
        }

        if(iterationNumber != null) {
            predicates.add(criteriaBuilder.equal(root.get("iterationNumber"), iterationNumber));
        }

        // Deal with performedDate params
        final String PERFORMED_START_DATE_PARAM = "performedStartDateParam";
        if(performedStartDate != null) {
            final ParameterExpression<LocalDate> param = criteriaBuilder.parameter(LocalDate.class, PERFORMED_START_DATE_PARAM);
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("performedDate"), param));
        }

        final String PERFORMED_END_DATE_PARAM = "performedEndDateParam";
        if(performedEndDate != null) {
            final ParameterExpression<LocalDate> param = criteriaBuilder.parameter(LocalDate.class, PERFORMED_END_DATE_PARAM);
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("performedDate"), param));
        }

        if(lfStatus != null) {
            predicates.add(criteriaBuilder.equal(root.get("lifeCycleStatus"), lfStatus));
        }

        createQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        createQuery.orderBy(criteriaBuilder.asc(root.get("code")));

        final TypedQuery<Mentorship> query = this.entityManager.createQuery(createQuery);

        if(performedStartDate != null) {
            query.setParameter(PERFORMED_START_DATE_PARAM, performedStartDate);
        }

        if(performedEndDate != null) {
            query.setParameter(PERFORMED_END_DATE_PARAM, performedEndDate);
        }

        return query.getResultList();
    }
}
