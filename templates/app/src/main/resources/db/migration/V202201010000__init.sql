{% if inputs.migration_tool != "None" %}
{% if inputs.migration_tool == "Liquibase" %}
--liquibase formatted sql

-- changeset {{project_name}}:202101010000
{% endif %}
CREATE TABLE {{project_name|upper}}(ID INT PRIMARY KEY, NAME VARCHAR(255));
{% if inputs.migration_tool == "Liquibase" %}
-- rollback DROP TABLE {{project_name|upper}};
{% endif %}
{% endif %}
