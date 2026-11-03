package com.hamac;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.hamac.employee.Employee;
import com.hamac.leave.LeaveNotifier;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LeaveApprovalTests {

    @Autowired
    MockMvcTester mvc;

    @MockitoBean
    LeaveNotifier notifier;

    @Test
    void approvesRequestOfPermanentEmployee() {
        assertThat(mvc.post().uri("/api/leave-requests/1/approval"))
                .hasStatusOk();
    }

    @Test
    void notifiesEmployeeWhenRequestIsApproved() {
        assertThat(mvc.post().uri("/api/leave-requests/1/approval")).hasStatusOk();

        verify(notifier).notify(any(Employee.class), eq("Votre demande de congés est approuvée."));
    }

    @Test
    void balanceOfFixedTermEmployeeCountsOnlyFullMonths() throws Exception {
        var result = mvc.get().uri("/api/employees/4/balance").exchange();

        double balance = JsonPath.read(result.getResponse().getContentAsString(), "$.balance");
        assertThat(balance).isCloseTo(4.56, within(0.01));
    }

    @Test
    void approvingRequestTwiceReturnsConflict() {
        assertThat(mvc.post().uri("/api/leave-requests/1/approval")).hasStatusOk();

        assertThat(mvc.post().uri("/api/leave-requests/1/approval"))
                .hasStatus(HttpStatus.CONFLICT);
    }

    @Test
    void creatingRequestEndingBeforeItStartsReturnsBadRequest() {
        assertThat(mvc.post().uri("/api/leave-requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"employeeId": 4, "startDate": "2026-07-10", "endDate": "2026-07-06"}
                        """))
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void balanceOfInternIsAvailableBeforeArrival() throws Exception {
        var result = mvc.get().uri("/api/employees/6/balance").exchange();

        assertThat(result).hasStatusOk();
        double balance = JsonPath.read(result.getResponse().getContentAsString(), "$.balance");
        assertThat(balance).isEqualTo(0.0);
    }
}
