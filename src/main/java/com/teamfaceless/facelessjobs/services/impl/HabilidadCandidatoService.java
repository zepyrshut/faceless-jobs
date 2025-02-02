package com.teamfaceless.facelessjobs.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamfaceless.facelessjobs.dao.IHabilidadCandidatoRepository;
import com.teamfaceless.facelessjobs.model.Candidato;
import com.teamfaceless.facelessjobs.model.Habilidad;
import com.teamfaceless.facelessjobs.model.HabilidadCandidato;
import com.teamfaceless.facelessjobs.services.IHabilidadCandidatoService;
import com.teamfaceless.facelessjobs.services.IHabilidadService;

@Service
public class HabilidadCandidatoService implements IHabilidadCandidatoService{

	@Autowired
	private IHabilidadCandidatoRepository repository;
	
	@Autowired
	private IHabilidadService habService;

	@Override
	public void modify(HabilidadCandidato habilidadCandidato) {
		repository.save(habilidadCandidato);		
	}
	
	@Override
	@Transactional
	public void delete(HabilidadCandidato habilidadCandidato) {
		Candidato candidato = habilidadCandidato.getCandidato();
		Habilidad habilidad = habilidadCandidato.getHabilidad();
		repository.deleteHabilidadCandidato(candidato, habilidad);
	}
	
	@Override
	public List<Habilidad> findHabilidadesDurasRestantesByCandidato(Candidato candidato) {
		List<Integer> listaId = repository.findHabilidadesDurasRestantesByCandidato(candidato.getIdCandidato());
		List<Habilidad> listaHabilidades = new ArrayList<>();
		for(Integer id : listaId) {
			Habilidad nuevaHabilidad = habService.findById(id).get();
			listaHabilidades.add(nuevaHabilidad);
		}
		return listaHabilidades;
	}

	@Override
	public List<Habilidad> findHabilidadesBlandasRestantesByCandidato(Candidato candidato) {
		List<Integer> listaId = repository.findHabilidadesBlandasRestantesByCandidato(candidato.getIdCandidato());
		List<Habilidad> listaHabilidades = new ArrayList<>();
		for(Integer id : listaId) {
			Habilidad nuevaHabilidad = habService.findById(id).get();
			listaHabilidades.add(nuevaHabilidad);
		}
		return listaHabilidades;
	}

	@Override
	public List<HabilidadCandidato> findHabilidadesCandidatoDurasByCandidato(Candidato candidato) {
		return repository.findHabilidadesCandidatoDurasByCandidato(candidato.getIdCandidato());
	}

	@Override
	public List<HabilidadCandidato> findHabilidadesCandidatoBlandasByCandidato(Candidato candidato) {
		return repository.findHabilidadesCandidatoBlandasByCandidato(candidato.getIdCandidato());
	}
	
	@Override
	public HabilidadCandidato findHabilidadCandidatoByCandidatoAndHabilidad(Candidato candidato, Habilidad habilidad) {
		return repository.findHabilidadCandidatoByCandidatoAndHabilidad(candidato, habilidad);
	}

	/**
	 * @author Mefisto
	 * Devuelve una lista de Habilidad a partir de las habilidades del candidato
	 * @param habilidadesCandidato
	 * @return Lista Habilidad
	 */
	@Override
	public List<Habilidad> generalizacionHabilidadesCandidato(List<HabilidadCandidato> habilidadesCandidato) {

		//Desmonto en habilidades simples las habilidades del candidato
		List<Habilidad> habilidadCandidatoSimple = new ArrayList<>();
		for (HabilidadCandidato habCand  : habilidadesCandidato) {
			habilidadCandidatoSimple.add(habCand.getHabilidad());
		}
		return habilidadCandidatoSimple;
	}

	/**
	 * @author Mefisto
	 * Devuelve una lista de HabilidadCandidato COINCIDENTES a partir de una lista de Habilidad genérica
	 * @param habilidades
	 * @param candidato
	 * @return Lista de HabilidadCandidato 
	 */
	@Override
	public List<HabilidadCandidato> especializacionHabilidadesCandidatoCoincidentes(List<Habilidad> habilidades, Candidato candidato) {
		
		List<HabilidadCandidato> habilidadesCandidato = candidato.getHabilidadCandidatoList();
		List<HabilidadCandidato> habilidadesComprobadas = new ArrayList<>();
		
		for (Habilidad habilidad : habilidades) {
			for (HabilidadCandidato habCand : habilidadesCandidato) {
				if (habCand.getHabilidad().equals(habilidad)) {
					habilidadesComprobadas.add(habCand);
					break;
				}
			}
		}
		return habilidadesComprobadas;
	}
	
	public List<HabilidadCandidato> especializacionHabilidadesCandidatoRellenos(List<Habilidad> habilidades, Candidato candidato) {
		
		List<HabilidadCandidato> habilidadesCandidato = candidato.getHabilidadCandidatoList();
		List<HabilidadCandidato> habilidadesComprobadas = new ArrayList<>();
		
		for (Habilidad habilidad : habilidades) {
			Boolean bandera = true;
			for (HabilidadCandidato habCand : habilidadesCandidato) {
				if (habCand.getHabilidad().equals(habilidad)) {
					habilidadesComprobadas.add(habCand);
					bandera = false;
					break;
				}
			}
			if (bandera) {
				HabilidadCandidato relleno = new HabilidadCandidato();
				Habilidad masRelleno = new Habilidad();
				masRelleno.setNombreHabilidad("No disponible");
				relleno.setNotaHabilidadCandidato(0);
				relleno.setHabilidad(masRelleno);
				habilidadesComprobadas.add(relleno);
			}
		}
		return habilidadesComprobadas;
	}
	
}
