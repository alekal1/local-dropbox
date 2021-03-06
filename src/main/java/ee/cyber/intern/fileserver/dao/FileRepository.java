package ee.cyber.intern.fileserver.dao;

import ee.cyber.intern.fileserver.entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/***
 * Data access object for communication between database and file sub system.
 */
@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
