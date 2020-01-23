package br.com.sismed.mongodb.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.sismed.mongodb.domain.Convenio;
import br.com.sismed.mongodb.service.ConvenioService;

@Controller
@RequestMapping("/convenios")
public class ConvenioController {

	@Autowired
	private ConvenioService convenio_service;
	
	@GetMapping
	public ResponseEntity<List<Convenio>> findAll(){
		List<Convenio>  lista = convenio_service.findAll();
		
		return ResponseEntity.ok().body(lista);
	}
	
}
