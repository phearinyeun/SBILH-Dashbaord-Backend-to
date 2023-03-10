package com.sbilhbank.insur.service.dashboard;

import com.sbilhbank.insur.dto.DashboardDto;
import com.sbilhbank.insur.entity.primary.Dashboard;
import com.sbilhbank.insur.extras.request.Pagination;
import com.sbilhbank.insur.extras.request.RequestDashboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface DashboardService {
    Page<DashboardDto> get(Pageable pageable);
    DashboardDto getById(Long id);
    Dashboard Create (RequestDashboard dashboardDto);
    Dashboard Update (RequestDashboard dashboardDto, Long id);
    Dashboard delete (Long id);
}
