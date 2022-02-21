from pathlib import Path
from yaml import safe_load

from templateframework.metadata import Metadata
from templateframework.runner import run
from templateframework.template import Template


def delete_empty_directory(path: Path):
    if path.is_dir() and next(path.iterdir(), None) is None:
        path.rmdir()

def delete_empty_file(path: Path):
    if path.is_file() and path.stat().st_size == 0:
        path.unlink()

def remove_empty_files_directories(metadata: Metadata):
    db_resource_path = metadata.target_path.joinpath('app', 'src', 'main', 'resources', 'db')

    changelog_path = db_resource_path.joinpath('changelog')
    delete_empty_file(changelog_path.joinpath('db.changelog-master.yaml'))
    delete_empty_directory(changelog_path)

    migration_path = db_resource_path.joinpath('migration')
    delete_empty_file(migration_path.joinpath('V202201010000__init.sql'))
    delete_empty_directory(migration_path)

    delete_empty_directory(db_resource_path)

def merge_entrypoint(metadata: Metadata):
    docker_compose_file = metadata.target_path.joinpath('app', 'docker-compose.yml')
    database_host = metadata.inputs['dbms'].lower() + '-server:' + ("3306" if metadata.inputs['dbms'] != 'PostgreSQL' else "5432")
    merged_entrypoint = ["./wait-for.sh","-t","30",database_host,"--","java","-jar","app.jar"]

    if 'previous_entrypoint' in metadata.computed_inputs:
        if './wait-for.sh' not in metadata.computed_inputs['previous_entrypoint']:
            merged_entrypoint = ["./wait-for.sh","-t","30",database_host] + metadata.computed_inputs['previous_entrypoint']
        else:
            merged_entrypoint = metadata.computed_inputs['previous_entrypoint']

    original = docker_compose_file.read_text()
    result = original.replace('call_wait_for_here', '["'+'","'.join(merged_entrypoint)+'"]')
    docker_compose_file.write_text(result)

def save_current_entrypoint(metadata: Metadata):
    docker_compose_yml = safe_load(metadata.target_path.joinpath('app', 'docker-compose.yml').read_text())
    project_name = metadata.global_inputs["project_name"]
    if 'services' in docker_compose_yml and project_name in docker_compose_yml['services'] and 'entrypoint' in docker_compose_yml['services'][project_name]:
        metadata.computed_inputs["previous_entrypoint"] = docker_compose_yml['services'][project_name]["entrypoint"]
    return metadata

class CleanEmptyMigrationSettings(Template):
    def pre_hook(self, metadata: Metadata) -> Metadata:
        return save_current_entrypoint(metadata)

    def post_hook(self, metadata: Metadata):
        remove_empty_files_directories(metadata)
        merge_entrypoint(metadata)


if __name__ == '__main__':
    run(CleanEmptyMigrationSettings())
