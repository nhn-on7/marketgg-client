package com.nhnacademy.marketgg.client.web;

import com.nhnacademy.marketgg.client.dto.response.PointRetrieveResponse;
import com.nhnacademy.marketgg.client.service.PointService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/shop/v1/admin/points")
@RequiredArgsConstructor
public class AdminPointController {

    private final PointService pointService;

    @GetMapping
    public ModelAndView adminRetrievePointHistory() {
        List<PointRetrieveResponse> responses = this.pointService.adminRetrievePointHistories();

        ModelAndView mav = new ModelAndView("/points/admin-retrieve-members");
        mav.addObject("points", responses);

        return mav;
    }
}
