package ee.cyber.intern.fileserver.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;

@NoArgsConstructor
@Data
@Entity
@Table(name = "directory")
public class DirectoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "last_accessed_on")
    private LocalDate lastAccessedOn;

    @Column(name = "size")
    private BigInteger size;

    @Column(name = "dir_path")
    private String directoryPath;

    @ManyToOne
    @JoinColumn(name = "dir_id")
    private DirectoryEntity parentDirectory;
}
