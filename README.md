# LogSplitter 

Simple utility to parse sdc log file to push output from each pipeline into it's own file for "ease of reading".

Records which are not identifiable as being from a specific pipeline are not send to any output file. 

Currently we expect to find the log files in the present working directory and will create a new subdirectory
"out" then create the individual pipeline log files in that directory.

## Issues? 
-[ ] this record is not picked up, since the pipeline field is "-";
```2019-01-24 15:21:03,846 [user:*?] [pipeline:-] [runner:] [thread:managerExecutor-pool-3-thread-1] [stage:] INFO  StandaloneAndClusterPipelineManager - Removing runner for pipeline 'SQLServerCTtolocalfs554177f7-d736-4214-89fa-f66e84b84791::'0'```
-[ ] better selection of framework messages?
-[ ] start args to specify source log file directory and maybe a different destination directory for the split-up log files?