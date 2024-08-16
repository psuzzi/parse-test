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

To run the benchmark, build the project, and then execute the test under `parse-test-benchmark` 

## Notes

This is a multi-module project, not yet published to maven-central, so there will be some quirks.

### First Build

As the project is not published, at first setup, you will need to `install` the `parse-test-common` project

### Cleanup

To reproduce the fresh situation before any install, you can : 

```
git reset --hard
git clean -fdx
mvn dependency:purge-local-repository -DmanualInclude=dev.algo:parse-test,dev.algo:parse-test-antlr-v4,dev.algo:parse-test-benchmark,dev.algo:parse-test-common,,dev.algo:parse-test-kolasu-v1-5
```

### Future Changes

#### Add more tests

To add more parser tests as additional modules, look at `parse-test-antlr-v4` or `parse-test-kolasu-v1-5`.
Once the parser is added, add a new XXXBenchmark into `parse-test-benchmark`

#### Improve the build

Since this is a maven multi-module project, we could take advantage od the Maven's reactor, so:
  - run maven commands from the root directory of the multi-module project `parse-test`
  - use `mvn clean package` instead of `mvn install` to build your project without installing artifacts in your local repository
  - if you need to run a specific plugin goal for a module, use the `-pl` flag. For example: `mvn antlr4:antlr -pl parse-test-antlr-v4`
