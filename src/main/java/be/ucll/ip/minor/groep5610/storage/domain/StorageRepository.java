package be.ucll.ip.minor.groep5610.storage.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {
    Storage findByNameAndPostalCode(String name, Integer postalCode);
    List<Storage> findByOrderByNameAsc();
    List<Storage> findByOrderByHeightDesc();
    Storage findByName(String name);

    @Query ("SELECT s from Storage s WHERE s.name LIKE %?1%")
    List<Storage> findStoragesBySearch(String keyword);

}