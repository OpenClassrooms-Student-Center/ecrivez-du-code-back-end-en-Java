package com.hamac;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest
@AutoConfigureMockMvc
class LeaveApprovalTests {

    @Autowired
    MockMvcTester mvc;

    @Test
    void approvesRequestOfPermanentEmployee() {
        assertThat(mvc.get().uri("/api/leave/approve?id=1"))
                .hasStatusOk()
                .bodyText().isEqualTo("Approved");
    }

    @Test
    void balanceOfFixedTermEmployeeCountsOnlyFullMonths() throws Exception {
        var result = mvc.get().uri("/api/leave/balance?employeeId=4").exchange();

        var balance = Double.parseDouble(result.getResponse().getContentAsString());
        assertThat(balance).isCloseTo(4.56, within(0.01));
    }
}
