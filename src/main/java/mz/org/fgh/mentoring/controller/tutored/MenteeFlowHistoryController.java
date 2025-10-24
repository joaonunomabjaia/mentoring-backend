package mz.org.fgh.mentoring.controller.tutored;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mz.org.fgh.mentoring.api.PaginatedResponse;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.SuccessResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.tutored.MenteeFlowHistoryDTO;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.enums.EnumFlowHistoryProgressStatus;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.service.tutored.MenteeFlowHistoryService;
import mz.org.fgh.mentoring.service.user.UserService;
import mz.org.fgh.mentoring.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.MENTEE_FLOW_HISTORIES)
public class MenteeFlowHistoryController extends BaseController {

    private final MenteeFlowHistoryService menteeFlowHistoryService;
    private static final Logger LOG = LoggerFactory.getLogger(MenteeFlowHistoryController.class);
    private final UserService userService;

    public MenteeFlowHistoryController(MenteeFlowHistoryService menteeFlowHistoryService, UserService userService) {
        this.menteeFlowHistoryService = menteeFlowHistoryService;
        this.userService = userService;
    }

    @Operation(summary = "Listar histórico de progresso de mentorandos (filtrável e paginado)")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "MenteeFlowHistory")
    @Get("/paginated")
    public HttpResponse<?> listPaginated(
            @Nullable @QueryValue("menteeName") String menteeName,
            @Nullable @QueryValue("progressStatus") String progressStatus,
            @Nullable @QueryValue("flowHistoryName") String flowHistoryName,
            @Nullable @QueryValue("startDate") String startDateStr,
            @Nullable @QueryValue("endDate") String endDateStr,
            @Nullable Pageable pageable
    ) {
        try {
            Date startDate = null;
            Date endDate = null;

            if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                startDate = DateUtils.createDate(startDateStr, "yyyy-MM-dd");
            }

            if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                endDate = DateUtils.createDate(endDateStr, "yyyy-MM-dd");
            }

            Page<MenteeFlowHistoryDTO> data = menteeFlowHistoryService.findFiltered(
                    menteeName,
                    progressStatus,
                    flowHistoryName,
                    startDate,
                    endDate,
                    resolvePageable(pageable)
            );

            String message = data.getTotalSize() == 0
                    ? "Sem dados para os filtros aplicados"
                    : "Dados encontrados com sucesso";

            return HttpResponse.ok(
                    PaginatedResponse.of(
                            data.getContent(),
                            data.getTotalSize(),
                            data.getPageable(),
                            message
                    )
            );

        } catch (Exception e) {
            LOG.error("Erro ao listar MenteeFlowHistory: {}", e.getMessage(), e);
            return HttpResponse.badRequest().body(
                    MentoringAPIError.builder()
                            .status(HttpStatus.BAD_REQUEST.getCode())
                            .error("Erro ao filtrar histórico de mentorandos")
                            .message(e.getMessage())
                            .build()
            );
        }
    }


    @Operation(summary = "Criar novo histórico de progresso de mentorando")
    @Post
    public HttpResponse<?> create(@Body MenteeFlowHistoryDTO dto, Authentication authentication) {
        try {
            String userUuid = (String) authentication.getAttributes().get("useruuid");
            User user = userService.findByUuid(userUuid);
            MenteeFlowHistory entity = new MenteeFlowHistory(dto);
            entity.setCreatedBy(userUuid);

            MenteeFlowHistory created = menteeFlowHistoryService.save(entity, user);

            return HttpResponse.created(
                    SuccessResponse.of(
                            "Histórico de progresso criado com sucesso",
                            new MenteeFlowHistoryDTO(created)
                    )
            );

        } catch (Exception e) {
            LOG.error("Erro ao criar MenteeFlowHistory: {}", e.getMessage(), e);
            return HttpResponse.serverError(
                    MentoringAPIError.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.getCode())
                            .error("Falha ao criar histórico")
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "Atualizar histórico existente de progresso de mentorando")
    @Put
    public HttpResponse<?> update(@Body MenteeFlowHistoryDTO dto, Authentication authentication) {
        try {
            String userUuid = (String) authentication.getAttributes().get("useruuid");
            MenteeFlowHistory entity = new MenteeFlowHistory(dto);
            entity.setUpdatedBy(userUuid);

            MenteeFlowHistory updated = menteeFlowHistoryService.update(entity);

            return HttpResponse.ok(
                    SuccessResponse.of(
                            "Histórico de progresso atualizado com sucesso",
                            new MenteeFlowHistoryDTO(updated)
                    )
            );

        } catch (Exception e) {
            LOG.error("Erro ao atualizar MenteeFlowHistory: {}", e.getMessage(), e);
            return HttpResponse.badRequest().body(
                    MentoringAPIError.builder()
                            .status(HttpStatus.BAD_REQUEST.getCode())
                            .error("Falha ao atualizar histórico")
                            .message(e.getMessage())
                            .build()
            );
        }
    }

//    @Operation(summary = "Listar rondas/sessões concluídas")
//    @Get("/completed")
//    public HttpResponse<?> listCompletedRondasMentoria() {
//        try {
//            List<MenteeFlowHistoryDTO> data = menteeFlowHistoryService.findCompletedRondaMentoria()
//                    .stream()
//                    .map(MenteeFlowHistoryDTO::new)
//                    .collect(Collectors.toList());
//
//            return HttpResponse.ok(
//                    SuccessResponse.of(
//                            "Rondas/Sessões concluídas listadas com sucesso",
//                            data
//                    )
//            );
//        } catch (Exception e) {
//            LOG.error("Erro ao listar rondas concluídas: {}", e.getMessage(), e);
//            return HttpResponse.serverError(
//                    MentoringAPIError.builder()
//                            .status(HttpStatus.INTERNAL_SERVER_ERROR.getCode())
//                            .error("Erro ao listar rondas concluídas")
//                            .message(e.getMessage())
//                            .build()
//            );
//        }
//    }
}
