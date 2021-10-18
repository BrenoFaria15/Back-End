package com.projeto.tccv1.api.controller;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.tccv1.api.dto.AtendimentoDTO;
import com.projeto.tccv1.exeception.RegraNegocioException;
import com.projeto.tccv1.model.entity.Agenda;
import com.projeto.tccv1.model.entity.Atendimento;
import com.projeto.tccv1.model.entity.Paciente;
import com.projeto.tccv1.model.entity.Profissional;
import com.projeto.tccv1.model.entity.TipoAtendimento;
import com.projeto.tccv1.model.entity.Unidade;
import com.projeto.tccv1.model.entity.Usuario;
import com.projeto.tccv1.service.AgendaService;
import com.projeto.tccv1.service.AtendimentoService;
import com.projeto.tccv1.service.PacienteService;
import com.projeto.tccv1.service.ProfissionalService;
import com.projeto.tccv1.service.TipoAtendimentoService;
import com.projeto.tccv1.service.UnidadeService;
import com.projeto.tccv1.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/atendimentos")
@RequiredArgsConstructor
public class AtendimentoContoller {
	
	private final AtendimentoService service;
	private final AgendaService agendaService;
	private  final PacienteService pacienteService;
	private final  ProfissionalService profissionalService;
	private  final TipoAtendimentoService tipoAtendService;
	private  final UnidadeService unidadeService;
	private final UsuarioService usuarioService;
	
	
	
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody AtendimentoDTO dto) {
		try {
			Atendimento entidade = converter(dto);
			entidade = service.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
			
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id,@RequestBody AtendimentoDTO dto) {
		
		return service.buscarPorId(id).map(entity -> {
			try {
			Atendimento atendimento = converter(dto);
			atendimento.setId_atendimento(entity.getId_atendimento());
			service.atualizar(atendimento);
			return ResponseEntity.ok(atendimento);
		}catch(RegraNegocioException e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		}).orElseGet(() -> new ResponseEntity("Atendimento não encontrado", HttpStatus.BAD_REQUEST));
		
		
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id")Long id ) {
		return service.buscarPorId(id).map(entidade -> {
			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			
		}).orElseGet(() ->  new ResponseEntity("Atendimento não encontrado", HttpStatus.BAD_REQUEST)); 
	}
	
	
	@GetMapping
	public ResponseEntity buscar(@RequestParam(value="data",required = false)Date data,
								@RequestParam("profissional")Long idProfissional,
								 @RequestParam("paciente")Long idPaciente
								) {
		
		Atendimento atendimentoFiltro = new Atendimento();
		atendimentoFiltro.setData(data);
		
		Optional<Profissional>profissional = profissionalService.buscarPorId(idProfissional);
		if(!profissional.isPresent()) {
			return ResponseEntity.badRequest().body("Profissional com esse id não encontrado");
		}else {
			atendimentoFiltro.setProfissional(profissional.get());
		}
		
		Optional<Paciente>paciente = pacienteService.buscarPorId(idPaciente);
		if(!paciente.isPresent()) {
			return ResponseEntity.badRequest().body("Paciente com esse id não encontrado");
		}else {
			atendimentoFiltro.setPaciente(paciente.get());
		}
		
		List<Atendimento> atendimentos= service.buscar(atendimentoFiltro);
		return ResponseEntity.ok(atendimentos);
	}
	
	private Atendimento converter(AtendimentoDTO dto) {
		Atendimento atendimento = new Atendimento();
		
		atendimento.setId_atendimento(dto.getId_atendimento());
		atendimento.setAltura(dto.getAltura());
		atendimento.setAvaliacao(dto.getAvaliacao());
		atendimento.setBpm(dto.getBpm());
		atendimento.setCid(dto.getCid());
		atendimento.setData(dto.getData());
		atendimento.setEntrevistaClinica(dto.getEntrevistaClinica());
		atendimento.setFlg_atendido(dto.isFlg_atendido());
		atendimento.setGlicemia(dto.getGlicemia());
		atendimento.setHoraFim(dto.getHoraFim());
		atendimento.setHoraInicio(dto.getHoraInicio());
		atendimento.setImc(dto.getImc());
		atendimento.setPeso(dto.getPeso());
		atendimento.setSaturacao(dto.getSaturacao());
		atendimento.setTemperatura(dto.getTemperatura());
		
		//Agenda agenda =  agendaService.buscarPorId(dto.getId_agenda())
		//.orElseThrow(() -> new RegraNegocioException("Agenda com esse id não encontrado"));
		//atendimento.setAgenda(agenda);
		
		Agenda agenda =  agendaService.buscarPorId(dto.getId_agenda()).orElse(null);
		atendimento.setAgenda(agenda);
				
		Paciente paciente =pacienteService.buscarPorId(dto.getPaciente())
				.orElseThrow(() -> new RegraNegocioException("Paciente com esse id não encontrado"));
		atendimento.setPaciente(paciente);
		
		Profissional profissional = profissionalService.buscarPorId(dto.getProfissional())
				.orElseThrow(() -> new RegraNegocioException("Profissional com esse id não encontrado"));
		atendimento.setProfissional(profissional);
		
		TipoAtendimento tipoAtendimento = tipoAtendService.buscarPorId(dto.getTipoatendimento())
				.orElseThrow(() -> new RegraNegocioException("Tipo de Atendimento com esse id não encontrado"));
		atendimento.setTipoatendimento(tipoAtendimento);
		
		Unidade unidade = unidadeService.buscarPorId(dto.getUnidade())
				.orElseThrow(() -> new RegraNegocioException("Unidade de Atendimento com esse id não encontrada"));
		atendimento.setUnidade(unidade);
		
		Usuario usuario = usuarioService.buscarPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuario com esse id não encontrado"));
		atendimento.setUsuario(usuario);
		
		
		
		
		
		
		
		
		return atendimento;
	}

}
