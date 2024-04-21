package org.geekbang.time.commonmistakes.threadpool.threadpooloom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ThreadPoolOOMControllerTest {

    private ThreadPoolOOMController threadPoolOOMControllerUnderTest;

    @BeforeEach
    void setUp() {
        threadPoolOOMControllerUnderTest = new ThreadPoolOOMController();
    }

    @Test
    void testOom1() throws Exception {
        // Setup
        // Run the test
        threadPoolOOMControllerUnderTest.oom1();

        // Verify the results
    }

    @Test
    void testOom1_ThrowsInterruptedException() {
        // Setup
        // Run the test
        assertThatThrownBy(() -> threadPoolOOMControllerUnderTest.oom1()).isInstanceOf(InterruptedException.class);
    }

    @Test
    void testOom2() throws Exception {
        // Setup
        // Run the test
        threadPoolOOMControllerUnderTest.oom2();

        // Verify the results
    }

    @Test
    void testOom2_ThrowsInterruptedException() {
        // Setup
        // Run the test
        assertThatThrownBy(() -> threadPoolOOMControllerUnderTest.oom2()).isInstanceOf(InterruptedException.class);
    }

    @Test
    void testRight() throws Exception {
        // Setup
        // Run the test
        final int result = threadPoolOOMControllerUnderTest.right();

        // Verify the results
        assertThat(result).isEqualTo(0);
    }

    @Test
    void testRight_ThrowsInterruptedException() {
        // Setup
        // Run the test
        assertThatThrownBy(() -> threadPoolOOMControllerUnderTest.right()).isInstanceOf(InterruptedException.class);
    }

    @Test
    void testBetter() throws Exception {
        // Setup
        // Run the test
        final int result = threadPoolOOMControllerUnderTest.better();

        // Verify the results
        assertThat(result).isEqualTo(0);
    }

    @Test
    void testBetter_ThrowsInterruptedException() {
        // Setup
        // Run the test
        assertThatThrownBy(() -> threadPoolOOMControllerUnderTest.better()).isInstanceOf(InterruptedException.class);
    }
}
