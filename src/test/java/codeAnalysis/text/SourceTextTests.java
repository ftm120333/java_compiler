package codeAnalysis.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SourceTextTests {
    @ParameterizedTest
    @CsvSource({
        "'.', 1",
        "'.\r\n', 2",
        "'.\r\n\r\n', 3"
    })

    void sourceText_includeLastLine(String text, int expectedLineCount) {
        SourceText sourceText = SourceText.from(text);
        assertEquals(expectedLineCount, sourceText.getLines().size());
    }
}
