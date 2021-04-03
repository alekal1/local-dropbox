package ee.cyber.intern.fileserver.exceptions.file;

import static ee.cyber.intern.fileserver.constant.C.FILE_ALREADY_EXISTS_MSG;

/**
 * Custom exception if file with given name already exists
 */
public class FileAlreadyExistsException extends RuntimeException {
    public FileAlreadyExistsException() {
        super(FILE_ALREADY_EXISTS_MSG);
    }
}
