package ee.cyber.intern.fileserver.constant;

/***
 * Constant class.
 */
public class C {
    private static final String API_BASE_PATH = "/api";

    public static final String FILE_API_PATH = API_BASE_PATH + "/file";
    public static final String DIRECTORY_API_PATH = API_BASE_PATH + "/dir";

    public static final String DIR_IS_NOT_EXISTS = "Directory is not exists!";
    public static final String DIR_ALREADY_EXISTS_MSG = "Directory with given name already exists!";
    public static final String DIR_IS_NOT_EMPTY_MSG = "Directory is not empty!";
    public static final String DIR_SPACE_FULL_MSG = "Directory space is lower than file size!";

    public static final String FILE_ALREADY_EXISTS_MSG = "File with given name already exists!";
    public static final String FILE_IS_NOT_EXISTS = "File is not exists";
}
