package mz.org.fgh.mentoring.entity.session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

public class PerformedSession {

    private String district;
    private String healthFacility;
    private String formName;
    private Long totalPerformed;
    private String createdAt;
    private String performedDate;
    private String tutorName;
    private String position;
    private String startDate;
    private String endDate;
    private SessionStatus status;
    private String cabinet;
    private String MENTORING_ID;

    /**
     * Properties of HTS report
     */
    private String tutoredName;
    private String door;
    private String timeOfDay;
    private Long atendidos;
    private Long previos;
    private Long testados;
    private Long positivos;
    private Long inscritos;
    private Long cabinetId;

    /**
     * Properties of Narrative report
     */
    private Long preventionVCT;
    private Long preventionPICT;
    private Long preventionIndexCase;
    private Long preventionSaaj;
    private Long preventionHtcLink;
    private Long preventionANC;
    private Long preventionCPN;
    private Long ctStiAdultsPrison;
    private Long ctAdultsPrison;
    private Long ctAdultsVLPrison;
    private Long ctTbHiv;
    private Long ctApss;
    private Long ctAdults;
    private Long ctAdultsVL;
    private Long ctInh;
    private Long ctTbHivCt;
    private Long ctNutrition;
    private Long ctApssTutoreds;
    private Long ctApssSessions;
    private Long ctEAC;
    private Long ctMDC;
    private Long ctCervical;
    private Long ctStiAdults;
    private Long tbSessions;
    private Long tbSessionsCt;
    private Long tbInh;
    private Long tbSessionsPediatric;
    private Long pediatricNutrition;
    private Long pediatricStarART;
    private Long pediatricAMA;
    private Long pediatricTB;
    private Long pediatricVL;

    /**
     * Properties of POP report
     */
    private String elaborado;
    private String aprovado;
    private String revisado;

    /**
     * Props for PMQ-TR_HIV Report
     */
    private int formacao;
    private int instalacoes;
    private int seguranca;
    private int pretestagem;
    private int testagem;
    private int postestagem;
    private int avaliacao;
    private int total;
    private Long mentorship_id;

    /**
     * COP20 Costed Workplan report
     */
    private Long ind_11061;
    private Long ind_11011;
    private Long ind_11031;
    private Long ind_11041;
    private Long ind_11043;
    private Long ind_11073;
    private Long ind_42   ;
    private Long ind_10043;
    private Long ind_10045;
    private Long ind_04071;
    private Long ind_04073;
    private Long ind_04041;
    private Long ind_04077;
    private Long ind_04078;
    private Long ind_04061;
    private Long ind_15051;
    private Long ind_06044;
    private Long ind_02041;
    private Long ind_01102;
    private Long ind_01031;
    private Long ind_01142;
    private Long ind_02063;
    private Long ind_01116;
    private Long ind_02071;
    private Long ind_02021;
    private Long ind_02023;
    private Long ind_08051;
    private Long ind_03029;
    private Long ind_030211;
    private Long ind_030213;
    private Long ind_03011;
    private Long ind_03013;
    private Long ind_05012;
    private Long ind_05031;
    private Long ind_05061;
    private Long ind_05052;
    private Long ind_05054;
    private Long ind_05057;
    private Long ind_19051;
    private Long ind_19015;

    /**
     * PMQ-TR List
     */

    private String MTQ00000751;
    private String MTQ00000752;
    private String MTQ00000753;
    private String MTQ00000754;
    private String MTQ00000755;
    private String MTQ00000756;
    private String MTQ00000757;
    private String MTQ00000758;
    private String MTQ00000759;
    private String MTQ00000760;
    private String MTQ00000761;
    private String MTQ00000762;
    private String MTQ00000763;
    private String MTQ00000764;
    private String MTQ00000765;
    private String MTQ00000766;
    private String MTQ00000767;
    private String MTQ00000768;
    private String MTQ00000769;
    private String MTQ00000770;
    private String MTQ00000771;
    private String MTQ00000772;
    private String MTQ00000773;
    private String MTQ00000774;
    private String MTQ00000775;
    private String MTQ00000776;
    private String MTQ00000777;
    private String MTQ00000778;
    private String MTQ00000779;
    private String MTQ00000780;
    private String MTQ00000781;
    private String MTQ00000782;
    private String MTQ00000783;
    private String MTQ00000784;
    private String MTQ00000785;
    private String MTQ00000786;
    private String MTQ00000787;
    private String MTQ00000788;
    private String MTQ00000789;
    private String MTQ00000790;
    private String MTQ00000791;
    private String MTQ00000792;
    private String MTQ00000793;
    private String MTQ00000794;
    private String MTQ00000795;
    private String MTQ00000796;
    private String MTQ00000797;
    private String MTQ00000798;
    private String MTQ00000799;
    private String MTQ00000800;
    private String MTQ00000801;
    private String MTQ00000802;
    private String MTQ00000803;
    private String MTQ00000804;
    private String MTQ00000805;
    private String MTQ00000806;
    private String MTQ00000807;
    private String MTQ00000808;
    private String MTQ00000809;
    private String MTQ00000810;
    private String MTQ00000811;
    private String MTQ00000812;
    private String MTQ00000813;
    private String MTQ00000814;



    public PerformedSession() {
    }









    public PerformedSession(String district, Long ind_11061, Long ind_11011, Long ind_11031, Long ind_11041,
                            Long ind_11043, Long ind_11073, Long ind_42, Long ind_10043, Long ind_10045, Long ind_04071, Long ind_04073,
                            Long ind_04041, Long ind_04077, Long ind_04078, Long ind_04061, Long ind_15051, Long ind_06044, Long ind_02041,
                            Long ind_01102, Long ind_01031, Long ind_01142, Long ind_02063, Long ind_01116, Long ind_02071,
                            Long ind_02021, Long ind_02023, Long ind_08051, Long ind_03029, Long ind_030211, Long ind_030213,
                            Long ind_03011, Long ind_03013, Long ind_05012, Long ind_05031, Long ind_05061, Long ind_05052,
                            Long ind_05054, Long ind_05057, Long ind_19051, Long ind_19015) {
        this.district = district;
        this.ind_11061 = ind_11061;
        this.ind_11011 = ind_11011;
        this.ind_11031 = ind_11031;
        this.ind_11041 = ind_11041;
        this.ind_11043 = ind_11043;
        this.ind_11073 = ind_11073;
        this.ind_42 = ind_42;
        this.ind_10043 = ind_10043;
        this.ind_10045 = ind_10045;
        this.ind_04071 = ind_04071;
        this.ind_04073 = ind_04073;
        this.ind_04041 = ind_04041;
        this.ind_04077 = ind_04077;
        this.ind_04078 = ind_04078;
        this.ind_04061 = ind_04061;
        this.ind_15051 = ind_15051;
        this.ind_06044 = ind_06044;
        this.ind_02041 = ind_02041;
        this.ind_01102 = ind_01102;
        this.ind_01031 = ind_01031;
        this.ind_01142 = ind_01142;
        this.ind_02063 = ind_02063;
        this.ind_01116 = ind_01116;
        this.ind_02071 = ind_02071;
        this.ind_02021 = ind_02021;
        this.ind_02023 = ind_02023;
        this.ind_08051 = ind_08051;
        this.ind_03029 = ind_03029;
        this.ind_030211 = ind_030211;
        this.ind_030213 = ind_030213;
        this.ind_03011 = ind_03011;
        this.ind_03013 = ind_03013;
        this.ind_05012 = ind_05012;
        this.ind_05031 = ind_05031;
        this.ind_05061 = ind_05061;
        this.ind_05052 = ind_05052;
        this.ind_05054 = ind_05054;
        this.ind_05057 = ind_05057;
        this.ind_19051 = ind_19051;
        this.ind_19015 = ind_19015;
    }









    public PerformedSession(String district, String healthFacility, String performedDate,
                            String tutorName,String tutoredName,  String cabinet, int formacao, int instalacoes, int seguranca,
                            int pretestagem, int testagem, int postestagem, int avaliacao, int total,
                            String createdAt, Long mentorship_id) {
        this.district = district;
        this.healthFacility = healthFacility;
        this.performedDate = performedDate;
        this.tutorName = tutorName;
        this.tutoredName = tutoredName;
        this.cabinet = cabinet;
        this.formacao = formacao;
        this.setInstalacoes(instalacoes);
        this.setSeguranca(seguranca);
        this.setPretestagem(pretestagem);
        this.setTestagem(testagem);
        this.setPostestagem(postestagem);
        this.setAvaliacao(avaliacao);
        this.setTotal(total);
        this.createdAt = createdAt;
        this.mentorship_id = mentorship_id;
    }



    public PerformedSession(final String formName, final Calendar createdAt, final LocalDate performedDate,
                            final String district, final String healthFacility, final String cabinet, final String tutorName,
                            final String tutorSurname, final String position, final LocalDateTime startDate,
                            final LocalDateTime endDate, final SessionStatus status) {
        this.formName = formName;
     //   this.createdAt = new LocalDateTimeAdapter()
       //         .marshal(LocalDateTime.ofInstant(createdAt.toInstant(), createdAt.getTimeZone().toZoneId()));
       // this.performedDate = new LocalDateAdapter().marshal(performedDate);
        this.district = district;
        this.healthFacility = healthFacility;
        this.cabinet = cabinet;
        this.tutorName = tutorName + " " + tutorSurname;
        this.position = position;
      //  this.startDate = new LocalDateTimeAdapter().marshal(startDate);
      //  this.endDate = new LocalDateTimeAdapter().marshal(endDate);
        this.status = status;
    }

    public PerformedSession(final String formName, final Long totalPerformed) {
        this.formName = formName;
        this.totalPerformed = totalPerformed;
    }

    public PerformedSession(final String district, final String healthFacility, final Long totalPerformed) {
        this.district = district;
        this.healthFacility = healthFacility;
        this.totalPerformed = totalPerformed;
    }

    /**
     * This constructor will build the HTS Summary
     */
    public PerformedSession(
            final String districtName,
            final String healthFacility,
            final String performedDate,
            final String tutorName,
            final String tutoredName,
            final String cabinet,
            final String door,
            final String timeOfDay,
            final Long atendidos,
            final Long previos,
            final Long testados,
            final Long positivos,
            final Long inscritos,
            final String createdAt,
            final String MENTORING_ID) {
        this.district = districtName;
        this.healthFacility = healthFacility;
        this.performedDate =performedDate;
        this.tutorName = tutorName;
        this.cabinet = cabinet;
        this.tutoredName = tutoredName;
        this.door = door;
        this.timeOfDay = timeOfDay;
        this.atendidos = atendidos;
        this.previos = previos;
        this.testados = testados;
        this.positivos = positivos;
        this.inscritos = inscritos;
        this.createdAt=createdAt;
        this.MENTORING_ID=MENTORING_ID;
    }

    /**
     * This constructor will build the HTS Summary Mobile
     */
    public PerformedSession(
            final String districtName,
            final String healthFacility,
            final String performedDate,
            final String tutorName,
            final String tutoredName,
            final String cabinet,
            final String door,
            final String timeOfDay,
            final Long atendidos,
            final Long previos,
            final Long testados,
            final Long positivos,
            final Long inscritos,
            final String createdAt,
            final Long cabinetId) {
        this.district = districtName;
        this.healthFacility = healthFacility;
        this.performedDate =performedDate;
        this.tutorName = tutorName;
        this.cabinet = cabinet;
        this.tutoredName = tutoredName;
        this.door = door;
        this.timeOfDay = timeOfDay;
        this.atendidos = atendidos;
        this.previos = previos;
        this.testados = testados;
        this.positivos = positivos;
        this.inscritos = inscritos;
        this.createdAt=createdAt;
        this.cabinetId=cabinetId;
    }





    public PerformedSession(String district, Long preventionVCT, Long preventionPICT, Long preventionIndexCase,
                            Long preventionSaaj, Long preventionHtcLink, Long preventionANC,Long preventionCPN, Long ctStiAdultsPrison,
                            Long ctAdultsPrison, Long ctAdultsVLPrison, Long ctTbHiv, Long ctApss, Long ctAdults, Long ctAdultsVL,
                            Long ctInh, Long ctTbHivCt, Long ctNutrition, Long ctApssTutoreds, Long ctHCW, Long ctEAC, Long ctMDC,Long ctCervical, Long ctStiAdults,Long tbSessions,
                            Long tbSessionsCt, Long tbInh, Long tbSessionsPediatric, Long pediatricNutrition, Long pediatricStarART,
                            Long pediatricAMA, Long pediatricTB, Long pediatricVL) {
        this.district = district;
        this.preventionVCT = preventionVCT;
        this.preventionPICT = preventionPICT;
        this.preventionIndexCase = preventionIndexCase;
        this.preventionSaaj = preventionSaaj;
        this.preventionHtcLink = preventionHtcLink;
        this.preventionANC = preventionANC;
        this.preventionCPN = preventionCPN;
        this.ctStiAdultsPrison = ctStiAdultsPrison;
        this.ctAdultsPrison = ctAdultsPrison;
        this.ctAdultsVLPrison = ctAdultsVLPrison;
        this.ctTbHiv = ctTbHiv;
        this.ctApss = ctApss;
        this.ctAdults = ctAdults;
        this.ctAdultsVL = ctAdultsVL;
        this.ctInh = ctInh;
        this.ctTbHivCt = ctTbHivCt;
        this.ctNutrition = ctNutrition;
        this.ctApssTutoreds = ctApssTutoreds;
        this.ctApssSessions = ctHCW;
        this.ctEAC = ctEAC;
        this.ctMDC = ctMDC;
        this.ctCervical = ctCervical;
        this.ctStiAdults = ctStiAdults;
        this.tbSessions = tbSessions;
        this.tbSessionsCt = tbSessionsCt;
        this.tbInh = tbInh;
        this.tbSessionsPediatric = tbSessionsPediatric;
        this.pediatricNutrition = pediatricNutrition;
        this.pediatricStarART = pediatricStarART;
        this.pediatricAMA = pediatricAMA;
        this.pediatricTB = pediatricTB;
        this.pediatricVL = pediatricVL;
    }



    public PerformedSession(String district, String healthFacility,String performedDate,String tutorName, String formName,
                            String elaborado, String aprovado, String revisado, String createdAt) {

        this.district = district;
        this.healthFacility = healthFacility;
        this.performedDate = performedDate;
        this.tutorName = tutorName;
        this.formName = formName;
        this.elaborado = elaborado;
        this.aprovado = aprovado;
        this.revisado = revisado;
        this.createdAt = createdAt;
    }




    public PerformedSession(String district, String healthFacility, String performedDate,
                            String tutorName, String tutoredName, String cabinet, String mTQ00000751, String mTQ00000752, String mTQ00000753,
                            String mTQ00000754, String mTQ00000755, String mTQ00000756, String mTQ00000757, String mTQ00000758,
                            String mTQ00000759, String mTQ00000760, String mTQ00000761, String mTQ00000762, String mTQ00000763,
                            String mTQ00000764, String mTQ00000765, String mTQ00000766, String mTQ00000767, String mTQ00000768,
                            String mTQ00000769, String mTQ00000770, String mTQ00000771, String mTQ00000772, String mTQ00000773,
                            String mTQ00000774, String mTQ00000775, String mTQ00000776, String mTQ00000777, String mTQ00000778,
                            String mTQ00000779, String mTQ00000780, String mTQ00000781, String mTQ00000782, String mTQ00000783,
                            String mTQ00000784, String mTQ00000785, String mTQ00000786, String mTQ00000787, String mTQ00000788,
                            String mTQ00000789, String mTQ00000790, String mTQ00000791, String mTQ00000792, String mTQ00000793,
                            String mTQ00000794, String mTQ00000795, String mTQ00000796, String mTQ00000797, String mTQ00000798,
                            String mTQ00000799, String mTQ00000800, String mTQ00000801, String mTQ00000802, String mTQ00000803,
                            String mTQ00000804, String mTQ00000805, String mTQ00000806, String mTQ00000807, String mTQ00000808,
                            String mTQ00000809, String mTQ00000810, String mTQ00000811, String mTQ00000812, String mTQ00000813,
                            String mTQ00000814, String createdAt, String MENTORING_ID) {
        this.district = district;
        this.healthFacility = healthFacility;
        this.performedDate = performedDate;
        this.tutorName = tutorName;
        this.tutoredName = tutoredName;
        this.cabinet=cabinet;
        this.MTQ00000751 = mTQ00000751;
        this.MTQ00000752 = mTQ00000752;
        this.MTQ00000753 = mTQ00000753;
        this.MTQ00000754 = mTQ00000754;
        this.MTQ00000755 = mTQ00000755;
        this.MTQ00000756 = mTQ00000756;
        this.MTQ00000757 = mTQ00000757;
        this.MTQ00000758 = mTQ00000758;
        this.MTQ00000759 = mTQ00000759;
        this.MTQ00000760 = mTQ00000760;
        this.MTQ00000761 = mTQ00000761;
        this.MTQ00000762 = mTQ00000762;
        this.MTQ00000763 = mTQ00000763;
        this.MTQ00000764 = mTQ00000764;
        this.MTQ00000765 = mTQ00000765;
        this.MTQ00000766 = mTQ00000766;
        this.MTQ00000767 = mTQ00000767;
        this.MTQ00000768 = mTQ00000768;
        this.MTQ00000769 = mTQ00000769;
        this.MTQ00000770 = mTQ00000770;
        this.MTQ00000771 = mTQ00000771;
        this.MTQ00000772 = mTQ00000772;
        this.MTQ00000773 = mTQ00000773;
        this.MTQ00000774 = mTQ00000774;
        this.MTQ00000775 = mTQ00000775;
        this.MTQ00000776 = mTQ00000776;
        this.MTQ00000777 = mTQ00000777;
        this.MTQ00000778 = mTQ00000778;
        this.MTQ00000779 = mTQ00000779;
        this.MTQ00000780 = mTQ00000780;
        this.MTQ00000781 = mTQ00000781;
        this.MTQ00000782 = mTQ00000782;
        this.MTQ00000783 = mTQ00000783;
        this.MTQ00000784 = mTQ00000784;
        this.MTQ00000785 = mTQ00000785;
        this.MTQ00000786 = mTQ00000786;
        this.MTQ00000787 = mTQ00000787;
        this.MTQ00000788 = mTQ00000788;
        this.MTQ00000789 = mTQ00000789;
        this.MTQ00000790 = mTQ00000790;
        this.MTQ00000791 = mTQ00000791;
        this.MTQ00000792 = mTQ00000792;
        this.MTQ00000793 = mTQ00000793;
        this.MTQ00000794 = mTQ00000794;
        this.MTQ00000795 = mTQ00000795;
        this.MTQ00000796 = mTQ00000796;
        this.MTQ00000797 = mTQ00000797;
        this.MTQ00000798 = mTQ00000798;
        this.MTQ00000799 = mTQ00000799;
        this.MTQ00000800 = mTQ00000800;
        this.MTQ00000801 = mTQ00000801;
        this.MTQ00000802 = mTQ00000802;
        this.MTQ00000803 = mTQ00000803;
        this.MTQ00000804 = mTQ00000804;
        this.MTQ00000805 = mTQ00000805;
        this.MTQ00000806 = mTQ00000806;
        this.MTQ00000807 = mTQ00000807;
        this.MTQ00000808 = mTQ00000808;
        this.MTQ00000809 = mTQ00000809;
        this.MTQ00000810 = mTQ00000810;
        this.MTQ00000811 = mTQ00000811;
        this.MTQ00000812 = mTQ00000812;
        this.MTQ00000813 = mTQ00000813;
        this.MTQ00000814 = mTQ00000814;
        this.createdAt = createdAt;
        this.MENTORING_ID = MENTORING_ID;
    }


    public String getDistrict() {
        return this.district;
    }

    public String getHealthFacility() {
        return this.healthFacility;
    }

    public String getFormName() {
        return this.formName;
    }

    public Long getTotalPerformed() {
        return this.totalPerformed;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public String getPerformedDate() {
        return this.performedDate;
    }

    public String getTutorName() {
        return this.tutorName;
    }

    public String getPosition() {
        return this.position;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public SessionStatus getStatus() {
        return this.status;
    }

    public String getCabinet() {
        return this.cabinet;
    }

    public String getTutoredName() {
        return tutoredName;
    }

    public void setTutoredName(String tutoredName) {
        this.tutoredName = tutoredName;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public Long getAtendidos() {
        return atendidos;
    }

    public void setAtendidos(Long atendidos) {
        this.atendidos = atendidos;
    }

    public Long getPrevios() {
        return previos;
    }

    public void setPrevios(Long previos) {
        this.previos = previos;
    }

    public Long getTestados() {
        return testados;
    }

    public void setTestados(Long testados) {
        this.testados = testados;
    }

    public Long getPositivos() {
        return positivos;
    }

    public void setPositivos(Long positivos) {
        this.positivos = positivos;
    }

    public Long getInscritos() {
        return inscritos;
    }

    public void setInscritos(Long inscritos) {
        this.inscritos = inscritos;
    }

    public Long getPreventionVCT() {
        return preventionVCT;
    }

    public void setPreventionVCT(Long preventionVCT) {
        this.preventionVCT = preventionVCT;
    }

    public Long getPreventionPICT() {
        return preventionPICT;
    }

    public void setPreventionPICT(Long preventionPICT) {
        this.preventionPICT = preventionPICT;
    }

    public Long getPreventionIndexCase() {
        return preventionIndexCase;
    }

    public void setPreventionIndexCase(Long preventionIndexCase) {
        this.preventionIndexCase = preventionIndexCase;
    }

    public Long getPreventionSaaj() {
        return preventionSaaj;
    }

    public void setPreventionSaaj(Long preventionSaaj) {
        this.preventionSaaj = preventionSaaj;
    }

    public Long getPreventionHtcLink() {
        return preventionHtcLink;
    }

    public void setPreventionHtcLink(Long preventionHtcLink) {
        this.preventionHtcLink = preventionHtcLink;
    }

    public Long getPreventionANC() {
        return preventionANC;
    }

    public void setPreventionANC(Long preventionANC) {
        this.preventionANC = preventionANC;
    }

    public Long getCtStiAdultsPrison() {
        return ctStiAdultsPrison;
    }

    public void setCtStiAdultsPrison(Long ctStiAdultsPrison) {
        this.ctStiAdultsPrison = ctStiAdultsPrison;
    }

    public Long getCtAdultsPrison() {
        return ctAdultsPrison;
    }

    public void setCtAdultsPrison(Long ctAdultsPrison) {
        this.ctAdultsPrison = ctAdultsPrison;
    }

    public Long getCtAdultsVLPrison() {
        return ctAdultsVLPrison;
    }

    public void setCtAdultsVLPrison(Long ctAdultsVLPrison) {
        this.ctAdultsVLPrison = ctAdultsVLPrison;
    }

    public Long getCtTbHiv() {
        return ctTbHiv;
    }

    public void setCtTbHiv(Long ctTbHiv) {
        this.ctTbHiv = ctTbHiv;
    }

    public Long getCtApss() {
        return ctApss;
    }

    public void setCtApss(Long ctApss) {
        this.ctApss = ctApss;
    }

    public Long getCtAdults() {
        return ctAdults;
    }

    public void setCtAdults(Long ctAdults) {
        this.ctAdults = ctAdults;
    }

    public Long getCtAdultsVL() {
        return ctAdultsVL;
    }

    public void setCtAdultsVL(Long ctAdultsVL) {
        this.ctAdultsVL = ctAdultsVL;
    }

    public Long getCtInh() {
        return ctInh;
    }

    public void setCtInh(Long ctInh) {
        this.ctInh = ctInh;
    }

    public Long getCtNutrition() {
        return ctNutrition;
    }

    public void setCtNutrition(Long ctNutrition) {
        this.ctNutrition = ctNutrition;
    }

    public Long getCtApssTutoreds() {
        return ctApssTutoreds;
    }

    public void setCtApssTutoreds(Long ctApssTutoreds) {
        this.ctApssTutoreds = ctApssTutoreds;
    }

    public Long getCtApssSessions() {
        return ctApssSessions;
    }

    public void setCtApssSessions(Long ctHCW) {
        this.ctApssSessions = ctHCW;
    }

    public Long getCtEAC() {
        return ctEAC;
    }

    public void setCtEAC(Long ctEAC) {
        this.ctEAC = ctEAC;
    }

    public Long getCtCervical() {
        return ctCervical;
    }

    public void setCtCervical(Long ctCervical) {
        this.ctCervical = ctCervical;
    }

    public Long getTbSessions() {
        return tbSessions;
    }

    public void setTbSessions(Long tbSessions) {
        this.tbSessions = tbSessions;
    }

    public Long getTbSessionsCt() {
        return tbSessionsCt;
    }

    public void setTbSessionsCt(Long tbSessionsCt) {
        this.tbSessionsCt = tbSessionsCt;
    }

    public Long getTbInh() {
        return tbInh;
    }

    public void setTbInh(Long tbInh) {
        this.tbInh = tbInh;
    }

    public Long getTbSessionsPediatric() {
        return tbSessionsPediatric;
    }

    public void setTbSessionsPediatric(Long tbSessionsPediatric) {
        this.tbSessionsPediatric = tbSessionsPediatric;
    }

    public Long getPediatricNutrition() {
        return pediatricNutrition;
    }

    public void setPediatricNutrition(Long pediatricNutrition) {
        this.pediatricNutrition = pediatricNutrition;
    }

    public Long getPediatricStarART() {
        return pediatricStarART;
    }

    public void setPediatricStarART(Long pediatricStarART) {
        this.pediatricStarART = pediatricStarART;
    }

    public Long getPediatricAMA() {
        return pediatricAMA;
    }

    public void setPediatricAMA(Long pediatricAMA) {
        this.pediatricAMA = pediatricAMA;
    }

    public Long getPediatricTB() {
        return pediatricTB;
    }

    public void setPediatricTB(Long pediatricTB) {
        this.pediatricTB = pediatricTB;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getElaborado() {
        return elaborado;
    }

    public void setElaborado(String elaborado) {
        this.elaborado = elaborado;
    }

    public String getAprovado() {
        return aprovado;
    }

    public void setAprovado(String aprovado) {
        this.aprovado = aprovado;
    }

    public String getRevisado() {
        return revisado;
    }

    public void setRevisado(String revisado) {
        this.revisado = revisado;
    }

    public Long getPediatricVL() {
        return pediatricVL;
    }

    public void setPediatricVL(Long pediatricVL) {
        this.pediatricVL = pediatricVL;
    }

    public Long getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(Long cabinetId) {
        this.cabinetId = cabinetId;
    }

    public Long getPreventionCPN() {
        return preventionCPN;
    }

    public void setPreventionCPN(Long preventionCPN) {
        this.preventionCPN = preventionCPN;
    }

    public Long getCtTbHivCt() {
        return ctTbHivCt;
    }

    public void setCtTbHivCt(Long ctTbHivCt) {
        this.ctTbHivCt = ctTbHivCt;
    }

    public Long getCtMDC() {
        return ctMDC;
    }

    public void setCtMDC(Long ctMDC) {
        this.ctMDC = ctMDC;
    }

    public Long getCtStiAdults() {
        return ctStiAdults;
    }

    public void setCtStiAdults(Long ctStiAdults) {
        this.ctStiAdults = ctStiAdults;
    }



    public double getFormacao() {
        return formacao;
    }




    public Long getMentorship_id() {
        return mentorship_id;
    }



    public void setMentorship_id(Long mentorship_id) {
        this.mentorship_id = mentorship_id;
    }



    public int getInstalacoes() {
        return instalacoes;
    }



    public void setInstalacoes(int instalacoes) {
        this.instalacoes = instalacoes;
    }



    public int getSeguranca() {
        return seguranca;
    }



    public void setSeguranca(int seguranca) {
        this.seguranca = seguranca;
    }



    public int getPretestagem() {
        return pretestagem;
    }



    public void setPretestagem(int pretestagem) {
        this.pretestagem = pretestagem;
    }



    public int getTestagem() {
        return testagem;
    }



    public void setTestagem(int testagem) {
        this.testagem = testagem;
    }



    public int getPostestagem() {
        return postestagem;
    }



    public void setPostestagem(int postestagem) {
        this.postestagem = postestagem;
    }



    public int getAvaliacao() {
        return avaliacao;
    }



    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }



    public int getTotal() {
        return total;
    }



    public void setTotal(int total) {
        this.total = total;
    }



    public Long getInd_11061() {
        return ind_11061;
    }



    public void setInd_11061(Long ind_11061) {
        this.ind_11061 = ind_11061;
    }



    public Long getInd_11011() {
        return ind_11011;
    }



    public void setInd_11011(Long ind_11011) {
        this.ind_11011 = ind_11011;
    }



    public Long getInd_11031() {
        return ind_11031;
    }



    public void setInd_11031(Long ind_11031) {
        this.ind_11031 = ind_11031;
    }



    public Long getInd_11041() {
        return ind_11041;
    }



    public void setInd_11041(Long ind_11041) {
        this.ind_11041 = ind_11041;
    }



    public Long getInd_11043() {
        return ind_11043;
    }



    public void setInd_11043(Long ind_11043) {
        this.ind_11043 = ind_11043;
    }



    public Long getInd_11073() {
        return ind_11073;
    }



    public void setInd_11073(Long ind_11073) {
        this.ind_11073 = ind_11073;
    }



    public Long getInd_42() {
        return ind_42;
    }



    public void setInd_42(Long ind_42) {
        this.ind_42 = ind_42;
    }



    public Long getInd_10043() {
        return ind_10043;
    }



    public void setInd_10043(Long ind_10043) {
        this.ind_10043 = ind_10043;
    }



    public Long getInd_10045() {
        return ind_10045;
    }



    public void setInd_10045(Long ind_10045) {
        this.ind_10045 = ind_10045;
    }



    public Long getInd_04071() {
        return ind_04071;
    }



    public void setInd_04071(Long ind_04071) {
        this.ind_04071 = ind_04071;
    }



    public Long getInd_04073() {
        return ind_04073;
    }



    public void setInd_04073(Long ind_04073) {
        this.ind_04073 = ind_04073;
    }



    public Long getInd_04041() {
        return ind_04041;
    }



    public void setInd_04041(Long ind_04041) {
        this.ind_04041 = ind_04041;
    }



    public Long getInd_04061() {
        return ind_04061;
    }



    public void setInd_04061(Long ind_04061) {
        this.ind_04061 = ind_04061;
    }



    public Long getInd_15051() {
        return ind_15051;
    }



    public void setInd_15051(Long ind_15051) {
        this.ind_15051 = ind_15051;
    }



    public Long getInd_06044() {
        return ind_06044;
    }



    public void setInd_06044(Long ind_06044) {
        this.ind_06044 = ind_06044;
    }



    public Long getInd_02041() {
        return ind_02041;
    }



    public void setInd_02041(Long ind_02041) {
        this.ind_02041 = ind_02041;
    }



    public Long getInd_01102() {
        return ind_01102;
    }



    public void setInd_01102(Long ind_01102) {
        this.ind_01102 = ind_01102;
    }



    public Long getInd_01031() {
        return ind_01031;
    }



    public void setInd_01031(Long ind_01031) {
        this.ind_01031 = ind_01031;
    }



    public Long getInd_01142() {
        return ind_01142;
    }



    public void setInd_01142(Long ind_01142) {
        this.ind_01142 = ind_01142;
    }



    public Long getInd_02063() {
        return ind_02063;
    }



    public void setInd_02063(Long ind_02063) {
        this.ind_02063 = ind_02063;
    }



    public Long getInd_01116() {
        return ind_01116;
    }



    public void setInd_01116(Long ind_01116) {
        this.ind_01116 = ind_01116;
    }



    public Long getInd_02071() {
        return ind_02071;
    }



    public void setInd_02071(Long ind_02071) {
        this.ind_02071 = ind_02071;
    }



    public Long getInd_02021() {
        return ind_02021;
    }



    public void setInd_02021(Long ind_02021) {
        this.ind_02021 = ind_02021;
    }



    public Long getInd_02023() {
        return ind_02023;
    }



    public void setInd_02023(Long ind_02023) {
        this.ind_02023 = ind_02023;
    }



    public Long getInd_03029() {
        return ind_03029;
    }



    public void setInd_03029(Long ind_03029) {
        this.ind_03029 = ind_03029;
    }



    public Long getInd_03011() {
        return ind_03011;
    }



    public void setInd_03011(Long ind_03011) {
        this.ind_03011 = ind_03011;
    }



    public Long getInd_03013() {
        return ind_03013;
    }



    public void setInd_03013(Long ind_03013) {
        this.ind_03013 = ind_03013;
    }



    public Long getInd_05012() {
        return ind_05012;
    }



    public void setInd_05012(Long ind_05012) {
        this.ind_05012 = ind_05012;
    }



    public Long getInd_05031() {
        return ind_05031;
    }



    public void setInd_05031(Long ind_05031) {
        this.ind_05031 = ind_05031;
    }



    public Long getInd_05061() {
        return ind_05061;
    }



    public void setInd_05061(Long ind_05061) {
        this.ind_05061 = ind_05061;
    }



    public Long getInd_05052() {
        return ind_05052;
    }



    public void setInd_05052(Long ind_05052) {
        this.ind_05052 = ind_05052;
    }



    public Long getInd_05054() {
        return ind_05054;
    }



    public void setInd_05054(Long ind_05054) {
        this.ind_05054 = ind_05054;
    }



    public Long getInd_05057() {
        return ind_05057;
    }



    public void setInd_05057(Long ind_05057) {
        this.ind_05057 = ind_05057;
    }


    public Long getInd_04077() {
        return ind_04077;
    }


    public void setInd_04077(Long ind_04077) {
        this.ind_04077 = ind_04077;
    }


    public Long getInd_08051() {
        return ind_08051;
    }


    public void setInd_08051(Long ind_08051) {
        this.ind_08051 = ind_08051;
    }


    public Long getInd_030211() {
        return ind_030211;
    }


    public void setInd_030211(Long ind_030211) {
        this.ind_030211 = ind_030211;
    }


    public Long getInd_030213() {
        return ind_030213;
    }


    public void setInd_030213(Long ind_030213) {
        this.ind_030213 = ind_030213;
    }


    public Long getInd_19051() {
        return ind_19051;
    }


    public void setInd_19051(Long ind_19051) {
        this.ind_19051 = ind_19051;
    }


    public Long getInd_19015() {
        return ind_19015;
    }


    public void setInd_19015(Long ind_19015) {
        this.ind_19015 = ind_19015;
    }









    public String getMENTORING_ID() {
        return MENTORING_ID;
    }









    public void setMENTORING_ID(String mENTORING_ID) {
        MENTORING_ID = mENTORING_ID;
    }









    public String getMTQ00000751() {
        return MTQ00000751;
    }









    public void setMTQ00000751(String mTQ00000751) {
        MTQ00000751 = mTQ00000751;
    }









    public String getMTQ00000752() {
        return MTQ00000752;
    }









    public void setMTQ00000752(String mTQ00000752) {
        MTQ00000752 = mTQ00000752;
    }









    public String getMTQ00000753() {
        return MTQ00000753;
    }









    public void setMTQ00000753(String mTQ00000753) {
        MTQ00000753 = mTQ00000753;
    }









    public String getMTQ00000754() {
        return MTQ00000754;
    }









    public void setMTQ00000754(String mTQ00000754) {
        MTQ00000754 = mTQ00000754;
    }









    public String getMTQ00000755() {
        return MTQ00000755;
    }









    public void setMTQ00000755(String mTQ00000755) {
        MTQ00000755 = mTQ00000755;
    }









    public String getMTQ00000756() {
        return MTQ00000756;
    }









    public void setMTQ00000756(String mTQ00000756) {
        MTQ00000756 = mTQ00000756;
    }









    public String getMTQ00000757() {
        return MTQ00000757;
    }









    public void setMTQ00000757(String mTQ00000757) {
        MTQ00000757 = mTQ00000757;
    }









    public String getMTQ00000758() {
        return MTQ00000758;
    }









    public void setMTQ00000758(String mTQ00000758) {
        MTQ00000758 = mTQ00000758;
    }









    public String getMTQ00000759() {
        return MTQ00000759;
    }









    public void setMTQ00000759(String mTQ00000759) {
        MTQ00000759 = mTQ00000759;
    }









    public String getMTQ00000760() {
        return MTQ00000760;
    }









    public void setMTQ00000760(String mTQ00000760) {
        MTQ00000760 = mTQ00000760;
    }









    public String getMTQ00000761() {
        return MTQ00000761;
    }









    public void setMTQ00000761(String mTQ00000761) {
        MTQ00000761 = mTQ00000761;
    }









    public String getMTQ00000762() {
        return MTQ00000762;
    }









    public void setMTQ00000762(String mTQ00000762) {
        MTQ00000762 = mTQ00000762;
    }









    public String getMTQ00000763() {
        return MTQ00000763;
    }









    public void setMTQ00000763(String mTQ00000763) {
        MTQ00000763 = mTQ00000763;
    }









    public String getMTQ00000764() {
        return MTQ00000764;
    }









    public void setMTQ00000764(String mTQ00000764) {
        MTQ00000764 = mTQ00000764;
    }









    public String getMTQ00000765() {
        return MTQ00000765;
    }









    public void setMTQ00000765(String mTQ00000765) {
        MTQ00000765 = mTQ00000765;
    }









    public String getMTQ00000766() {
        return MTQ00000766;
    }









    public void setMTQ00000766(String mTQ00000766) {
        MTQ00000766 = mTQ00000766;
    }









    public String getMTQ00000767() {
        return MTQ00000767;
    }









    public void setMTQ00000767(String mTQ00000767) {
        MTQ00000767 = mTQ00000767;
    }









    public String getMTQ00000768() {
        return MTQ00000768;
    }









    public void setMTQ00000768(String mTQ00000768) {
        MTQ00000768 = mTQ00000768;
    }









    public String getMTQ00000769() {
        return MTQ00000769;
    }









    public void setMTQ00000769(String mTQ00000769) {
        MTQ00000769 = mTQ00000769;
    }









    public String getMTQ00000770() {
        return MTQ00000770;
    }









    public void setMTQ00000770(String mTQ00000770) {
        MTQ00000770 = mTQ00000770;
    }









    public String getMTQ00000771() {
        return MTQ00000771;
    }









    public void setMTQ00000771(String mTQ00000771) {
        MTQ00000771 = mTQ00000771;
    }









    public String getMTQ00000772() {
        return MTQ00000772;
    }









    public void setMTQ00000772(String mTQ00000772) {
        MTQ00000772 = mTQ00000772;
    }









    public String getMTQ00000773() {
        return MTQ00000773;
    }









    public void setMTQ00000773(String mTQ00000773) {
        MTQ00000773 = mTQ00000773;
    }









    public String getMTQ00000774() {
        return MTQ00000774;
    }









    public void setMTQ00000774(String mTQ00000774) {
        MTQ00000774 = mTQ00000774;
    }









    public String getMTQ00000775() {
        return MTQ00000775;
    }









    public void setMTQ00000775(String mTQ00000775) {
        MTQ00000775 = mTQ00000775;
    }









    public String getMTQ00000776() {
        return MTQ00000776;
    }









    public void setMTQ00000776(String mTQ00000776) {
        MTQ00000776 = mTQ00000776;
    }









    public String getMTQ00000777() {
        return MTQ00000777;
    }









    public void setMTQ00000777(String mTQ00000777) {
        MTQ00000777 = mTQ00000777;
    }









    public String getMTQ00000778() {
        return MTQ00000778;
    }









    public void setMTQ00000778(String mTQ00000778) {
        MTQ00000778 = mTQ00000778;
    }









    public String getMTQ00000779() {
        return MTQ00000779;
    }









    public void setMTQ00000779(String mTQ00000779) {
        MTQ00000779 = mTQ00000779;
    }









    public String getMTQ00000780() {
        return MTQ00000780;
    }









    public void setMTQ00000780(String mTQ00000780) {
        MTQ00000780 = mTQ00000780;
    }









    public String getMTQ00000781() {
        return MTQ00000781;
    }









    public void setMTQ00000781(String mTQ00000781) {
        MTQ00000781 = mTQ00000781;
    }









    public String getMTQ00000782() {
        return MTQ00000782;
    }









    public void setMTQ00000782(String mTQ00000782) {
        MTQ00000782 = mTQ00000782;
    }









    public String getMTQ00000783() {
        return MTQ00000783;
    }









    public void setMTQ00000783(String mTQ00000783) {
        MTQ00000783 = mTQ00000783;
    }









    public String getMTQ00000784() {
        return MTQ00000784;
    }









    public void setMTQ00000784(String mTQ00000784) {
        MTQ00000784 = mTQ00000784;
    }









    public String getMTQ00000785() {
        return MTQ00000785;
    }









    public void setMTQ00000785(String mTQ00000785) {
        MTQ00000785 = mTQ00000785;
    }









    public String getMTQ00000786() {
        return MTQ00000786;
    }









    public void setMTQ00000786(String mTQ00000786) {
        MTQ00000786 = mTQ00000786;
    }









    public String getMTQ00000787() {
        return MTQ00000787;
    }









    public void setMTQ00000787(String mTQ00000787) {
        MTQ00000787 = mTQ00000787;
    }









    public String getMTQ00000788() {
        return MTQ00000788;
    }









    public void setMTQ00000788(String mTQ00000788) {
        MTQ00000788 = mTQ00000788;
    }









    public String getMTQ00000789() {
        return MTQ00000789;
    }









    public void setMTQ00000789(String mTQ00000789) {
        MTQ00000789 = mTQ00000789;
    }









    public String getMTQ00000790() {
        return MTQ00000790;
    }









    public void setMTQ00000790(String mTQ00000790) {
        MTQ00000790 = mTQ00000790;
    }









    public String getMTQ00000791() {
        return MTQ00000791;
    }









    public void setMTQ00000791(String mTQ00000791) {
        MTQ00000791 = mTQ00000791;
    }









    public String getMTQ00000792() {
        return MTQ00000792;
    }









    public void setMTQ00000792(String mTQ00000792) {
        MTQ00000792 = mTQ00000792;
    }









    public String getMTQ00000793() {
        return MTQ00000793;
    }









    public void setMTQ00000793(String mTQ00000793) {
        MTQ00000793 = mTQ00000793;
    }









    public String getMTQ00000794() {
        return MTQ00000794;
    }









    public void setMTQ00000794(String mTQ00000794) {
        MTQ00000794 = mTQ00000794;
    }









    public String getMTQ00000795() {
        return MTQ00000795;
    }









    public void setMTQ00000795(String mTQ00000795) {
        MTQ00000795 = mTQ00000795;
    }









    public String getMTQ00000796() {
        return MTQ00000796;
    }









    public void setMTQ00000796(String mTQ00000796) {
        MTQ00000796 = mTQ00000796;
    }









    public String getMTQ00000797() {
        return MTQ00000797;
    }









    public void setMTQ00000797(String mTQ00000797) {
        MTQ00000797 = mTQ00000797;
    }









    public String getMTQ00000798() {
        return MTQ00000798;
    }









    public void setMTQ00000798(String mTQ00000798) {
        MTQ00000798 = mTQ00000798;
    }









    public String getMTQ00000799() {
        return MTQ00000799;
    }









    public void setMTQ00000799(String mTQ00000799) {
        MTQ00000799 = mTQ00000799;
    }









    public String getMTQ00000800() {
        return MTQ00000800;
    }









    public void setMTQ00000800(String mTQ00000800) {
        MTQ00000800 = mTQ00000800;
    }









    public String getMTQ00000801() {
        return MTQ00000801;
    }









    public void setMTQ00000801(String mTQ00000801) {
        MTQ00000801 = mTQ00000801;
    }









    public String getMTQ00000802() {
        return MTQ00000802;
    }









    public void setMTQ00000802(String mTQ00000802) {
        MTQ00000802 = mTQ00000802;
    }









    public String getMTQ00000803() {
        return MTQ00000803;
    }









    public void setMTQ00000803(String mTQ00000803) {
        MTQ00000803 = mTQ00000803;
    }









    public String getMTQ00000804() {
        return MTQ00000804;
    }









    public void setMTQ00000804(String mTQ00000804) {
        MTQ00000804 = mTQ00000804;
    }









    public String getMTQ00000805() {
        return MTQ00000805;
    }









    public void setMTQ00000805(String mTQ00000805) {
        MTQ00000805 = mTQ00000805;
    }









    public String getMTQ00000806() {
        return MTQ00000806;
    }









    public void setMTQ00000806(String mTQ00000806) {
        MTQ00000806 = mTQ00000806;
    }









    public String getMTQ00000807() {
        return MTQ00000807;
    }









    public void setMTQ00000807(String mTQ00000807) {
        MTQ00000807 = mTQ00000807;
    }









    public String getMTQ00000808() {
        return MTQ00000808;
    }









    public void setMTQ00000808(String mTQ00000808) {
        MTQ00000808 = mTQ00000808;
    }









    public String getMTQ00000809() {
        return MTQ00000809;
    }









    public void setMTQ00000809(String mTQ00000809) {
        MTQ00000809 = mTQ00000809;
    }









    public String getMTQ00000810() {
        return MTQ00000810;
    }









    public void setMTQ00000810(String mTQ00000810) {
        MTQ00000810 = mTQ00000810;
    }









    public String getMTQ00000811() {
        return MTQ00000811;
    }









    public void setMTQ00000811(String mTQ00000811) {
        MTQ00000811 = mTQ00000811;
    }









    public String getMTQ00000812() {
        return MTQ00000812;
    }









    public void setMTQ00000812(String mTQ00000812) {
        MTQ00000812 = mTQ00000812;
    }









    public String getMTQ00000813() {
        return MTQ00000813;
    }









    public void setMTQ00000813(String mTQ00000813) {
        MTQ00000813 = mTQ00000813;
    }









    public String getMTQ00000814() {
        return MTQ00000814;
    }









    public void setMTQ00000814(String mTQ00000814) {
        MTQ00000814 = mTQ00000814;
    }









    public Long getInd_04078() {
        return ind_04078;
    }









    public void setInd_04078(Long ind_04078) {
        this.ind_04078 = ind_04078;
    }




}

