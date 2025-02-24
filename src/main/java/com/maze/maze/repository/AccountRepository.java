package com.maze.maze.repository;

import com.maze.maze.model.Account;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
	public Account findByPhoneNumber(String phoneNumber);

	Account findByPhoneNumberContaining(String search);

	Account findByEmail(String email);

	@Query("select count(acc.id) from Account acc")
	int getTotalRegister();

	Account findByPhoneNumberAndPassword(String phoneNumber, String password);
}
