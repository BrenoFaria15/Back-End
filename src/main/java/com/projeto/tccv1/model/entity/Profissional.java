package com.projeto.tccv1.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projeto.tccv1.model.enums.TipoUsuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="profissional")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor


public class Profissional 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id_profissional;
	
	@Column(unique = true,name = "cns",nullable = false)
	private String cns;
	
	@Column(unique = true,name = "cpf",nullable = false)
	private String cpf;
	
	@Column(name = "nome",nullable = false)
	private String nome;
	
	@Column(name = "logradouro",nullable = false)
	private String logradouro;
	
	@Column(name = "numero",nullable = false)
	private String numero;
	
	@Column(name = "cep",nullable = false)
	private String cep;
	
	@Column(name = "bairro",nullable = false)
	private String bairro;
	
	@Column(name = "complemento")
	private String complemento;
	
	@Column(unique = true,name = "rg")
	private String rg;
	
	@Column(name = "celular",nullable = false)
	private String celular;
	
	@Column(name = "municipio")
	private String municipio;
	
	@Column(name = "especialidade",nullable = false)
	private String especialidade;
	
	@Column(name = "nome_conselho")
	private String nomeConselho;
	
	@Column(name = "cd_conselho")
	private String cdConselho;
	
	@Column(name = "data_nascimento",nullable = false)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date dataNascimento;
	
	@Column(name = "flg_ativo",nullable = false)
	private boolean flgAtivo;

	
	
	
	
  

	

}
