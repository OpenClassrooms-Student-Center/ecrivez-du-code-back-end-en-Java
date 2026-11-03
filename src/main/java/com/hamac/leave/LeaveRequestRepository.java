package com.hamac.leave;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

interface LeaveRequestRepository extends Repository<LeaveRequest, Long> {

    Optional<LeaveRequest> findById(Long id);

    List<LeaveRequest> findAll(Sort sort);

    List<LeaveRequest> findByStatus(String status, Sort sort);

    LeaveRequest save(LeaveRequest request);
}
