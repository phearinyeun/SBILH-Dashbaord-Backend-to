package com.sbilhbank.insur.mapper;

import com.sbilhbank.insur.dto.DashboardDto;
import com.sbilhbank.insur.entity.primary.Dashboard;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DashboardMapper {
    Dashboard mapDashboardDtoToDashboard(DashboardDto dashboardDto);
    DashboardDto mapDashboardToDashboardDto(Dashboard dashboard);
    List<Dashboard> mapDashboardsDtoToDashboards(List<DashboardDto> dashboardDtoList);
    List<DashboardDto> mapDashboardsToDashboardsDto(List<Dashboard> dashboards);
}
