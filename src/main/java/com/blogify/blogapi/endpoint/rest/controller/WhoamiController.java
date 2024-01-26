package com.blogify.blogapi.endpoint.rest.controller;

import com.blogify.blogapi.endpoint.mapper.WhoamiMapper;
import com.blogify.blogapi.endpoint.rest.model.Whoami;
import com.blogify.blogapi.service.WhoamiService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class WhoamiController {
  private final WhoamiService service;
  private final WhoamiMapper mapper;

  @GetMapping(value = "/whoami")
  public Whoami whoami() {
    var whoami = service.whoami();
    return mapper.toRest(whoami);
  }
}
