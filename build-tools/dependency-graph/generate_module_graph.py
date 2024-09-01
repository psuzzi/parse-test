import xml.etree.ElementTree as ET
import os
import glob

# Configuration constants
PATH_TO_MVN_ROOT = '../../'
PATH_TO_OUTPUT = 'output/module_dependencies.dot'
PRINT_GROUP_ID = False  # Set to True to include group ID in output, False to exclude it
DEPS_TO_TRACK = ['antlr', 'xtext.testing']  # List of dependency names to track

# Calculate the absolute paths
BASE_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), PATH_TO_MVN_ROOT))
OUTPUT_FILE = os.path.abspath(os.path.join(os.path.dirname(__file__), PATH_TO_OUTPUT))

def find_pom_files():
    return glob.glob(os.path.join(BASE_DIR, '**/pom.xml'), recursive=True)

def extract_module_info(pom_file):
    tree = ET.parse(pom_file)
    root = tree.getroot()
    namespace = {'maven': 'http://maven.apache.org/POM/4.0.0'}

    group_id = root.find('./maven:groupId', namespace) or root.find('./maven:parent/maven:groupId', namespace)
    artifact_id = root.find('./maven:artifactId', namespace)

    group_id = group_id.text if group_id is not None else ''
    artifact_id = artifact_id.text if artifact_id is not None else ''

    dependencies = []
    for dep in root.findall('.//maven:dependency', namespace):
        dep_group_id = dep.find('maven:groupId', namespace).text
        dep_artifact_id = dep.find('maven:artifactId', namespace).text
        if dep_group_id == 'dev.algo' or any(tracked in dep_artifact_id.lower() for tracked in DEPS_TO_TRACK):
            dependencies.append(f"{dep_group_id}:{dep_artifact_id}")

    return (group_id, artifact_id), dependencies

def format_module_name(group_id, artifact_id):
    return f"{group_id}:{artifact_id}" if PRINT_GROUP_ID else artifact_id

def generate_dot_file(modules, output_file):
    with open(output_file, 'w') as f:
        f.write('digraph "Project Dependencies" {\n')
        for (group_id, artifact_id), deps in modules.items():
            module_name = format_module_name(group_id, artifact_id)
            for dep in deps:
                dep_group_id, dep_artifact_id = dep.split(':')
                dep_name = format_module_name(dep_group_id, dep_artifact_id)
                f.write(f'  "{module_name}" -> "{dep_name}";\n')
        f.write('}\n')

def main():
    # Ensure the output directory exists
    os.makedirs(os.path.dirname(OUTPUT_FILE), exist_ok=True)

    modules = {
        module_info: dependencies
        for pom_file in find_pom_files()
        for module_info, dependencies in [extract_module_info(pom_file)]
    }

    generate_dot_file(modules, OUTPUT_FILE)
    print(f"DOT file '{OUTPUT_FILE}' has been generated.")

    print("\nModules and their dependencies:")
    for (group_id, artifact_id), deps in modules.items():
        module_name = format_module_name(group_id, artifact_id)
        print(f"{module_name}:")
        for dep in deps:
            dep_group_id, dep_artifact_id = dep.split(':')
            dep_name = format_module_name(dep_group_id, dep_artifact_id)
            print(f"  - {dep_name}")

if __name__ == "__main__":
    main()