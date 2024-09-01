import xml.etree.ElementTree as ET
import os
import glob
from typing import Dict, Tuple, List

# Configuration constants
PATH_TO_MVN_ROOT = '../../'
PATH_TO_OUTPUT = 'output/module_dependencies.dot'
PRINT_GROUP_ID = False  # Set to True to include group ID in output, False to exclude it
DEPS_TO_TRACK = ['antlr', 'xtext.testing']  # List of dependency names to track
LOCAL_GROUP = 'dev.algo' # Group Id for local dependencies, highlighted in lightblue
PRINT_ALL_PROJECTS = False # Set to True to include all main projects, False for only those with dependencies

# Calculate the absolute paths
BASE_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), PATH_TO_MVN_ROOT))
OUTPUT_FILE = os.path.abspath(os.path.join(os.path.dirname(__file__), PATH_TO_OUTPUT))

# Type aliases
ModuleInfo = Tuple[str, str]  # (group_id, artifact_id)
Dependencies = List[str] # List of dependency as "group_id:artifact_id"

def find_pom_files() -> List[str]:
    """Find all pom.xml files in the project."""
    return glob.glob(os.path.join(BASE_DIR, '**/pom.xml'), recursive=True)

def extract_module_info(pom_file: str) -> Tuple[ModuleInfo, Dependencies]:
    """Extract module information and dependencies from a pom.xml file."""
    tree = ET.parse(pom_file)
    root = tree.getroot()
    ns = {'maven': 'http://maven.apache.org/POM/4.0.0'}

    group_id = root.find('./maven:groupId', ns) or root.find('./maven:parent/maven:groupId', ns)
    artifact_id = root.find('./maven:artifactId', ns)

    group_id = group_id.text if group_id is not None else ''
    artifact_id = artifact_id.text if artifact_id is not None else ''

    # Extract matching dependencies with list-comprehensions
    dependencies = [
        # list of strings in format "groupId:artifactId"
        f"{dep.find('maven:groupId', ns).text}:{dep.find('maven:artifactId', ns).text}"
        for dep in root.findall('.//maven:dependency', ns)
        # Only include deps which belong to local group or which are tracked by us
        if (dep.find('maven:groupId', ns).text == LOCAL_GROUP or
            any(tracked in dep.find('maven:artifactId', ns).text.lower() for tracked in DEPS_TO_TRACK))
    ]

    return (group_id, artifact_id), dependencies

def format_module_name(group_id: str, artifact_id: str) -> str:
    return f"{group_id}:{artifact_id}" if PRINT_GROUP_ID else artifact_id

def generate_dot_file(modules: Dict[ModuleInfo, Dependencies], output_file: str):
    """Generate a DOT file representing the module dependencies."""

    def should_include(module: ModuleInfo, deps: Dependencies) -> bool:
        """Determine if a module should be included in the graph."""
        return PRINT_ALL_PROJECTS or deps or any(module[1] == dep.split(':')[1] for deps in modules.values() for dep in deps)

    # Collect modules to include in the graph
    modules_in_graph = {
        module
        for module, deps in modules.items()
        if should_include(module, deps)
    }

    # Add all dependencies to the graph
    for deps in modules.values():
        modules_in_graph.update(tuple(dep.split(':')) for dep in deps)

    with open(output_file, 'w') as f:
        f.write('digraph "Project Dependencies" {\n')
        f.write('  node [style=filled];\n')

        # define nodes
        for module in modules_in_graph:
            name = format_module_name(*module)
            color = "lightblue" if module[0] == LOCAL_GROUP else "white"
            f.write(f'  "{name}" [fillcolor={color}];\n')

        # define edges
        for module, deps in modules.items():
            if module in modules_in_graph:
                for dep in deps:
                    if tuple(dep.split(':')) in modules_in_graph:
                        f.write(f'  "{format_module_name(*module)}" -> "{format_module_name(*dep.split(':'))}";\n')

        f.write('}\n')

def main():
    # Ensure the output directory exists
    os.makedirs(os.path.dirname(OUTPUT_FILE), exist_ok=True)

    # Extract module information from all pom.xml
    modules = {
        module_info: dependencies
        for pom_file in find_pom_files()
        for module_info, dependencies in [extract_module_info(pom_file)]
    }

    # Generate the DOT file
    generate_dot_file(modules, OUTPUT_FILE)
    print(f"DOT file '{OUTPUT_FILE}' has been generated.")

    # Print modules included in the graph
    print("\nModules included in the graph:")
    for module, deps in modules.items():
        if PRINT_ALL_PROJECTS or deps or any(module[1] == dep.split(':')[1] for deps in modules.values() for dep in deps):
            print(f"  - {format_module_name(*module)}")

if __name__ == "__main__":
    main()