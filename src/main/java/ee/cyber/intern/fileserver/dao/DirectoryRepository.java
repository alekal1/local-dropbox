package ee.cyber.intern.fileserver.dao;

import ee.cyber.intern.fileserver.entities.DirectoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectoryRepository extends JpaRepository<DirectoryEntity, Long> {
}
