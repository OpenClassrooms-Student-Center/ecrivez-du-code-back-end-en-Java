package com.hamac.leave;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/leave-requests")
class LeaveRequestsController {

    private final LeaveService leaveService;

    LeaveRequestsController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping
    List<LeaveRequestDto> getAll(@RequestParam(required = false) LeaveStatus status) {
        return leaveService.getRequests(status).stream()
                .map(LeaveRequestDto::from)
                .toList();
    }

    @GetMapping("/{id}")
    LeaveRequestDto getById(@PathVariable long id) {
        return leaveService.getRequest(id)
                .map(LeaveRequestDto::from)
                .orElseThrow(() -> notFound());
    }

    @PostMapping
    ResponseEntity<LeaveRequestDto> create(@Valid @RequestBody CreateLeaveRequestDto dto) {
        var request = leaveService.create(dto.employeeId(), dto.startDate(), dto.endDate())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Ce salarié n’existe pas."));
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(request.getId())
                .toUri();
        return ResponseEntity.created(location).body(LeaveRequestDto.from(request));
    }

    @PostMapping("/{id}/approval")
    LeaveRequestDto approve(@PathVariable long id) {
        return switch (leaveService.approve(id)) {
            case APPROVED, INSUFFICIENT_BALANCE -> getById(id);
            case NOT_FOUND -> throw notFound();
            case NOT_PENDING -> throw alreadyProcessed();
        };
    }

    @PostMapping("/{id}/refusal")
    LeaveRequestDto refuse(@PathVariable long id) {
        return switch (leaveService.refuse(id)) {
            case REFUSED -> getById(id);
            case NOT_FOUND -> throw notFound();
            case NOT_PENDING -> throw alreadyProcessed();
        };
    }

    private static ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Cette demande n’existe pas.");
    }

    private static ResponseStatusException alreadyProcessed() {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Cette demande a déjà été traitée.");
    }
}
