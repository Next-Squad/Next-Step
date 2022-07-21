package util;

import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

class FileUtilsTest {

    @Test
    void readFileTest() {
        // given
        URL testFile = getClass().getClassLoader().getResource("test.txt");
        String path = testFile.getPath();

        // when
        byte[] bytes = FileUtils.readFile(path);

        // then
        assertThat(bytes).isNotNull();
        assertThat(bytes).isNotEmpty();
    }
}