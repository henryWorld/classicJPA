INSERT INTO sight_test (id, type, tr_number) VALUES ('17396D3B-FD1F-4454-B309-41990D705E6B', 'SIGHT_TEST', 8);

INSERT INTO rx_eye (id, sphere, cylinder, axis, visual_acuity, pupillary_distance, addition, prism_horizontal, prism_vertical)
    VALUES ('850F1DFF-05E7-448D-A388-84F956F2079B', '+1.50', '+1.25', 2.5, '3.00', 4, 5.5, null, null),
           ('DD82478A-E2E9-4D1E-8705-0FC3ACDA2137', '+1.50', '+1.25', 2.5, '3.00', 4, 5.5, null, null);

INSERT INTO rx (id, left_rx_eye_id, right_rx_eye_id, notes)
    VALUES ('8069EE41-EE8C-4CEB-A9BB-B3135D4F2160', '850F1DFF-05E7-448D-A388-84F956F2079B', 'DD82478A-E2E9-4D1E-8705-0FC3ACDA2137', 'hello');

INSERT INTO habitual_rx (id, sight_test_id, pair_number, age, clinician_name, rx_id)
    VALUES ('9A759CFA-E8D8-4DD8-932D-84F9DE23956C', '17396D3B-FD1F-4454-B309-41990D705E6B', 1, 3, 'Bob Bobson', '8069EE41-EE8C-4CEB-A9BB-B3135D4F2160');
