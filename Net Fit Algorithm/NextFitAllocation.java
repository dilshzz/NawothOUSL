import java.util.Scanner;

public class NextFitMemoryAllocation {

    static class Block {
        int size;
        boolean allocated;
        String allocatedProcess;

        Block(int size) {
            this.size = size;
            this.allocated = false;
            this.allocatedProcess = null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Predefined memory blocks (Fixed sizes)
        int[] blockSizes = {200, 300, 100, 500, 50};
        Block[] memoryBlocks = new Block[blockSizes.length];

        for (int i = 0; i < blockSizes.length; i++) {
            memoryBlocks[i] = new Block(blockSizes[i]);
        }

        // Step 2: Input process sizes
        System.out.println("Enter the number of processes:");
        int processCount = scanner.nextInt();
        int[] processSizes = new int[processCount];

        System.out.println("Enter the sizes of the processes:");
        for (int i = 0; i < processCount; i++) {
            processSizes[i] = scanner.nextInt();
            if (processSizes[i] <= 0) {
                System.out.println("Invalid input. Process size must be greater than zero.");
                i--; // Retry input
            }
        }

        String[] processNames = new String[processCount];
        for (int i = 0; i < processCount; i++) {
            processNames[i] = "Process " + (char) ('A' + i); // Generate process names as A, B, C, ...
        }

        int pointer = 0; // Start search from the first block

        // Step 3: Allocate processes using Next Fit
        for (int i = 0; i < processSizes.length; i++) {
            int processSize = processSizes[i];

            // Check if the process is too large for all blocks
            boolean canFit = false;
            for (Block block : memoryBlocks) {
                if (!block.allocated && block.size >= processSize) {
                    canFit = true;
                    break;
                }
            }

            if (!canFit) {
                System.out.println(processNames[i] + " is too large for any block and cannot be allocated.");
                continue; // Skip to the next process
            }

            // Existing Next-Fit Allocation Logic
            boolean allocated = false;
            for (int j = 0; j < memoryBlocks.length; j++) {
                int index = (pointer + j) % memoryBlocks.length; // Wrap around using modulo

                if (!memoryBlocks[index].allocated && memoryBlocks[index].size >= processSize) {
                    System.out.println("\n" + processNames[i] + " allocated " + processSize + " KB in Block " + (index + 1));
                    memoryBlocks[index].size -= processSize;
                    memoryBlocks[index].allocated = memoryBlocks[index].size == 0; // Mark as allocated if size is 0
                    memoryBlocks[index].allocatedProcess = processNames[i];
                    pointer = (index + 1) % memoryBlocks.length; // Update search pointer
                    allocated = true;

                    // Display relevant block and other free blocks
                    System.out.println("Relevant Block (Block " + (index + 1) + "): " + memoryBlocks[index].size + " KB (Free)");
                    displayOtherBlocks(memoryBlocks, index);
                    break;
                }
            }

            if (!allocated) {
                System.out.println(processNames[i] + " could not be allocated.");
            }
        }

        // Step 4: Print final memory state
        System.out.println("\nFinal Memory State:");
        for (int i = 0; i < memoryBlocks.length; i++) {
            System.out.println("Block " + (i + 1) + ": " + memoryBlocks[i].size + " KB ("
                    + (memoryBlocks[i].allocated ? "Allocated" : "Free") + ")");
        }

        // Step 5: Print final memory allocation summary
        System.out.println("\nFinal Memory Allocation:");
        for (int i = 0; i < memoryBlocks.length; i++) {
            if (memoryBlocks[i].allocatedProcess != null) {
                System.out.println(memoryBlocks[i].allocatedProcess + ": Allocated in Block " + (i + 1));
            }
        }

        scanner.close();
    }

    private static void displayOtherBlocks(Block[] blocks, int relevantIndex) {
        System.out.println("Other Free Blocks:");
        for (int i = 0; i < blocks.length; i++) {
            if (i != relevantIndex && !blocks[i].allocated) {
                System.out.println("Block " + (i + 1) + ": " + blocks[i].size + " KB (Free)");
            }
        }
    }
}
