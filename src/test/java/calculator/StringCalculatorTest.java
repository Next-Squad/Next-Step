package calculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StringCalculatorTest {

    private StringCalculator cal;

    @BeforeEach
    void setUp() {
        cal = new StringCalculator();
    }

    @Test
    void add() {
        String text = "1,2,3";
        int result = cal.add(text);
        assertThat(result).isEqualTo(6);
    }

    @Test
    void add2() {
        String text = "1:2,3";
        int result = cal.add(text);
        assertThat(result).isEqualTo(6);
    }

    @Test
    void addCustomDelimiter() {
        String text = "//;\n1;2;3";
        int result = cal.add(text);
        assertThat(result).isEqualTo(6);
    }

    @Test
    void addException() {
        String text = "1:2,-3";
        assertThrows(RuntimeException.class, () -> cal.add(text));
    }

    @Test
    void emptyString() {
        String text = null;
        String text2 = "";
        int result = cal.add(text);
        int result2 = cal.add(text2);
        assertThat(result).isZero();
        assertThat(result2).isZero();
    }
}
