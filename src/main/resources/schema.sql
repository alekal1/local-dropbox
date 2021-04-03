CREATE TABLE directory (
  id               IDENTITY     NOT NULL,
  parent_dir_id    BIGINT,
  name             VARCHAR(255) NOT NULL,
  created_on       DATE         NOT NULL,
  last_accessed_on DATE,
  dir_path  VARCHAR(255)        NOT NULL,


  CONSTRAINT directory_pk PRIMARY KEY (id),
  FOREIGN KEY (parent_dir_id) REFERENCES directory (id)
);

CREATE TABLE file (
  id               IDENTITY     NOT NULL,
  parent_dir_id    BIGINT       NOT NULL,
  name             VARCHAR(255) NOT NULL,
  created_on       DATE         NOT NULL,
  last_accessed_on DATE,
  size             BIGINT       NOT NULL,
  file_path        VARCHAR(255) NOT NULL

  CONSTRAINT file_pk PRIMARY KEY (id),
  FOREIGN KEY (parent_dir_id) REFERENCES directory (id)
);
