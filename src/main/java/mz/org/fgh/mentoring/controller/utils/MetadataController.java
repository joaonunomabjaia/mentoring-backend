package mz.org.fgh.mentoring.controller.utils;

import io.micronaut.http.annotation.Controller;
import mz.org.fgh.mentoring.api.RESTAPIMapping;

@Controller(RESTAPIMapping.METADATA)
public class MetadataController {
/*
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

        List<CabinetDTO> cabinets = this.cabinetService.findAllCabinets();

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

        List<CabinetDTO> cabinets = this.cabinetService.findAllCabinets();
        return new Metadata(cabinets);
    }

 */
}
