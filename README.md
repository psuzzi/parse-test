# Parse Test

This project is for comparing the performances of different parsers. 
It was build in order to solve this kolasu issue: https://github.com/Strumenta/kolasu/issues/354

Maven multi-module project with the following structure

```
parse-test
├── parse-test-antlr-v4
├── parse-test-benchmark
├── parse-test-common
└── parse-test-kolasu-v1-5
```

To run the benchmark, build the project, and then execute the tests under `parse-test-benchmark`

### Results

This project has a CI build, so you can check the results under https://github.com/psuzzi/parse-test/actions. 
- Drill down under the latest successful pipeline run > build > Test With Maven.
- In the console out, you can see INFO level output with JSON files representing the benchmark results

Below you see an example of how the result will show up in the console:

```
Aug 16, 2024 12:39:23 PM dev.algo.parsetest.benchmark.AntlrArithmeticExprBenchmarkTest setup
INFO: Initialize Test
Aug 16, 2024 12:39:23 PM dev.algo.parsetest.benchmark.AntlrArithmeticExprBenchmarkTest testBenchmarkArithmeticExprInFolder
INFO: {
  "folder_path" : "/tmp/junit14272317889945345094",
  "number_of_files" : 5,
  "total_parsing_time_ms" : 43,
  "total_memory_used_kb" : 3329,
  "benchmarks" : [ {
    "file_name" : "medium.ae",
    "parse_time_ms" : 29
  }, {
    "file_name" : "test1.ae",
    "parse_time_ms" : 0
  }, {
    "file_name" : "large.ae",
    "parse_time_ms" : 12
  }, {
    "file_name" : "small.ae",
    "parse_time_ms" : 0
  }, {
    "file_name" : "test2.ae",
    "parse_time_ms" : 0
  } ]
}
```


## Multi-module build

This is a multi-module project, not yet published to maven-central. If you want to contribute to it, you might have some issues.
Below, you see a few hints, in case you are stuck with it

### First Build
In case the first build doesn't run, and the `parse-test-common` is unpublished, you might want to try executing a `mvn install`

### Cleanup

In case you installed modules in your local maven cache, and you want to selectively clean, you can try this:
```
mvn dependency:purge-local-repository -DmanualInclude=dev.algo:parse-test,dev.algo:parse-test-antlr-v4,dev.algo:parse-test-benchmark,dev.algo:parse-test-common,,dev.algo:parse-test-kolasu-v1-5
```

## Future Changes

### Add more tests

To add more parser tests as additional modules, look at `parse-test-antlr-v4` or `parse-test-kolasu-v1-5`.
Once the parser is added, add a new XXXBenchmark into `parse-test-benchmark`

### Improve the build

Since this is a maven multi-module project, we could take advantage od the Maven's reactor, so:
  - run maven commands from the root directory of the multi-module project `parse-test`
  - use `mvn clean package` instead of `mvn install` to build your project without installing artifacts in your local repository
  - if you need to run a specific plugin goal for a module, use the `-pl` flag. For example: `mvn antlr4:antlr -pl parse-test-antlr-v4`
