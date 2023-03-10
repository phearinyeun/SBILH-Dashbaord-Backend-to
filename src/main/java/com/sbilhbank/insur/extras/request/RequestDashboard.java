package com.sbilhbank.insur.extras.request;

import com.sbilhbank.insur.dto.DashboardDto;
import com.sbilhbank.insur.entity.primary.Dashboard;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDashboard {
    DashboardDto dashboardDto;
    String[] deleteFiles;
}

