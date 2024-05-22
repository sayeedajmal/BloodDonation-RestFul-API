package com.strong.BloodDonation.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.strong.BloodDonation.Model.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
            select t from Token t
            inner join t.staff s
            where s.staffId = :staffId and t.loggedOut = false
            """)
    List<Token> findAllAccessTokensByStaff(@Param("staffId") Integer staffId);

    Optional<Token> findByAccessToken(String token);

    Optional<Token> findByRefreshToken(String token);
}
