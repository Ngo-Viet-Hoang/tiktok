package com.example.tiktok.repository;

import com.example.tiktok.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findAccountByUsername(String username);
    Optional<Account> findAccountsByEmail(String email);
     List<Account> findAllByUsername(String username);

    @Query("SELECT re FROM Account re WHERE "
            + "re.username LIKE %:keyword% ")
     Page<Account> filter(@Param("keyword") String keyword, Pageable pageable);

}
