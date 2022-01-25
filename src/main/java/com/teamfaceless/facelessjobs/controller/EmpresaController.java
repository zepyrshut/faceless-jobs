package com.teamfaceless.facelessjobs.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.teamfaceless.facelessjobs.dtos.empresa.EmpresaListadoDto;
import com.teamfaceless.facelessjobs.dtos.empresa.EmpresaRegistroDto;
import com.teamfaceless.facelessjobs.dtos.empresa.mapper.IEmpresaMapper;
import com.teamfaceless.facelessjobs.dtos.inscripcion.InscripcionOfertaInscritoDto;
import com.teamfaceless.facelessjobs.model.Empresa;
import com.teamfaceless.facelessjobs.model.OfertaEmpleo;
import com.teamfaceless.facelessjobs.services.IEmpresaService;
import com.teamfaceless.facelessjobs.services.IInscriptionService;
import com.teamfaceless.facelessjobs.services.IOfertaService;
import com.teamfaceless.facelessjobs.services.IProvinciaService;
import com.teamfaceless.facelessjobs.services.ISectorService;
import com.teamfaceless.facelessjobs.validations.IValidations;

@Controller
@RequestMapping("/app/empresa")
public class EmpresaController {

	@Autowired
	private IEmpresaService iEmpresaService;

	@Autowired
	private IProvinciaService iProvinciaService;
	
	@Autowired
	private ISectorService iSectorService;
	
	@Autowired
	private IEmpresaMapper iEmpresaMapper;
	
	@Autowired
	private IOfertaService iOfertaService;
	
	@Autowired
	private IInscriptionService iInscriptionService;
	
	@GetMapping("/home")
	public String home() {
		return "views/app/empresa/home";
	}

	@GetMapping("/pruebas/{idEmpresa}")
	public String goPruebas(@PathVariable Integer idEmpresa, Model model) {
		model.addAttribute("provincias", iProvinciaService.findAll());
		model.addAttribute("sectores", iSectorService.findAll());
		model.addAttribute("empresa", iEmpresaService.findById(idEmpresa));
		return "views/app/empresa/pruebas";
	}
	
	@PostMapping("/pruebas")
	public String postPruebas(Empresa empresa, Model model, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("empresa", empresa);
			model.addAttribute("provincias", iProvinciaService.findAll());
			model.addAttribute("sectores", iSectorService.findAll());
			return "views/app/empresa/pruebas";
		}
		iEmpresaService.modify(empresa);
		return "redirect:/app/empresa/listado";
	}
	
	@GetMapping("/perfil")
	public String goPerfil(Model model) {
		model.addAttribute("titulo", "Perfil de Empresa");
		return "views/app/empresa/perfil";
	}
	
	@GetMapping("/listado")
	public String goListado(Model model) {
		model.addAttribute("listEmpresas", iEmpresaService.findAll());
		return "/views/app/empresa/listado";
	}
	
	@GetMapping("/modificar/{idEmpresa}")
	public String goModificar(@PathVariable Integer idEmpresa, Model model) {
		model.addAttribute("empresa", iEmpresaService.findById(idEmpresa).get());
//		model.addAttribute("empresaregistrodto", new EmpresaRegistroDto());
		model.addAttribute("provincias", iProvinciaService.findAll());
		model.addAttribute("sectores", iSectorService.findAll());
		return "/views/app/empresa/detalle";
	}
	
	@PostMapping("/modificar")
	public String modificarEmpresa(Model model, @Valid Empresa empresa, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("empresa", empresa);
			model.addAttribute("provincias", iProvinciaService.findAll());
			model.addAttribute("sectores", iSectorService.findAll());
			return "/views/app/empresa/detalle";
		}
		Empresa empresaAnterior = iEmpresaService.findById(empresa.getIdEmpresa()).get();
		empresa.setOfertasEmpleos(empresaAnterior.getOfertasEmpleos());
		empresa.setCredencial(empresaAnterior.getCredencial());
		iEmpresaService.modify(empresa);
		return "redirect:/app/empresa/listado";
	}
	
	@GetMapping("/borrar/{idEmpresa}")
	public String formBaja(@PathVariable Integer idEmpresa) {
		iEmpresaService.delete(idEmpresa);
		return "redirect:/app/empresa/listado";
	}
	
	@PostMapping("/borrar/{idEmpresa}")
	public String bajaEmpresa(@PathVariable Integer idEmpresa) {
		iEmpresaService.delete(idEmpresa);
		return "redirect:/app/empresa/listado";
	}
	
	@GetMapping("/inscritos/{idOferta}")
	public String goInscritos(@PathVariable Integer idOferta, Model model) {
		
		//TODO esto sobra
		model.addAttribute("provincias", iProvinciaService.findAll());
		model.addAttribute("sectores", iSectorService.findAll());
		
		OfertaEmpleo ofertaPrueba = iOfertaService.findById(idOferta).get();
		//Pasamos Oferta para pruebas
		model.addAttribute("oferta", iOfertaService.findById(idOferta).get());
		
		//TODO
		List<InscripcionOfertaInscritoDto> inscritos = iInscriptionService.inscritosOfertaConHabilidades(ofertaPrueba);
		model.addAttribute("inscritos", inscritos);
		return "views/app/empresa/inscritos";
	}
		
	@GetMapping("/pruebasValidation")
	private String goPruebasValidation(Model model) {
		model.addAttribute("empresa", new EmpresaListadoDto());
		return "/views/app/empresa/pruebasValidation";
	}
	
	@PostMapping("/pruebasValidation")
	private String goPruebasValidationReturn(Model model,@Valid EmpresaListadoDto empresaListadoDto, BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("empresa", empresaListadoDto);
			return "/views/app/empresa/pruebasValidation";
		}
		return "redirect:/empresa/listado";
	}
}
