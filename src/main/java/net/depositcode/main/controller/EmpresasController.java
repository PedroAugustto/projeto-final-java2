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

import net.depositcode.main.domain.Empresa;
import net.depositcode.main.repository.EmpresaRepository;

@RestController
@RequestMapping("/empresa")
public class EmpresasController {

	@Autowired
	private EmpresaRepository empresaRepository;

	@GetMapping("/todos")
	public ResponseEntity<List<Empresa>> getAll() {
		
		List<Empresa> empresaTodas = empresaRepository.findAll();
		return ResponseEntity.ok(empresaTodas);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Empresa> getEmpresa(@PathVariable String codigo) {
		Empresa existente = empresaRepository.findByCnpj(codigo);
		existente = (existente != null) ? existente: empresaRepository.findById(Integer.valueOf(codigo));
		if (existente == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(existente);
	}
	
	@GetMapping
	public ResponseEntity<Set<Empresa>> getByQueryString(@RequestParam Map <String, String> parametros) {
		
		Set<Empresa> encontrados = new HashSet<Empresa>();
		List<Empresa> encontradosNomeFantasia;
		List<Empresa> encontradosDataCriacao;
		Empresa encontradoCnpj;
		Empresa encontradoId;
		
		encontradosNomeFantasia = empresaRepository.findAllByNomeFantasia(parametros.getOrDefault("nome", ""));
		
		if (encontradosNomeFantasia != null) {
			for (Empresa empresas : encontradosNomeFantasia) {
				encontrados.add(empresas);
				
			}
		}
		
		encontradosDataCriacao = empresaRepository.findAllByDataCriacao(LocalDate.parse(parametros.getOrDefault("dataCriacao", "1900-01-01")));
		
		if (encontradosDataCriacao != null) {
			for (Empresa empresas : encontradosDataCriacao) {
				encontrados.add(empresas);
				
			}
		}
		
		encontradoCnpj = empresaRepository.findByCnpj(parametros.getOrDefault("cnpj", ""));
		if (encontradoCnpj != null) {
			encontrados.add(encontradoCnpj);
		}
		
		encontradoId = empresaRepository.findById(Integer.valueOf(parametros.getOrDefault("id", "0")));
		if (encontradoId != null) {
			encontrados.add(encontradoId);
		}
		
		if(encontrados.size() == 0)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(encontrados);
	}
	
	@PostMapping
	public ResponseEntity<Empresa> postEmpresa(@RequestBody Empresa nova) {
		Empresa existente = empresaRepository.findByCnpj(nova.getCnpj());

		if (existente != null)
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

		nova.setId(null);
		empresaRepository.save(nova);

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

	@DeleteMapping ("/{codigo}")
	public ResponseEntity<Empresa> deleteEmpresa(@PathVariable String codigo ) {
		Empresa existente = empresaRepository.findByCnpj(codigo);
		existente = (existente != null) ? existente: empresaRepository.findById(Integer.valueOf(codigo));
		if (existente == null)
			return  ResponseEntity.notFound().build();
		
		
		empresaRepository.delete(existente);
		return ResponseEntity.ok(existente);
	}
}
