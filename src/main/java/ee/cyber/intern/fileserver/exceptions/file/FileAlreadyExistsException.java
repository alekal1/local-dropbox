package ee.cyber.intern.fileserver.exceptions.file;

import static ee.cyber.intern.fileserver.constant.C.FILE_ALREADY_EXISTS_MSG;

public class FileAlreadyExistsException extends RuntimeException {
    public FileAlreadyExistsException() {
        super(FILE_ALREADY_EXISTS_MSG);
    }
}
