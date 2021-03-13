package com.acme;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BlocksTest {

    @Test
    public void no_water() {
        assertEquals("no volume", 0, Blocks.calculateVolume(blocks(0, 0, 0)));
        assertEquals("no volume", 0, Blocks.calculateVolume(blocks(0, 1, 0)));
    }

    @Test
    public void max_right_height() {
        assertEquals(1, Blocks.maxHeightRight(blocks(1, 0, 1), 0));
        assertEquals(1, Blocks.maxHeightRight(blocks(1, 0, 1), 1));
        assertEquals(0, Blocks.maxHeightRight(blocks(1, 0, 1), 2));

        assertEquals(2, Blocks.maxHeightRight(blocks(1, 0, 2), 0));
        assertEquals(2, Blocks.maxHeightRight(blocks(1, 0, 2), 1));
    }

    //simple cases
    @Test
    public void simple_two_blocks() {
        assertEquals(1, Blocks.calculateVolume(blocks(1, 0, 1)));
        assertEquals(2, Blocks.calculateVolume(blocks(2, 0, 2)));
    }

    @Test
    public void complex_blocks() {
        assertEquals(3, Blocks.calculateVolume(blocks(16, 7, 1, 4, 0)));
        assertEquals(54, Blocks.calculateVolume(blocks(7, 1, 0, 15, 1, 0, 11, 3, 1, 3, 2, 1, 6)));
    }

    @Test
    public void question_1_3_test() {
        assertEquals(7, Blocks.calculateVolume(blocks(0, 1, 0, 2, 1, 0, 1, 3, 1, 3, 2, 1, 0)));
    }

    private static int[] blocks(int... blocks) {
        return blocks;
    }
}
