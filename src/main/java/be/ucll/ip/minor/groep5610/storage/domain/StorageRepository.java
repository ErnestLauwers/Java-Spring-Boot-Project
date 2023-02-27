package be.ucll.ip.minor.groep5610.storage.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface StorageRepository extends JpaRepository<Storage, Long> {}
