package spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.exception.ConsoleOutputException;
import ru.otus.spring.service.AnswerServiceImpl;
import ru.otus.spring.service.ConsoleOutputService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnswerServiceImplTest {

    private AnswerServiceImpl answerService;
    @Mock
    private ConsoleOutputService communicationService;

    @BeforeEach
    void setUp() {
        answerService = new AnswerServiceImpl(communicationService);
    }

    @Test
    void getAnswer() throws ConsoleOutputException {
        String expected = "B";
        when(communicationService.getAnswer()).thenReturn(expected);
        String answer = answerService.getAnswer();
        assertEquals(expected, answer);
    }
}