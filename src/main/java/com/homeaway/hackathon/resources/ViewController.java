package com.homeaway.hackathon.resources;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class ViewController {

	@RequestMapping(value="/",method = RequestMethod.GET)
	public String hello(){
		log.info("Redirecting to index.html.");
		return "index";
	}
}
