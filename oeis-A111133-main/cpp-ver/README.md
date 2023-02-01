C++ version of the solution. Uses Boost 1.81.0 https://www.boost.org/users/download/ 
Built on Windows 10 using g++. If building manually, include the parameter "-Wl,--stack,128000000" and "-O3" on g++ to increase the stack size to 128MB on Windows.

For input = 30000 legodp2.exe calculates it in 3.158865 seconds using only 551.3MB of RAM. Not tested for 300000 because lack of RAM.
