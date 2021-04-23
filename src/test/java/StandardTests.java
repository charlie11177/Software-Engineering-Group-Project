import model.Model;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.testfx.assertions.api.Assertions.assertThat;

//
class StandardTests {
//
//    @BeforeAll
//    static void initAll() {
//    }
//
//    @BeforeEach
//    void init() {
//    }
//
    static class foo {
        public static String test = "test";
    }
    @Test
    void succeedingTest() {
        assertThat(foo.test).isEqualTo("test");
    }
//
//    @Test
//    void failingTest() {
//        //fail("a failing test");
//    }
//
//    @Test
//    @Disabled("for demonstration purposes")
//    void skippedTest() {
//        // not executed
//    }
//
//    @Test
//    void abortedTest() {
//        assumeTrue("abc".contains("Z"));
//        fail("test should have been aborted");
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @AfterAll
//    static void tearDownAll() {
//    }
//
}
