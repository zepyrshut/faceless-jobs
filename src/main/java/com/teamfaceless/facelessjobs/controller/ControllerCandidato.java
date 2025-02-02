package com.teamfaceless.facelessjobs.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.teamfaceless.facelessjobs.dtos.candidato.CandidatoModifyDto;
import com.teamfaceless.facelessjobs.dtos.candidato.CandidatoRegistroDto;
import com.teamfaceless.facelessjobs.dtos.candidato.mapper.ICandidatoMapper;
import com.teamfaceless.facelessjobs.exceptions.EmailExisteException;
import com.teamfaceless.facelessjobs.model.Candidato;
import com.teamfaceless.facelessjobs.model.Credencial;
import com.teamfaceless.facelessjobs.services.ICandidatoService;
import com.teamfaceless.facelessjobs.services.IProvinciaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/candidato")
public class ControllerCandidato {

	@Autowired
	private ICandidatoService candidatoService;
	@Autowired
	private ICandidatoMapper candidatoMapper;
	@Autowired
	private IProvinciaService iProvinciaService;

	@Autowired
	private HttpSession httpSession;
	private Model addAttribute;

	@GetMapping("/registro")
	public String formRegistro(Model model, CandidatoRegistroDto candidatoRegistroDto) {
		model.addAttribute("candidato", candidatoRegistroDto);
		model.addAttribute("provincias", iProvinciaService.findAll());
		return "views/candidato/registro";
	}

	@PostMapping("/registro")
	public String registrar(@Valid @ModelAttribute("candidato") CandidatoRegistroDto candidatoRegistroDto,
			BindingResult result, Model model, RedirectAttributes redirect) {

		model.addAttribute("provincias", iProvinciaService.findAll());
		if (result.hasErrors()) {
			System.out.println("HAY ERRORES");
			return "views/candidato/registro";
		}
		// validacion pass coinciden y email coincide
		if (!candidatoRegistroDto.emailsEquals()) {
			model.addAttribute("errorEmail", "Los email no coinciden");// Preparar mensaje para internacionalizar
			return "views/candidato/registro";
		}
		if (!candidatoRegistroDto.passEquals()) {
			model.addAttribute("errorPass", "Las contraseñas no coinciden");// Preparar mensaje para internacionalizar
			return "views/candidato/registro";
		}

		// registrar
		System.out.println("NO HAY ERRORES");
		Candidato candidato = candidatoMapper.candidatoRegistroDtoToCandidato(candidatoRegistroDto);
		// encriptar pass con security
		try {
			candidatoService.create(candidato);
			redirect.addFlashAttribute("msg", "Registrado correctamente");// Preparar mensaje para internacionalizar
			return "redirect:/candidato/login";
		} catch (EmailExisteException e) {
			model.addAttribute("error", e.getMessage());// Preparar mensaje para internacionalizar
			return "views/candidato/registro";
		}

	}

	@RequestMapping(value="/perfil",method= {RequestMethod.GET,RequestMethod.POST})
	public String goToCandidateProfile(Model model) {

		Candidato candidato = (Candidato) httpSession.getAttribute("userSession");

		model.addAttribute("idCandidato", candidato.getIdCandidato());

		

		return "views/app/candidato/perfil";
	}

	@GetMapping("/modify")
	public String goToCandidateModify(Model model, CandidatoModifyDto candidatoModifyDto) {

		model.addAttribute("sessionCandidato", httpSession.getAttribute("userSession"));

		return "views/app/candidato/modify";
	}

	@PostMapping("/modify")
	public String candidateModifyData(@Valid @ModelAttribute("candidato") CandidatoModifyDto candidatoModifyDto,
			BindingResult result, RedirectAttributes redirect, Model model) {

		if (result.hasErrors()) {
			System.out.println("HAY ERRORES");
			model.addAttribute("sessionCandidato", candidatoModifyDto);
			return "views/app/candidato/modify";
		}

		Candidato candidatoTemp = (Candidato) httpSession.getAttribute("userSession");
		Credencial credencial = candidatoTemp.getCredencial();
		candidatoModifyDto.setCredencial(credencial);
		candidatoModifyDto.setIdCandidato(candidatoTemp.getIdCandidato());

		Candidato candidato = candidatoMapper.candidatoModifyDtoToCandidato(candidatoModifyDto);
		candidatoService.update(candidato);

		httpSession.setAttribute("userSession", candidato);
		redirect.addFlashAttribute("msg", "Modificado correctamente");
		return "redirect:/candidato/perfil";

	}
	
	@RequestMapping(value="/detalle/{idCandidato}",method={RequestMethod.GET,RequestMethod.POST})
	public String detalleCandidato(@PathVariable Integer idCandidato,Model model) {
		model.addAttribute("candidato", candidatoService.buscarPorId(idCandidato).get());
		return "/views/candidato/detalle";
	}
}
