INSERT INTO product (name,description,price)
VALUES ('Tomato', 'Tomato', 100);

INSERT INTO franchise (id, order_date)
VALUES (1, '2021-11-30');

INSERT INTO person (id, dni, first_name, last_name)
VALUES (1, '30256477', 'Juan', 'Roman'), (2, '30256478', 'Walter', 'Samuel');

INSERT INTO employee (id, date_of_hire, position, salary, franchise_id)
VALUES (1, '2021-12-05', 'CTO', 100000.50, 1);

INSERT INTO customer (id, shipment_address, final_customer, cuit)
VALUES (2, 'Mitre 1234', true, '20-30256478-9');

INSERT INTO sale(total_amount, customer_id, employee_id)
VALUES (100.00, 2, 1);