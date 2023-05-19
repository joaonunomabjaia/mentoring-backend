package mz.org.fgh.mentoring.controller.utils;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.entity.cabinet.Cabinet;
import mz.org.fgh.mentoring.entity.career.Career;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.formtarget.FormTarget;
import mz.org.fgh.mentoring.entity.healthfacility.HealthFacility;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.service.career.CareerService;
import mz.org.fgh.mentoring.service.form.FormTargetService;
import mz.org.fgh.mentoring.service.formquestion.FormQuestionService;
import mz.org.fgh.mentoring.service.healthfacility.HealthFacilityService;
import mz.org.fgh.mentoring.service.location.CabinetService;
import mz.org.fgh.mentoring.service.tutored.TutoredService;

import java.util.List;

@Controller(RESTAPIMapping.METADATA)
public class MetadataController {

    @Inject
    private HealthFacilityService healthFacilityService;

    @Inject
    private CareerService careerService;

    @Inject
    private FormQuestionService formQuestionService;

    @Inject
    private TutoredService tutoredService;

    @Inject
    private CabinetService cabinetService;

    @Inject
    private FormTargetService formTargetService;

    @Get("/{uuid}")
    public Metadata loadMetadata(@PathVariable("uuid") String uuid){

        List<HealthFacility> healthFacilities = this.healthFacilityService.findAllHealthFacilities();

        List<Career> careers = this.careerService.findAll();

        List<Cabinet> cabinets = this.cabinetService.findAllCabinets();

        List<FormQuestion> formQuestions = this.formQuestionService.findAll();

        List<Tutored> tutoreds = this.tutoredService.findAll();

        Tutor tutor = new Tutor();
        tutor.setUuid(uuid);

        List<FormTarget> formTargets = this.formTargetService.findFormTargetByTutor(tutor);

        final Metadata metadata = new Metadata(healthFacilities, careers, formQuestions, tutoreds, cabinets,
                formTargets);

    return metadata;

    }
    @Get("/cabinets")
    public Metadata loadMetadataCabinets(){

        List<Cabinet> cabinets = this.cabinetService.findAllCabinets();
        return new Metadata(cabinets);
    }
}
