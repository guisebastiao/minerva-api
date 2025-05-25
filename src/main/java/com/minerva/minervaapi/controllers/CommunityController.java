package com.minerva.minervaapi.controllers;

import com.minerva.minervaapi.controllers.dtos.DefaultDTO;
import com.minerva.minervaapi.services.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @GetMapping
    public ResponseEntity<DefaultDTO> findAllPublicCollections(@RequestParam(name = "search", required = false, defaultValue = "") String search, @RequestParam(name = "order", required = false, defaultValue = "") String order, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "20") int limit) {
        DefaultDTO response = this.communityService.findAllPublicCollections(search, order, offset, limit);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}