package mz.org.fgh.mentoring.controller.utils;

import lombok.*;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.entity.career.Career;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.formtarget.FormTarget;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.tutored.Tutored;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;



@XmlRootElement
public class Metadata {
    private List<HealthFacility> healthFacilities;

    private List<Career> careers;

    private List<FormQuestion> formQuestions;

    private List<Tutored> tutored;

    private List<Cabinet> cabinets;

    private List<FormTarget> formTargets;
    public Metadata(final List<HealthFacility> healthFacilities, final List<Career> careers,
                    final List<FormQuestion> formQuestions, List<Tutored> tutoreds, List<Cabinet> cabinets,
                    List<FormTarget> formTargets){

        this.healthFacilities = healthFacilities;
        this.careers = careers;
        this.formQuestions = formQuestions;
        this.tutored = tutoreds;
        this.cabinets = cabinets;
        this.formTargets = formTargets;
    }
    public Metadata(final List<Cabinet> cabinets){
      this.cabinets = cabinets;
    }
}
