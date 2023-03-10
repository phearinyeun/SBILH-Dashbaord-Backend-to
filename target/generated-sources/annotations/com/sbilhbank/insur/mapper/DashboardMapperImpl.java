package com.sbilhbank.insur.mapper;

import com.sbilhbank.insur.dto.DashboardDto;
import com.sbilhbank.insur.entity.primary.Dashboard;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-10T14:34:20+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class DashboardMapperImpl implements DashboardMapper {

    @Override
    public Dashboard mapDashboardDtoToDashboard(DashboardDto dashboardDto) {
        if ( dashboardDto == null ) {
            return null;
        }

        Dashboard dashboard = new Dashboard();

        dashboard.setName( dashboardDto.name );
        dashboard.setUrl( dashboardDto.url );
        dashboard.setResponse_Name( dashboardDto.response_Name );
        dashboard.setDescription( dashboardDto.description );
        dashboard.setIcon_file( dashboardDto.icon_file );
        dashboard.setGuideline_file( dashboardDto.guideline_file );

        return dashboard;
    }

    @Override
    public DashboardDto mapDashboardToDashboardDto(Dashboard dashboard) {
        if ( dashboard == null ) {
            return null;
        }

        DashboardDto dashboardDto = new DashboardDto();

        dashboardDto.name = dashboard.getName();
        dashboardDto.url = dashboard.getUrl();
        dashboardDto.response_Name = dashboard.getResponse_Name();
        dashboardDto.description = dashboard.getDescription();
        dashboardDto.icon_file = dashboard.getIcon_file();
        dashboardDto.guideline_file = dashboard.getGuideline_file();

        return dashboardDto;
    }

    @Override
    public List<Dashboard> mapDashboardsDtoToDashboards(List<DashboardDto> dashboardDtoList) {
        if ( dashboardDtoList == null ) {
            return null;
        }

        List<Dashboard> list = new ArrayList<Dashboard>( dashboardDtoList.size() );
        for ( DashboardDto dashboardDto : dashboardDtoList ) {
            list.add( mapDashboardDtoToDashboard( dashboardDto ) );
        }

        return list;
    }

    @Override
    public List<DashboardDto> mapDashboardsToDashboardsDto(List<Dashboard> dashboards) {
        if ( dashboards == null ) {
            return null;
        }

        List<DashboardDto> list = new ArrayList<DashboardDto>( dashboards.size() );
        for ( Dashboard dashboard : dashboards ) {
            list.add( mapDashboardToDashboardDto( dashboard ) );
        }

        return list;
    }
}
