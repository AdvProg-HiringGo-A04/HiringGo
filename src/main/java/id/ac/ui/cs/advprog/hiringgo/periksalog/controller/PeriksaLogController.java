package id.ac.ui.cs.advprog.hiringgo.periksalog.controller;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.model.WebResponse;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.service.AuthenticationService;
import id.ac.ui.cs.advprog.hiringgo.periksalog.service.PeriksaLogService;
import id.ac.ui.cs.advprog.hiringgo.periksalog.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@Validated
public class PeriksaLogController {

    private static final String ACCESS_FORBIDDEN_MESSAGE = "Access forbidden: Only lecturers can access logs";
    private static final String UPDATE_FORBIDDEN_MESSAGE = "Access forbidden: Only lecturers can update log status";

    private final PeriksaLogService logService;
    private final AuthenticationService authenticationService;
    private final ResponseBuilder responseBuilder;

    @GetMapping("/async")
    public CompletableFuture<ResponseEntity<WebResponse<List<LogDTO>>>> getAllLogsAsync(
            @RequestHeader(name = "Authorization", required = false) String token) {

        log.info("Async: Attempting to fetch all logs with JWT token");

        try {
            User user = authenticationService.authenticateUser(token);
            log.info("Async: Attempting to fetch all logs for user: {}", responseBuilder.getUserIdSafely(user));

            if (!authenticationService.isDosenUser(user)) {
                log.warn("Async: Unauthorized access attempt by user: {} with role: {}",
                        responseBuilder.getUserIdSafely(user), responseBuilder.getUserRoleSafely(user));
                return CompletableFuture.completedFuture(responseBuilder.createForbiddenResponse(ACCESS_FORBIDDEN_MESSAGE));
            }

            return logService.getAllLogsByDosenIdAsync(user.getId())
                    .thenApply(responseBuilder::createSuccessResponse)
                    .exceptionally(this::handleAsyncException);
        } catch (Exception e) {
            log.error("Async: Authentication error", e);
            return CompletableFuture.completedFuture(responseBuilder.createUnauthorizedResponse(e.getMessage()));
        }
    }

    @PutMapping("/{logId}/status/async")
    public CompletableFuture<ResponseEntity<WebResponse<LogDTO>>> updateLogStatusAsync(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable("logId") @NotBlank String logId,
            @RequestBody @Valid LogStatusUpdateDTO logStatusUpdateDTO) {

        log.info("Async: Attempting to update log status for logId: {} with JWT token", logId);

        try {
            User user = authenticationService.authenticateUser(token);
            log.info("Async: Attempting to update log status for logId: {} by user: {}",
                    logId, responseBuilder.getUserIdSafely(user));

            if (!authenticationService.isDosenUser(user)) {
                log.warn("Async: Unauthorized status update attempt by user: {} with role: {} for logId: {}",
                        responseBuilder.getUserIdSafely(user), responseBuilder.getUserRoleSafely(user), logId);
                return CompletableFuture.completedFuture(responseBuilder.createForbiddenResponse(UPDATE_FORBIDDEN_MESSAGE));
            }

            logStatusUpdateDTO.setLogId(logId);

            return logService.updateLogStatusAsync(user.getId(), logStatusUpdateDTO)
                    .thenApply(updatedLog -> {
                        log.info("Async: Successfully updated log status for logId: {} to status: {}",
                                logId, logStatusUpdateDTO.getStatus());
                        return responseBuilder.createSuccessResponse(updatedLog);
                    })
                    .exceptionally(throwable -> responseBuilder.handleAsyncUpdateException(throwable, logId));
        } catch (Exception e) {
            log.error("Async: Authentication error for log update", e);
            return CompletableFuture.completedFuture(responseBuilder.createUnauthorizedResponse(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<WebResponse<List<LogDTO>>> getAllLogs(
            @RequestHeader(name = "Authorization", required = false) String token) {

        log.info("Attempting to fetch all logs with JWT token");

        try {
            User user = authenticationService.authenticateUser(token);
            log.info("Attempting to fetch all logs for user: {}", responseBuilder.getUserIdSafely(user));

            if (!authenticationService.isDosenUser(user)) {
                log.warn("Unauthorized access attempt by user: {} with role: {}",
                        responseBuilder.getUserIdSafely(user), responseBuilder.getUserRoleSafely(user));
                return responseBuilder.createForbiddenResponse(ACCESS_FORBIDDEN_MESSAGE);
            }

            List<LogDTO> logs = logService.getAllLogsByDosenId(user.getId());
            return responseBuilder.createSuccessResponse(logs);
        } catch (IllegalArgumentException e) {
            log.error("Authentication error", e);
            return responseBuilder.createUnauthorizedResponse(e.getMessage());
        } catch (Exception e) {
            return responseBuilder.handleException(e);
        }
    }

    @PutMapping("/{logId}/status")
    public ResponseEntity<WebResponse<LogDTO>> updateLogStatus(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable("logId") @NotBlank String logId,
            @RequestBody @Valid LogStatusUpdateDTO logStatusUpdateDTO) {

        log.info("Attempting to update log status for logId: {} with JWT token", logId);

        try {
            User user = authenticationService.authenticateUser(token);
            log.info("Attempting to update log status for logId: {} by user: {}",
                    logId, responseBuilder.getUserIdSafely(user));

            if (!authenticationService.isDosenUser(user)) {
                log.warn("Unauthorized status update attempt by user: {} with role: {} for logId: {}",
                        responseBuilder.getUserIdSafely(user), responseBuilder.getUserRoleSafely(user), logId);
                return responseBuilder.createForbiddenResponse(UPDATE_FORBIDDEN_MESSAGE);
            }

            logStatusUpdateDTO.setLogId(logId);

            LogDTO updatedLog = logService.updateLogStatus(user.getId(), logStatusUpdateDTO);
            log.info("Successfully updated log status for logId: {} to status: {}",
                    logId, logStatusUpdateDTO.getStatus());
            return responseBuilder.createSuccessResponse(updatedLog);
        } catch (IllegalArgumentException e) {
            log.error("Authentication error for log update", e);
            return responseBuilder.createUnauthorizedResponse(e.getMessage());
        } catch (SecurityException e) {
            log.warn("Security exception while updating log status for logId: {}", logId, e);
            return responseBuilder.createSecurityExceptionResponse(e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred while updating log status for logId: {}", logId, e);
            return responseBuilder.createInternalServerErrorResponseForLog(
                    responseBuilder.getErrorMessage(e, "An error occurred while updating log status"));
        }
    }

    private ResponseEntity<WebResponse<List<LogDTO>>> handleAsyncException(Throwable throwable) {
        log.error("Async error occurred while fetching logs", throwable);
        String errorMessage = responseBuilder.getErrorMessage(throwable, "An error occurred while fetching logs");
        return responseBuilder.createInternalServerErrorResponse(errorMessage);
    }
}