package ee.cyber.intern.fileserver.exceptions.directory;

import static ee.cyber.intern.fileserver.constant.C.DIR_IS_NOT_EMPTY_MSG;

/**
 * Custom exception if directory is not empty
 */
public class DirectoryIsNotEmptyException extends RuntimeException {

    public DirectoryIsNotEmptyException() {
        super(DIR_IS_NOT_EMPTY_MSG);
    }
}
