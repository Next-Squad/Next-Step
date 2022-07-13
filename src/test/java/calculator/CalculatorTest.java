package calculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculatorTest {

    private Calculator cal;

    @BeforeEach
    void setUp() {
        cal = new Calculator();
        System.out.println("before");
    }

    @Test
    void add() {
        int result = cal.add(6, 3);
        assertThat(result).isEqualTo(9);
    }

    @Test
    void subtract() {
        int result = cal.subtract(6, 3);
        assertThat(result).isEqualTo(3);
    }

    @AfterEach
    void teardown() {
        System.out.println("teardown");
    }
}
