package id.ac.ui.cs.advprog.hiringgo.manajemenLog.validation;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.validation.validators.TimeValidator;

public class LogValidatorFactoryTest {
    @Test
    void testCreateValidatorsShouldReturnListWithTimeValidator() {
        LogValidatorFactory factory = new LogValidatorFactory();
        List<LogValidator> validators = factory.createValidators();

        assertThat(validators).hasSize(1);
        assertThat(validators.get(0)).isInstanceOf(TimeValidator.class);
    }
}
