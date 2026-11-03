package com.hamac;

import static org.assertj.core.api.Assertions.assertThat;

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
}
