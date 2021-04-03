package ee.cyber.intern.fileserver.exceptions.directory;

import static ee.cyber.intern.fileserver.constant.C.DIR_ALREADY_EXISTS_MSG;

public class DirectoryAlreadyExistsException extends RuntimeException {

    public DirectoryAlreadyExistsException() {
        super(DIR_ALREADY_EXISTS_MSG);
    }

}
