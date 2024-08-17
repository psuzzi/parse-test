# Parse Test

This project is for comparing the performances of different parsers. 
It was build in order to solve this kolasu issue: https://github.com/Strumenta/kolasu/issues/354

The benchmarks are under `parse-test-benchmark`.

## Prerequisites
- Java 17
- Maven 3

## Benchmark

A simple way to run the benchmark is to run `mvn clean package`, which includes running al tests.

```
git clone git@github.com:psuzzi/parse-test.git
cd parse-test
mvn clean package
```

The results are printed by tests, in JSON format: 

```
Aug 16, 2024 5:29:48 PM dev.algo.parsetest.benchmark.AntlrArithmeticExprBenchmarkTest testFullBenchmarkArithmeticExprInProvidedFolder
INFO: FULL TEST
{
  "folder_path" : "./parse-test/parse-test-benchmark/target/test-classes/arithmetic_expr_gen",
  "number_of_files" : 90,
  "total_parsing_time_ms" : 571,
  "total_memory_used_kb" : 290325
}
```

### CI Results

This project has a CI build.
Check the results under https://github.com/psuzzi/parse-test/actions. 
- Drill down under the latest successful pipeline run > build > Test With Maven.
- In the console out, search for `FULL TEST` preceding the JSON result

## Architecture

This is a maven multi-module project, not yet published on maven central. 

```
parse-test
├── parse-test-antlr-v4
├── parse-test-benchmark
├── parse-test-common
└── parse-test-kolasu-v1-5
```

I choose to use maven to simplify the usage of some legacy framework.
The subprojects are written in Java and Kotlin. 
- As an example of a Java parser project, see `parse-test-antlr-v4`
- As an example of a Kotlin/Java parser, see `parse-test-kolasu-v1-5`

### Build

In general, I prefer building with `mvn clean package`.
That's because `mvn clan install` pollutes my local maven repo.

If you need to run a specific plugin goal for a module, use the `-pl` flag from the root project.
For instance `mvn antlr4:antlr -pl parse-test-antlr-v4` generates the ANTLR artefacts.

In case of issues with the first build, feel free use the Maven Reactor: 
When building use `mvn clean install -U` from the parent directory.
This will ensure all modules are built in the correct order, using the most Updated version.

After installing the modules locally, you can selectively cleanup as follows

```
mvn dependency:purge-local-repository -DmanualInclude=dev.algo:parse-test,dev.algo:parse-test-antlr-v4,dev.algo:parse-test-benchmark,dev.algo:parse-test-common,,dev.algo:parse-test-kolasu-v1-5
```

The GitHub CI is governed by the `.github/workflows/maven.yml`

## Contributing

Feel free to open issues
To contribute improvements or add more parser tests, just open a PR.
For adding a new parser modules, look at `parse-test-antlr-v4` or `parse-test-kolasu-v1-5`.
For adding a new XXXBenchmark, look into `parse-test-benchmark`

## License

This repository is dual-licensed Apache2 and EPL 2.0
