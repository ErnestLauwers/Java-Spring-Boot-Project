package be.ucll.ip.minor.groep5610.team.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>{

    Team findByNameAndCategory(String name, String category);

    List<Team> findByCategoryIgnoreCase(String category);

    List<Team> findByPassengersLessThanOrderByPassengers(int passengers);
}
