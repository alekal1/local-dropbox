package ee.cyber.intern.fileserver.dao;

import ee.cyber.intern.fileserver.entities.DirectoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/***
 * Data access object for communication between database and directory sub system.
 */
@Repository
public interface DirectoryRepository extends JpaRepository<DirectoryEntity, Long> {
}
