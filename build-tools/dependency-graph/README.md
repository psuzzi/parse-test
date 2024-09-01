# Module Dependency Graph Generator

This tool generates a graph representation of the dependencies between modules in this multi-module Maven project.

## Prerequisites

- Python 3.x
- Graphviz (optional, for rendering the graph)

## Output
The script generates a DOT file named module_dependencies.dot, which can be visualized using various tools:

- Graphviz command-line tool
- IDEs with GraphViz plugins
- Online DOT viewers like GraphvizOnline

## Usage

This tool provides two wrappers: `generate_graph.bat` for Windows, and `generate_graph.sh` for Unix-like systems.

Below, for simplicity, you can see the manual usage for Unix-like systems.

Run the script
```bash
python3 generate_module_graph.py
```

To render the graph (if Graphviz is installed)
```bash
dot -Tpng output/module_dependencies.dot -o output/module_dependencies.png
```
