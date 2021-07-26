INSERT INTO rx (rx_id)
VALUES (1);

INSERT INTO record (record_id)
VALUES (123);

INSERT INTO name (name_id, first_name, last_name)
VALUES (10, 'Joe', 'Bloggs');

INSERT INTO staff (staff_id, name_id)
VALUES (20, 10);

INSERT INTO prescribed_rx (prescribed_rx_id, recall_period, rx_id)
VALUES (30, 12, 1);

INSERT INTO sight_test (sight_test_id, tr_number, staff_id, prescribed_rx_id)
VALUES (1, 123, 20, 30);

