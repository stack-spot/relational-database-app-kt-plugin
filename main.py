from pathlib import Path

from templateframework.metadata import Metadata
from templateframework.runner import run
from templateframework.template import Template


def delete_empty_directory(path: Path):
    if path.is_dir() and next(path.iterdir(), None) is None:
        path.rmdir()


def delete_empty_file(path: Path):
    if path.is_file() and path.stat().st_size == 0:
        path.unlink()


class CleanEmptyMigrationSettings(Template):
    def post_hook(self, metadata: Metadata):
        db_resource_path = metadata.target_path.joinpath(
            'app', 'src', 'main', 'resources', 'db')

        changelog_path = db_resource_path.joinpath('changelog')
        delete_empty_file(changelog_path.joinpath('db.changelog-master.yaml'))
        delete_empty_directory(changelog_path)

        migration_path = db_resource_path.joinpath('migration')
        delete_empty_file(migration_path.joinpath('V202201010000__init.sql'))
        delete_empty_directory(migration_path)

        delete_empty_directory(db_resource_path)


if __name__ == '__main__':
    run(CleanEmptyMigrationSettings())
