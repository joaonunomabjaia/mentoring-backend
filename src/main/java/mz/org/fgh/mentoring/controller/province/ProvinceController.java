package mz.org.fgh.mentoring.controller.province;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mz.org.fgh.mentoring.api.PaginatedResponse;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.province.ProvinceDTO;
import mz.org.fgh.mentoring.entity.location.Province;
import mz.org.fgh.mentoring.service.location.ProviceService;
import mz.org.fgh.mentoring.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.PROVINCE)
public class ProvinceController extends BaseController {

    public static final Logger LOG = LoggerFactory.getLogger(ProvinceController.class);

    private final ProviceService proviceService;

    public ProvinceController(ProviceService proviceService) {
        this.proviceService = proviceService;
    }

    @Operation(summary = "Return a list off all Provinces")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Province")
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("getall")
    public List<ProvinceDTO> getAll(@Nullable @QueryValue("limit") Long limit,
                                    @Nullable @QueryValue("offset") Long offset) {
        return this.proviceService.getAll(limit, offset);
    }

    @Operation(summary = "List or search provinces (paginated)")
    @Get
    public HttpResponse<?> listOrSearch(@Nullable @QueryValue("name") String name,
                                        @Nullable Pageable pageable) {

        Page<Province> data = !Utilities.stringHasValue(name)
                ? proviceService.findAll(resolvePageable(pageable))
                : proviceService.searchByName(name, resolvePageable(pageable));

        List<ProvinceDTO> dataDTOs = data.getContent().stream()
                .map(ProvinceDTO::new)
                .collect(Collectors.toList());

        String message = data.getTotalSize() == 0
                ? "Sem Dados para esta pesquisa"
                : "Dados encontrados";

        return HttpResponse.ok(
                PaginatedResponse.of(
                        dataDTOs,
                        data.getTotalSize(),
                        data.getPageable(),
                        message
                )
        );
    }
}
