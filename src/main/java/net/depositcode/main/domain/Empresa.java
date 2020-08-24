package net.depositcode.main.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

@Entity
public class Empresa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@Column(length = 50)
	private String nomeFantasia;

	@NotNull
	@Column(length = 14, unique = true)
	private String cnpj;

	@NotNull
	@Past
	@Column(columnDefinition = "Date")
	private LocalDate dataCriacao;

	@OneToMany(mappedBy="empresa", cascade = CascadeType.REMOVE)
	private List<Pessoas> pessoas = new ArrayList<>();

	public Empresa() {
	}

	public Empresa(Integer id, String nomeFantasia, String cnpj, LocalDate dataCriacao) {
		super();
		this.id = id;
		this.nomeFantasia = nomeFantasia;
		this.cnpj = cnpj;
		this.dataCriacao = dataCriacao;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public List<Pessoas> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoas> pessoas) {
		this.pessoas = pessoas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Empresa other = (Empresa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Empresa [id=" + id + ", nomeFantasia=" + nomeFantasia + ", cnpj=" + cnpj + ", dataCriacao="
				+ dataCriacao + ", pessoas=" + pessoas + "]";
	}

}
