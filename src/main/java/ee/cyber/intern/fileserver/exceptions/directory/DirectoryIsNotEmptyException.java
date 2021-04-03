package ee.cyber.intern.fileserver.exceptions.directory;

import static ee.cyber.intern.fileserver.constant.C.DIR_IS_NOT_EMPTY_MSG;

public class DirectoryIsNotEmptyException extends RuntimeException {

    public DirectoryIsNotEmptyException() {
        super(DIR_IS_NOT_EMPTY_MSG);
    }
}
