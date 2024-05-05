package mz.org.fgh.mentoring.controller.mentorship;

import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.dto.mentorship.MentorshipDTO;
import mz.org.fgh.mentoring.entity.mentorship.Mentorship;
import mz.org.fgh.mentoring.entity.session.Session;
import mz.org.fgh.mentoring.service.mentorship.MentorshipService;
import mz.org.fgh.mentoring.service.session.SessionService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.PerformedSession;
import mz.org.fgh.mentoring.util.SubmitedSessions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static mz.org.fgh.mentoring.api.RESTAPIMapping.API_VERSION;

@Controller(RESTAPIMapping.MENTORSHIP)
@Secured(SecurityRule.IS_AUTHENTICATED)
public class MentorshipController {

    public static final Logger LOG = LoggerFactory.getLogger(MentorshipController.class);

    MentorshipService mentorshipService;

    SessionService sessionService;

    @Operation(summary = "Return a list off @PerformedSession")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Mentorship")
    @Version(API_VERSION)
    @Get("/performed-sessions-pmqtr-list")
    public List<PerformedSession> findPerformedSessionsPMQTRList(@QueryValue("startDate") String startDate,
                                                                 @QueryValue("endDate") String endDate) {
        return mentorshipService.findPerformedSessionsBySelectedFilterPMQTRList(DateUtils.createDate(startDate, DateUtils.DDMM_DATE_FORMAT), DateUtils.createDate(endDate, DateUtils.DDMM_DATE_FORMAT));
    }

    @Operation(summary = "Creates a mentorship record")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Mentorship")
    @Version(API_VERSION)
    @Post
    public HttpResponse<RestAPIResponse> createMentorshipProcess(Mentorship mentorship) {
        mentorshipService.createMentorship(mentorship);
        LOG.debug("Created mentorship {}", mentorship);

        return HttpResponse.ok().body(mentorship);
    }

    @Operation(summary = "Search a Mentorship")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Mentorship")
    @Version(API_VERSION)
    @Get
    public List<Mentorship> findBySelectedFilter(   @QueryValue("code") String code,
                                                    @QueryValue("tutor") String tutor,
                                                    @QueryValue("tutored") String tutored,
                                                    @QueryValue("form") String form,
                                                    @QueryValue("healthFacility") String healthFacility,
                                                    @QueryValue("iterationType") String iterationType,
                                                    @QueryValue("iterationNumber") Integer iterationNumber,
                                                    @QueryValue("lifeCycleStatus") String lifeCycleStatus,
                                                    @QueryValue("performedStartDate") Date performedStartDate,
                                                    @QueryValue("performedEndDate") Date performedEndDate){
        List<Mentorship> mentorships = this.mentorshipService.fetchBySelectedFilter(code,
                                                                                    tutor,
                                                                                    tutored,
                                                                                    form,
                                                                                    healthFacility,
                                                                                    iterationType,
                                                                                    iterationNumber,
                                                                                    lifeCycleStatus,
                                                                                    performedStartDate,
                                                                                    performedEndDate);
        return mentorships;
    }

    @Post("sync")
    public Mentorship synchronizeMentorships(Session session) {
        this.mentorshipService.synchronizeMentorships(Collections.singletonList(session));
        return null;
    }

    @Get("sessions")
    public List<SubmitedSessions>findSubmitedSessions(){
        return this.sessionService.findNumberOfSessionsPerDistrict();
    }

    @Get("sessions/{tutoruuid}")
    public List<SubmitedSessions> findSubmitedSessionsOfTutor(@PathVariable("tutoruuid") String tutoruuid){
        return this.sessionService.findNumberOfSessionsPerDistrict(tutoruuid);
    }

    @Get("performed-sessions")
    public List<PerformedSession> findPerformedSessions(@QueryValue("districtUuid") String districtUuid,
                                                       @QueryValue("healthFacilityUuid") String healthFacilityUuid,
                                                       @QueryValue("formUuid") String formUuid,
                                                       @QueryValue("programmaticAreaUuid") String programmaticAreaUuid,
                                                       @QueryValue("tutorUuid") String tutorUuid,
                                                       @QueryValue("cabinetUuid") String cabinetUuid,
                                                       @QueryValue("startDate") Date startDate,
                                                       @QueryValue("endDate") Date endDate){

        List<PerformedSession> performedSessions = this.sessionService.findPerformedSessionsBySelectedFilter(
                                                                                                            districtUuid, healthFacilityUuid, formUuid,
                                                                                                            programmaticAreaUuid, tutorUuid, cabinetUuid, startDate,
                                                                                                            endDate);
        return performedSessions;
    }

    @Get("performed-sessions-list")
    public List<PerformedSession> findPerformedSessionsList(@QueryValue("districtUuid") String districtUuid,
                                                           @QueryValue("healthFacilityUuid") String healthFacilityUuid,
                                                           @QueryValue("formUuid") String formUuid,
                                                           @QueryValue("programmaticAreaUuid") String programmaticAreaUuid,
                                                           @QueryValue("tutorUuid") String tutorUuid,
                                                           @QueryValue("cabinetUuid") String cabinetUuid,
                                                           @QueryValue("startDate") Date startDate,
                                                           @QueryValue("endDate") Date endDate){

        return this.sessionService.findPerformedSessionsBySelectedFilterList(districtUuid, healthFacilityUuid, formUuid,  programmaticAreaUuid, tutorUuid, cabinetUuid, startDate, endDate);
    }

    @Get("performed-sessions-by-tutor-and-form")
    public List<PerformedSession> findPerformedSessionsByTutorAndForm(@QueryValue("tutorUuid") String tutorUuid,
                                                                      @QueryValue("formUuid") String formUuid,
                                                                      @QueryValue("startDate") Date startDate,
                                                                      @QueryValue("endDate") Date endDate){
        return this.sessionService.findPerformedSessionsByTutorAndForm(tutorUuid, formUuid, startDate, endDate);
    }

    @Get("performed-sessions-by-tutor")
    public List<PerformedSession> findPerformedSessionsByTutor(@QueryValue("tutorUuid") String tutorUuid,
                                                               @QueryValue("startDate") Date startDate,
                                                               @QueryValue("endDate") Date endDate){
        return this.sessionService.findPerformedSessionsByTutor(tutorUuid, startDate, endDate);
    }

    @Get("performed-sessions-hts")
    public List<PerformedSession> findPerformedSessionsHTS(@QueryValue("startDate") Date startDate,
                                                           @QueryValue("endDate") Date endDate){
        return this.sessionService.findPerformedSessionsBySelectedFilterHTS(startDate, endDate);
    }

    @Get("performed-sessions-narrative")
    public List<PerformedSession> findPerformedSessionsNarrative(@QueryValue("startDate") Date startDate,
                                                                 @QueryValue("endDate") Date endDate){
        return this.sessionService.findPerformedSessionsBySelectedFilterNarrative(startDate, endDate);
    }

    @Get("performed-sessions-months")
    public List<PerformedSession> findPerformedSessionsLast12Months(){
        return this.sessionService.findPerformedSessionsBySelectedFilterLast12Months();
    }

    @Get("performed-sessions-months/{tutoruuid}")
    public List<PerformedSession> findPerformedSessionsLast12Months(@PathVariable("tutoruuid") String tutoruuid){
        return this.sessionService.findPerformedSessionsBySelectedFilterLast12Months(tutoruuid);
    }

    @Get("performed-sessions-indicators")
    public List<PerformedSession> findPerformedSessionsIndicators(@QueryValue("startDate") Date startDate,
                                                                  @QueryValue("endDate") Date endDate){
        return this.sessionService.findPerformedSessionsBySelectedFilterIndicators(startDate, endDate);
    }

    @Get("performed-sessions-indicators-list")
    public List<PerformedSession> findPerformedSessionsIndicatorsList(@QueryValue("startDate") Date startDate,
                                                                      @QueryValue("endDate") Date endDate){
        return  this.sessionService.findPerformedSessionsBySelectedFilterIndicatorsList(startDate, endDate);
    }

    @Get("performed-sessions-hts-tutored")
    public List<PerformedSession> findPerformedSessionsHTS(@QueryValue("startDate") Date startDate,
                                                           @QueryValue("endDate") Date endDate,
                                                           @QueryValue("tutoredUuid") String tutoredUuid){
        return this.sessionService.findPerformedSessionsBySelectedFilterHTS(startDate, endDate);
    }

    @Get("performed-sessions-pmqtr")
    public List<PerformedSession> findPerformedSessionsPMQTR(@QueryValue("startDate") Date startDate,
                                                             @QueryValue("endDate") Date endDate){
        return this.sessionService.findPerformedSessionsBySelectedFilterPMQTR(startDate, endDate);

    }

    @Get("performed-sessions-narrative-cop20")
    public List<PerformedSession> findPerformedSessionsNarrativeCOP20(@QueryValue("startDate") Date startDate,
                                                                      @QueryValue("endDate") Date endDate){
        return this.sessionService.findPerformedSessionsBySelectedFilterNarrativeCOP20(startDate, endDate);
    }

    @Operation(summary = "Return a list of all Mentorships")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Mentorship")
    @Get("/getAllMentorshipSessionsOfMentor")
    public List<MentorshipDTO> getAllMentorshipSessionsOfMentor(@QueryValue("mentorId") Long mentorId) {
        List<MentorshipDTO> mentorships = this.mentorshipService.getAllMentorshipSessionsOfMentor(mentorId);
        return mentorships;
    }
}
