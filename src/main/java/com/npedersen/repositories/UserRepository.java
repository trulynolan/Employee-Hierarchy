package com.npedersen.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.npedersen.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	Optional<User> findByEmail(String email);

	List<User> findAll();

	Optional<User> findById(Long id);

	List<User> findByLevel(String level);
}