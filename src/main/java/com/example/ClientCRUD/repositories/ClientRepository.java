package com.example.ClientCRUD.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ClientCRUD.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

}
