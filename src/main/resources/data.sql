
INSERT INTO user
(username, password, email, role, registration_date, created_at, created_by)
VALUES
    (
        'admin',
        '$2a$10$7EqJtq98hPqEX7fNZaFWoOa6zKX9dCk3FZ8pJzG1h3hZ5i7kG6e6W',
        'admin@booklending.com',
        'ROLE_ADMIN',
        NOW(),
        NOW(),
        'SYSTEM'
    );

INSERT INTO book_category (id, code) VALUES
     (1, 'ROMANCE'),
     (2, 'HISTORICAL'),
     (3, 'FANTASY'),
     (4, 'SCI_FI'),
     (5, 'NON_FICTION'),
     (6, 'BIOGRAPHY'),
     (7, 'THRILLER'),
     (8, 'CRIME'),
     (9, 'CHILDREN'),
     (10, 'YOUNG_ADULT'),
     (11, 'CLASSIC'),
     (12, 'POETRY'),
     (13, 'DRAMA'),
     (14, 'ADVENTURE'),
     (15, 'SELF_HELP');

INSERT INTO book_condition (id, code) VALUES
      (1, 'NEW'),
      (2, 'LIKE_NEW'),
      (3, 'VERY_GOOD'),
      (4, 'GOOD'),
      (5, 'ACCEPTABLE'),
      (6, 'WORN'),
      (7, 'DAMAGED'),
      (8, 'HEAVILY_DAMAGED'),
      (9, 'MISSING_PAGES'),
      (10, 'ANNOTATED'),
      (11, 'LIBRARY_BINDING'),
      (12, 'OUT_OF_PRINT'),
      (13, 'COLLECTIBLE'),
      (14, 'EX_LIBRARY'),
      (15, 'DIGITAL_COPY_ONLY');