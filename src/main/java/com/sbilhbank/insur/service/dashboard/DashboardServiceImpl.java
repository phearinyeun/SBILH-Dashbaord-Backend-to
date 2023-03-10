package com.sbilhbank.insur.service.dashboard;

import com.sbilhbank.insur.dto.DashboardDto;
import com.sbilhbank.insur.extras.request.Pagination;
import com.sbilhbank.insur.extras.request.RequestDashboard;
import com.sbilhbank.insur.entity.primary.Dashboard;
import com.sbilhbank.insur.exception.NotFoundEntityException;
import com.sbilhbank.insur.mapper.DashboardMapper;
import com.sbilhbank.insur.repository.primary.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{
    private final DashboardRepository dashboardRepository;
    private final DashboardMapper dashboardMapper;

    @Value("${spring.root.upload-file}")
    private String rootPath;

    @Override
    public Page<DashboardDto> get(Pageable pageable) {
        pageable.getSortOr(Sort.by("id").ascending());
        Page<Dashboard> dashboards = dashboardRepository.findAll(pageable);
        return new PageImpl<DashboardDto>(
                dashboardMapper.mapDashboardsToDashboardsDto(dashboards.getContent()),
                pageable,
                dashboards.getTotalElements()
        );
    }

    @Override
    public DashboardDto getById(Long id) {
        return dashboardMapper.mapDashboardToDashboardDto(
                dashboardRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundEntityException(
                                MessageFormatter
                                    .format("Dashboard doesn't exist with id {}", id)
                                    .getMessage()
                        )
                )
        );
    }

    @Override
    public Dashboard Create(RequestDashboard dashboardDto) {
        Dashboard dashboard = dashboardMapper.mapDashboardDtoToDashboard(dashboardDto.getDashboardDto());
        dashboardRepository.save(dashboard);
        deleteFiles(Arrays.asList(dashboardDto.getDeleteFiles()),
                new String[]{dashboard.getIcon_file(),
                        dashboard.getGuideline_file()});
        return dashboard;
    }

    @Override
    public Dashboard Update(RequestDashboard dashboardDto, Long id) {
        Dashboard dashboardUpdate = dashboardMapper.mapDashboardDtoToDashboard(getById(id));
        Dashboard dashboard = dashboardMapper.mapDashboardDtoToDashboard(dashboardDto.getDashboardDto());
        List<String> deleteFiles = new ArrayList<>(Arrays.asList(dashboardDto.getDeleteFiles()));
        deleteFiles.add(dashboardUpdate.getIcon_file());
        deleteFiles.add(dashboardUpdate.getGuideline_file());
        dashboardUpdate = dashboard;
        dashboardUpdate.setId(id);
        dashboardRepository.save(dashboardUpdate);

        deleteFiles(deleteFiles, new String[]{ dashboardUpdate.getIcon_file(),dashboardUpdate.getGuideline_file() });
        return dashboardUpdate;
    }

    @Override
    public Dashboard delete(Long id) {
        Dashboard dashboard = dashboardMapper.mapDashboardDtoToDashboard(getById(id));
        String[] deleteFiles = new String[]{dashboard.getIcon_file(), dashboard.getGuideline_file()};
        dashboardRepository.delete(dashboard);
        deleteFiles(Arrays.asList(deleteFiles), new String[]{""});
        return dashboard;
    }
    private void deleteFiles (List<String> deleteFiles, String[] successFiles){
        for (String deleteFile: deleteFiles) {
            Path filePath = Paths.get(rootPath, deleteFile);
            if(Files.exists(filePath) && Arrays.stream(successFiles).noneMatch(deleteFile::equals)){
                try{
                    Files.delete(filePath);
                }catch (Exception ignored){

                }
            }
        }
    }
}
