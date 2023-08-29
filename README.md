# RobinsToolbox
This is my little box of tools I've made to help me with random things

# Print.class
This class is a simple class that helps with short handing log outputs and has a simple write to file interface with manual write buffer control to limit write operations. No more shall I write System.out.println()!

The write to file with buffer function was implemented to help with huge .csv and debug log writing operations. Instead of appending on every loop iteration, you can enable the buffer to stack up the output and do a single write. I was running into performance issues writing thousands of times per second and this feature eliminated alot of overhead
