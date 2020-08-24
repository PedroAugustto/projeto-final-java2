package net.depositcode.main.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.depositcode.main.domain.Pessoas;

public interface PessoaRepository extends JpaRepository<Pessoas, String> {
	
	Pessoas findByCpf(String cpf);
	Pessoas findById(Integer id);
	List<Pessoas> findAllByNome(String nome);

	List<Pessoas> findAllByDataNascimento(LocalDate dataNascimento);
}
