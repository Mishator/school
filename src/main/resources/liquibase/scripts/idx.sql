--liquibase formatted sql

--changeset mvoyteh:1
CREATE INDEX name_idx
ON student (name);

--changeset mvoyteh:2
CREATE INDEX name_and_color_idx
ON faculty (name, color);
