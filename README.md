# LogSplitter 

Simple utility to parse sdc log file to push output from each pipeline into it's own file for "ease of reading".

Records which are not identifiable as being from a specific pipeline are not send to any output file. 

Currently we expect to find the log files in the present working directory and will create a new subdirectory
"out" then create the individual pipeline log files in that directory. 