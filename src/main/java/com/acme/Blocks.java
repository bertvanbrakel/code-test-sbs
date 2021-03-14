package com.acme;

public class Blocks {

    /**
     * Calculate the max volume of water pooled between a series of blocks if water was poured over them
     *
     * @param blocks a list of blocks where the value is the height, and the position represents the x axis.
     * @return the total volume of water pooled in between the blocks
     */
    public static int calculateVolume(int[] blocks) {
        //the idea is we calculate the max water barrier height left and right of a block. The volume of water
        //a block contributes is the lowest of these two minus the block height
        var totalWaterVolume = 0;
        //we're calculating from left to right, so we can bring this forward instead of recalculating
        var maxHeightLeft = 0;
        for (var i = 0; i < blocks.length; i++) {
            var blockHeight = blocks[i];
            //NOTE: we are repeating our right calculation here. Let's only optimise if needed
            var maxHeightRight = calculateMaxHeightRight(blocks, i);
            var waterHeight = Math.min(maxHeightLeft, maxHeightRight);
            var waterVolume = Math.max(0, waterHeight - blockHeight);
            totalWaterVolume += waterVolume;

            //update for the next block
            maxHeightLeft = Math.max(maxHeightLeft, blockHeight);
        }

        return totalWaterVolume;
    }

    /**
     * Move right and find the highest block in the way of the water
     */
    protected static int calculateMaxHeightRight(int[] blocks, int currentPosition) {
        var maxRight = 0;
        for (var i = currentPosition + 1; i < blocks.length; i++) {
            maxRight = Math.max(blocks[i], maxRight);
        }
        return maxRight;
    }

}
