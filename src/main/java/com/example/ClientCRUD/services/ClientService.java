package com.example.ClientCRUD.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ClientCRUD.dto.ClientDTO;
import com.example.ClientCRUD.entities.Client;
import com.example.ClientCRUD.repositories.ClientRepository;
import com.example.ClientCRUD.services.exceptions.DatabaseException;
import com.example.ClientCRUD.services.exceptions.ResourceNotFoundException;


@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pagerequest){
		Page<Client> list = repository.findAll(pagerequest);
		return list.map(client -> new ClientDTO(client));
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Client not found!"));
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO insertClient(ClientDTO clientDto) {
		Client entity = new Client();
		entity.setId(clientDto.getId());
		entity.setName(clientDto.getName());
		entity.setCpf(clientDto.getCpf());
		entity.setIncome(clientDto.getIncome());
		entity.setBirthDate(clientDto.getBirthDate());
		entity.setChildren(clientDto.getChildren());
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO updateClient(Long id, ClientDTO dto) {
		try {
			Client entity = repository.getById(id);
			entity.setName(dto.getName());
			entity.setCpf(dto.getCpf());
			entity.setIncome(dto.getIncome());
			entity.setBirthDate(dto.getBirthDate());
			entity.setChildren(dto.getChildren());
			entity = repository.save(entity);
			return new ClientDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found "+ id);
		}
	}
	
	public void deleteClient(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found "+ id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation!");
		}		
	}
}
