INSERT INTO rx (rx_id, vision_right_as_string, vision_left_as_string, bin_vision_as_string, sph_right_as_string,
                sph_left_as_string, cyl_right_as_string, cyl_left_as_string, axis_right, axis_left, near_add_right,
                near_add_left, inter_add_right, inter_add_left, dist_va_right_as_string, dist_va_left_as_string,
                dist_bin_va_as_string, near_va_right_as_string, near_va_left_as_string, pd_right, pd_left, bvd,
                prism_distance_horizontal_left_as_string, prism_distance_horizontal_right_as_string,
                prism_distance_vertical_left_as_string, prism_distance_vertical_right_as_string,
                prism_near_horizontal_left_as_string, prism_near_horizontal_right_as_string,
                prism_near_vertical_left_as_string, prism_near_vertical_right_as_string, notes)
VALUES (1, NULL, NULL, NULL, '-3.50', '-3.25', '-1.00', '-0.50', '50', '115', '2.50', '2.25', '5', 8, '6/6', '6/6',
        '6/5', 'N5', 'N5', '29.0', '29.0', 12, '4.00 Out', '3.00 Out', '7.00 Up', '7.00 Down', '2.50 In', '2.00 In',
        '6.00 Down', '7.00 Down', 'Some notes');


INSERT INTO record (record_id, customer_arrival_time)
VALUES (123, '2021-04-19 12:00:00');

INSERT INTO name (name_id, first_name, last_name)
VALUES (10, 'Joe', 'Bloggs');

INSERT INTO staff (staff_id, name_id)
VALUES (20, 10);

INSERT INTO prescribed_rx (prescribed_rx_id, recall_period, rx_id)
VALUES (30, 12, 1);

INSERT INTO sight_test (sight_test_id, tr_number, staff_id, prescribed_rx_id, dispense_notes)
VALUES (1, 123, 20, 30, 'Some dispense notes');

INSERT INTO option_recommendation (option_recommendation_id, text)
VALUES (1, 'No RX'),
       (2, 'No Change');

INSERT INTO prescribed_option_recommendation (option_recommendation_id, sight_test_id)
VALUES (1, 1),
       (2, 1);




