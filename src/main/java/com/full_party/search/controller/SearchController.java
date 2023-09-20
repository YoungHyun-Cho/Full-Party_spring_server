package com.full_party.search.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/search")
public class SearchController {
    // # 검색 관련 기능
    // 키워드 & 태그 검색
    @GetMapping("/{region}")
    public ResponseEntity searchQuest(@PathVariable("region") String region,
                                      @RequestParam(name = "type") String type,
                                      @RequestParam(name = "keyword") String keyword,
                                      @RequestParam(name = "tag") String tag) {

        return new ResponseEntity(HttpStatus.OK);
    }
}
