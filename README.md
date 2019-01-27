# LogSplitter 

Simple utility to parse a log file from [StreamSets Data Collector](http://github.com/streamsets/data-collector) and to split the output from each pipeline into it's own file for "ease of reading".
Exception stack traces are sent to the correct file, this is based on the pipeline name found in the log4j output which starts the stack trace.

Records which are not identifiable as being from a specific pipeline are not sent to any output file. Mostly, this is trace and exceptions from the Data Collector Framework, which are often messages on behalf of a pipeline.  A lot of this information is pipeline metadata telling of pipeline state changes  and errors, etc.

Currently we expect to find the log files in the present working directory and will create a new subdirectory
"out" then create the individual pipeline log files in that directory.

## Issues? 
-[ ] this is an example of a record which is output by the framework; is not picked up because the pipeline field is "-";
```2019-01-24 15:21:03,846 [user:*?] [pipeline:-] [runner:] [thread:managerExecutor-pool-3-thread-1] [stage:] INFO  StandaloneAndClusterPipelineManager - Removing runner for pipeline 'SQLServerCTtolocalfs554177f7-d736-4214-89fa-f66e84b84791::'0'```
-[ ] start args to specify source log file directory and maybe a different destination directory for the split-up log files?