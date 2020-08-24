package net.depositcode.main.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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

import net.depositcode.main.domain.Pessoas;
import net.depositcode.main.repository.PessoaRepository;

@RestController
@RequestMapping("/pessoas")
public class PessoasController {

	@Autowired
	private PessoaRepository repository;

	@GetMapping("/todos")
	public ResponseEntity<List<Pessoas>> getAll() {

		List<Pessoas> pessoasTodos = repository.findAll();

		return ResponseEntity.ok(pessoasTodos);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Pessoas> getPessoa(@PathVariable String codigo) {

		Pessoas existente = repository.findByCpf(codigo);
		existente = (existente != null) ? existente : repository.findById(Integer.valueOf(codigo));

		if (existente == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(existente);
	}

	@GetMapping
	public ResponseEntity<Set<Pessoas>> getByQueryString(@RequestParam Map<String, String> parametros) {

		Set<Pessoas> encontrados = new HashSet<Pessoas>();
		List<Pessoas> encontradosNome;
		List<Pessoas> encontradosDataNascimento;
		Pessoas encontradoCpf;
		Pessoas encontradoId;

		encontradosNome = repository.findAllByNome(parametros.getOrDefault("nome", ""));

		if (encontradosNome != null) {
			for (Pessoas pessoas : encontradosNome) {
				encontrados.add(pessoas);

			}
		}

		encontradosDataNascimento = repository.findAllByDataNascimento(LocalDate.parse(parametros.getOrDefault("dataNascimento", "1900-01-01")));

		if (encontradosDataNascimento != null) {
			for (Pessoas pessoas : encontradosDataNascimento) {
				encontrados.add(pessoas);

			}
		}

		encontradoCpf = repository.findByCpf(parametros.getOrDefault("cpf", ""));
		if (encontradoCpf != null) {
			encontrados.add(encontradoCpf);
		}

		encontradoId = repository.findById(Integer.valueOf(parametros.getOrDefault("id", "0")));
		if (encontradoId != null) {
			encontrados.add(encontradoId);
		}
		
		if(encontrados.size() == 0)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(encontrados);
	}

	@PostMapping
	public ResponseEntity<Pessoas> postPessoa(@RequestBody Pessoas novo) {

		Pessoas existente = repository.findByCpf(novo.getCpf());

		if (existente != null)
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		novo.setId(null);
		repository.save(novo);

		// URI uri =
		// ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novo.getId()).toUri();
		// return ResponseEntity.created(uri).build();

		return new ResponseEntity<>(novo, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<Pessoas> putPessoa(@RequestBody Pessoas modificado) {

		Pessoas existente = repository.findByCpf(modificado.getCpf());

		if (existente == null)
			return ResponseEntity.notFound().build();

		
		existente.setDataNascimento(modificado.getDataNascimento() != null ? modificado.getDataNascimento() : existente.getDataNascimento());
		existente.setNome(modificado.getNome() != null ? modificado.getNome() :existente.getNome());

		repository.save(existente);

		return ResponseEntity.ok().body(existente);

	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Pessoas> deletePessoa(@PathVariable String codigo) {

		Pessoas existente = repository.findByCpf(codigo);

		existente = (existente != null) ? existente : repository.findById(Integer.valueOf(codigo));

		if (existente == null)
			return ResponseEntity.notFound().build();

		repository.delete(existente);

		return ResponseEntity.ok().body(existente);

	}
}
