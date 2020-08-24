package net.depositcode.main.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

import net.depositcode.main.domain.Empresa;
import net.depositcode.main.dto.EmpresaDTO;
import net.depositcode.main.repository.EmpresaRepository;
import net.depositcode.main.repository.PessoaRepository;

@RestController
@RequestMapping("/empresas")
public class EmpresasController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@GetMapping("/todos")
	public ResponseEntity<List<EmpresaDTO>> getAll() {

		List<Empresa> empresaTodas = empresaRepository.findAll();
		List<EmpresaDTO> empresaDTO = empresaTodas.stream().map(obj -> new EmpresaDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok(empresaDTO);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<EmpresaDTO> getEmpresa(@PathVariable String codigo) {
		Empresa existente = empresaRepository.findByCnpj(codigo);
		existente = (existente != null) ? existente : empresaRepository.findById(Integer.valueOf(codigo));
		
		if (existente == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(new EmpresaDTO(existente));
	}

	@GetMapping
	public ResponseEntity<Set<EmpresaDTO>> getByQueryString(@RequestParam Map<String, String> parametros) {

		Set<Empresa> encontrados = new HashSet<Empresa>();
		List<Empresa> encontradosNomeFantasia;
		List<Empresa> encontradosDataCriacao;
		Empresa encontradoCnpj;
		Empresa encontradoId;

		encontradosNomeFantasia = empresaRepository.findAllByNomeFantasia(parametros.getOrDefault("nome", ""));
		encontradosNomeFantasia.stream().filter(obj -> obj != null).forEach(obj -> encontrados.add(obj));

		encontradosDataCriacao = empresaRepository.findAllByDataCriacao(LocalDate.parse(parametros.getOrDefault("dataCriacao", "1900-01-01")));
		encontradosDataCriacao.stream().filter(obj -> obj != null).forEach(obj -> encontrados.add(obj));


		encontradoCnpj = empresaRepository.findByCnpj(parametros.getOrDefault("cnpj", ""));
		if (encontradoCnpj != null) {
			encontrados.add(encontradoCnpj);
		}

		encontradoId = empresaRepository.findById(Integer.valueOf(parametros.getOrDefault("id", "0")));
		if (encontradoId != null) {
			encontrados.add(encontradoId);
		}

		if (encontrados.size() == 0)
			return ResponseEntity.notFound().build();

		Set<EmpresaDTO> encontradosDtos = encontrados.stream().map(obj -> new EmpresaDTO(obj)).collect(Collectors.toSet());
		
		return ResponseEntity.ok(encontradosDtos);
	}

	@PostMapping
	public ResponseEntity<Empresa> postEmpresa(@RequestBody Empresa nova) {
		Empresa existente = empresaRepository.findByCnpj(nova.getCnpj());

		if (existente != null)
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

		nova.setId(null);

		empresaRepository.save(nova);

		nova.getPessoas().forEach(obj -> {
			obj.setEmpresa(nova);
			pessoaRepository.save(obj);
		});

		return ResponseEntity.status(HttpStatus.CREATED).build();

	}

	@PutMapping
	public ResponseEntity<Empresa> putEmpresa(@RequestBody Empresa modificada) {
		Empresa existente = empresaRepository.findByCnpj(modificada.getCnpj());
		if (existente == null)
			return ResponseEntity.notFound().build();

		existente.setDataCriacao(modificada.getDataCriacao() != null ? modificada.getDataCriacao() : existente.getDataCriacao());
		existente.setNomeFantasia(modificada.getNomeFantasia() != null ? modificada.getNomeFantasia() : existente.getNomeFantasia());
		
		empresaRepository.save(existente);

		return ResponseEntity.ok().body(existente);
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Empresa> deleteEmpresa(@PathVariable String codigo) {
		Empresa existente = empresaRepository.findByCnpj(codigo);
		existente = (existente != null) ? existente : empresaRepository.findById(Integer.valueOf(codigo));
		if (existente == null)
			return ResponseEntity.notFound().build();

		empresaRepository.delete(existente);
		return ResponseEntity.ok(existente);
	}
}
