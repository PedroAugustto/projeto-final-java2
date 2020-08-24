package net.depositcode.main.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.depositcode.main.domain.Empresa;

public interface EmpresaRepository  extends JpaRepository<Empresa,String>{
	
	Empresa findByCnpj(String cnpj);
	Empresa findById (Integer id);
	List<Empresa> findAllByNomeFantasia(String nome);
	List<Empresa> findAllByDataCriacao(LocalDate dataNascimento);
	
}
