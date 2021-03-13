package com.acme;

public class Blocks {

    /**
     * Calculate the max voume of water in between the blocks
     *
     * @param blocks
     * @return
     */
    public static int calculateVolume(int[] blocks) {
        //the idea is we calculate the max water barrier height left and right of a block. The volume of water
        //a block contributes is the lowest of these two minus the block height
        var totalWaterVolume = 0;
        //we're calculating from left to right, so we can bring this forward instead of recalculating
        var maxLeft = 0;
        for (var i = 0; i < blocks.length; i++) {
            var blockHeight = blocks[i];
            //NOTE: we are repeating our right calculation here. Let's only optimise if needed
            var maxRight = maxHeightRight(blocks, i);
            var waterHeight = Math.min(maxLeft, maxRight);
            var waterVolume = Math.max(0, waterHeight - blockHeight);
            totalWaterVolume += waterVolume;

            //update for the next block
            maxLeft = Math.max(maxLeft, blockHeight);
        }

        return totalWaterVolume;
    }

    /**
     * Move right and find the highest block in the way of the water
     */
    protected static int maxHeightRight(int[] blocks, int currentPosition) {
        var maxRight = 0;
        for (var i = currentPosition + 1; i < blocks.length; i++) {
            maxRight = Math.max(blocks[i], maxRight);
        }
        return maxRight;
    }

}
