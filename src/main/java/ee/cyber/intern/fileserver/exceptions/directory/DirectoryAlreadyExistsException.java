package ee.cyber.intern.fileserver.exceptions.directory;

import static ee.cyber.intern.fileserver.constant.C.DIR_ALREADY_EXISTS_MSG;

/**
 * Custom exception if directory with given name already exists
 */
public class DirectoryAlreadyExistsException extends RuntimeException {

    public DirectoryAlreadyExistsException() {
        super(DIR_ALREADY_EXISTS_MSG);
    }

}
