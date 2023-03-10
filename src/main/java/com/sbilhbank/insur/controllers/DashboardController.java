package com.sbilhbank.insur.controllers;


import com.sbilhbank.insur.extras.request.RequestDashboard;
import com.sbilhbank.insur.extras.response.Response;
import com.sbilhbank.insur.service.dashboard.DashboardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardServiceImpl dashboardService;

    @GetMapping
    public Response<?> getAll(Pageable pageable){
        return Response.ok(dashboardService.get(pageable));
    }

    @PostMapping
    public Response<?> create(@RequestBody RequestDashboard dashboardDto){
        return Response.ok(dashboardService.Create(dashboardDto));
    }

    @GetMapping("{/id}")
    public Response<?> getById(@PathVariable Long id){
        return Response.ok(dashboardService.getById(id));
    }

    @GetMapping("/update")
    public Response<?> update (@RequestBody RequestDashboard dashboardDto, @PathVariable Long id){
        return Response.ok(dashboardService.Update(dashboardDto, id));
    }

}
