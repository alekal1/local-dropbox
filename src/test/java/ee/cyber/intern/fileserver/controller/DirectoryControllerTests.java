package ee.cyber.intern.fileserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.cyber.intern.fileserver.entities.dto.DirectoryDto;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;

import static ee.cyber.intern.fileserver.constant.C.DIRECTORY_API_PATH;
import static ee.cyber.intern.fileserver.constant.C.LOCAL_STORAGE_PATH;
import static ee.cyber.intern.fileserver.helper.Companion.getTodayDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DirectoryControllerTests {

    private DirectoryDto dir = new DirectoryDto();
    private DirectoryDto subDir = new DirectoryDto();

    private final String dtoName = "test-directory-v1";
    private final LocalDate dtoCreatedOn = getTodayDate();
    private final BigInteger dtoSize = BigInteger.valueOf(10L);

    private final String subDtoName = "test-sub-directory-v1";
    private final MockMultipartFile file
            = new MockMultipartFile(
            "file",
            "test-file.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "Test file".getBytes()
    );

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void initDto() {
        dir.setId(1L);
        dir.setName(dtoName);
        dir.setCreatedOn(dtoCreatedOn);
        dir.setLastAccessedOn(null);
        dir.setSize(dtoSize);
        dir.setParentDirectory(null);

        subDir.setName(subDtoName);
        subDir.setCreatedOn(dtoCreatedOn);
        subDir.setLastAccessedOn(null);
        subDir.setSize(dtoSize);
        subDir.setParentDirectory(null);
    }

    @Test
    public void verify_canAddDirectory() throws Exception {
        addParentDir()
                .andExpect(status().isOk());
        cleanLocalStorageDir();
    }

    @Test
    public void verify_canAddSubDirectory() throws Exception {
        addParentDir();

        this.mockMvc.perform(post(DIRECTORY_API_PATH + "/" + dir.getId(), subDir)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subDir)))
                .andExpect(status().isOk());
        cleanLocalStorageDir();
    }

    @Test
    public void verify_canUploadFileToDirectory() throws Exception {
        addParentDir();
        this.mockMvc.perform(multipart(DIRECTORY_API_PATH + "/" + dir.getId() + "/file").file(file))
                .andExpect(status().isOk());
        cleanLocalStorageDir();
    }

    public ResultActions addParentDir() throws Exception {
        return this.mockMvc.perform(post(DIRECTORY_API_PATH, dir)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dir)));
    }

    public void cleanLocalStorageDir() throws IOException {
        FileUtils.cleanDirectory(new File(LOCAL_STORAGE_PATH));
    }


}
