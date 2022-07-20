package util;

import java.io.BufferedReader;
import java.io.StringBufferInputStream;
import java.io.StringReader;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class IOUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(IOUtilsTest.class);

    @Test
    @DisplayName("데이터와 데이터의 길이가 같으면, 데이터 전체를 읽어온다.")
    public void readData() throws Exception {
        String data = "abcd123";
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        String readResult = IOUtils.readData(br, data.length());

        assertThat(readResult).isEqualTo(data);
    }
}
