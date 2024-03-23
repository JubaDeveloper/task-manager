insert into
    test(name)
values (
    'JUBA'
);

insert into
    "User" (id, name, email, password, roles)
values (
    10,
    'Juba',
    'juba@gmail.com',
    '$2a$12$ws0EiWH8i6RZCski4Ba5weHMbLf4mjnF9.iIn.bUxrTpUBKhzDwsy',
    ('ROLE_ADMIN')
);

insert into
    Task (id, title, description, finished, user_id)
values (
    10,
    'My task',
    'I should wake up and brush my teeth',
    false,
    10
);