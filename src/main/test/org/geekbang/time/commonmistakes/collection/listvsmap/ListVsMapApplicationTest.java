package org.geekbang.time.commonmistakes.collection.listvsmap;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ListVsMapApplicationTest {

    @Test
    void testMain() throws Exception {
        // Setup
        // Run the test
        ListVsMapApplication.main(new String[]{"args"});

        // Verify the results
    }

    @Test
    void testMain_ThrowsInterruptedException() {
        // Setup
        // Run the test
        assertThatThrownBy(() -> ListVsMapApplication.main(new String[]{"args"}))
                .isInstanceOf(InterruptedException.class);
    }
}
