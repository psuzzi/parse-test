# Parse Test

This project is for comparing the performances of different parsers.
It was build in order to solve this kolasu issue: https://github.com/Strumenta/kolasu/issues/354

See also this YT video with explanation:
https://www.youtube.com/watch?v=owIi5YvDOmA

The benchmarks are under `parse-test-benchmark`.

## Prerequisites
- Java 17
- Maven 3

## Benchmark

A simple way to run the benchmark is to run `mvn clean package`, which includes running al tests.

```
git clone git@github.com:psuzzi/parse-test.git
cd parse-test
# Build with Maven
mvn -B package --file pom.xml
# Test with Maven
mvn test
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
In the CI, the build / test is executed as follows:

```
mvn clean
mvn clean install -U
# Build with Maven
mvn -B package --file pom.xml
# Test with Maven
mvn test
```

To find the latest results:
- open https://github.com/psuzzi/parse-test/actions.
- Drill down under the latest successful pipeline run > build > Test With Maven.
- In the console out, search for `FULL TEST` preceding the JSON result

## Architecture

This is a maven multi-module project, not yet published on maven central.
As you can see below, we use multiple languages.
Note that kotlin and xtend artefacts have to be generated by maven.

```
parse-test                      [pom] parent pom
├── parse-test-antlr-v4         [jar] antlr parser (java)
├── parse-test-benchmark        [jar] benchmark depend on: common, antlr, kolasu, xtext (java/kotlin)
├── parse-test-common           [jar] common classes (java)
├── parse-test-kolasu-v1-5      [jar] kolasu parser (kotlin)
└── parse.test.xtext.parent     [pom] xtext parser parent [pom]
    └── parse.test.xtext        [jar] xtext parser (java/xtend)
```

I choose to use maven to simplify the usage of some legacy framework.
The subprojects are written in Java and Kotlin.
- As an example of a Java parser project, see `parse-test-antlr-v4`
- As an example of a Kotlin/Java parser, see `parse-test-kolasu-v1-5`

### Build

In general, I prefer building with `mvn clean package`.
That's because `mvn clean install` pollutes my local maven repo.

The GitHub CI is governed by the `.github/workflows/maven.yml`, which executes:
```
mvn -B package --file pom.xml
mvn test
```

If you need to run a specific plugin goal for a module, use the `-pl` flag from the root project.
For instance `mvn antlr4:antlr -pl parse-test-antlr-v4` generates the ANTLR artefacts.

You can cleanly build submodules starting from the root dir and executing the following commands in order:

```
mvn clean
mvn package -pl parse-test-common -am
mvn package -pl parse-test-antlr-v4 -am
mvn package -pl parse-test-kolasu-v1-5 -am
mvn package -pl parse.test.xtext.parent -am
mvn package -pl parse-test-benchmark -am
```

In case of issues with the first build, feel free use the Maven Reactor:
When building use `mvn clean install -U` from the parent directory.
This will ensure all modules are built in the correct order, using the most Updated version.

After installing the modules locally, you can selectively cleanup your local mvn repo as follows:

```
mvn dependency:purge-local-repository -DmanualInclude=dev.algo:parse-test,dev.algo:parse-test-antlr-v4,dev.algo:parse-test-benchmark,dev.algo:parse-test-common,dev.algo:parse-test-kolasu-v1-5,dev.algo:parse.test.xtext.parent,dev.algo:parse.test.xtext
```

## Dependencies

![module_dependencies.png](build-tools/dependency-graph/output/module_dependencies.png)

To check the dependencies, you can use the [dependency-graph](build-tools/dependency-graph) tool provided as part of the build tools:

```bash
cd build-tools/dependency-graph
python3 generate_module_graph.py
# optional, if graphviz is installed
dot -Tpng output/module_dependencies.dot -o output/module_dependencies.png 
```

Alternatively, you can use the default maven plugin with `mvn dependency:tree`.

## Troubleshooting

When building Xtext inside IntelliJ IDEA, you might have issues, because of:  [xtext/issue/1953 ](https://github.com/eclipse/xtext/issues/1953)and companion [issue/IDEA-262695](https://youtrack.jetbrains.com/issue/IDEA-262695)
I solved this crash of the Mwe2 by removing the `plexus-classworlds.license` file from IDEA's maven.
A different option is to configure IDEA to use a different maven version.
If you run all via command line, there is no issue.


## Contributing

Feel free to open issues
To contribute improvements or add more parser tests, just open a PR.
For adding a new parser modules, look at `parse-test-antlr-v4` (Java) or `parse-test-kolasu-v1-5` (Kotlin).
For adding a new Parser Benchmark, look at [MockParserBenchmarkTest.java](parse-test-common/src/test/java/MockParserBenchmarkTest.java) in `parse-test-common`

## License

This repository is dual-licensed Apache2 and EPL 2.0
