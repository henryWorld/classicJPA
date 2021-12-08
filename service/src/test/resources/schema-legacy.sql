DROP TABLE IF EXISTS `name`;
DROP TABLE IF EXISTS `option_recommendation`;
DROP TABLE IF EXISTS `prescribed_rx`;
DROP TABLE IF EXISTS `prescribed_option_recommendation`;
DROP TABLE IF EXISTS `record`;
DROP TABLE IF EXISTS `rx`;
DROP TABLE IF EXISTS `sight_test`;
DROP TABLE IF EXISTS `staff`;
DROP TABLE IF EXISTS `refracted_rx`;

CREATE TABLE `name`
(
    `name_id`    bigint(20) NOT NULL AUTO_INCREMENT,
    `first_name` varchar(100)          DEFAULT NULL,
    `last_name`  varchar(100) NOT NULL DEFAULT '',
    PRIMARY KEY (`name_id`)
);

CREATE TABLE `option_recommendation`
(
    `option_recommendation_id` int(11) NOT NULL DEFAULT '0',
    `name`                     varchar(255) DEFAULT NULL,
    `text`                     varchar(255) DEFAULT NULL,
    `display_sequence`         int(11) DEFAULT NULL,
    `text_token_key`           varchar(255) DEFAULT NULL,
    PRIMARY KEY (`option_recommendation_id`),
    UNIQUE KEY `display_sequence` (`display_sequence`)
);

CREATE TABLE `prescribed_rx`
(
    `prescribed_rx_id`    bigint(20) NOT NULL AUTO_INCREMENT,
    `recall_period`       int(11) DEFAULT NULL,
    `bal_sph`             varchar(10) DEFAULT NULL,
    `rx_id`               bigint(20) DEFAULT NULL,
    `outside_optician_id` int(11) DEFAULT NULL,
    PRIMARY KEY (`prescribed_rx_id`)
);

CREATE TABLE `prescribed_option_recommendation`
(
    `prescribed_option_recommendation_id` int(11) NOT NULL AUTO_INCREMENT,
    `option_recommendation_id`            int(11) DEFAULT NULL,
    `sight_test_id`                       int(11) DEFAULT NULL,
    PRIMARY KEY (`prescribed_option_recommendation_id`)
);

CREATE TABLE `record`
(
    `record_id`             int(11) NOT NULL AUTO_INCREMENT,
    `type`                  varchar(3) NOT NULL DEFAULT '',
    `customer_arrival_time` datetime            DEFAULT NULL,
    `appointment_id`        int(11) DEFAULT NULL,
    `customer_id`           bigint(20) DEFAULT NULL,
    `cl_appt_end_time`      datetime            DEFAULT NULL,
    `appointment_end_time`  datetime            DEFAULT NULL,
    PRIMARY KEY (`record_id`)
);

CREATE TABLE `rx`
(
    `rx_id`                                     bigint(20) NOT NULL AUTO_INCREMENT,
    `vision_right_as_string`                    varchar(10)   DEFAULT NULL,
    `vision_left_as_string`                     varchar(10)   DEFAULT NULL,
    `bin_vision_as_string`                      varchar(10)   DEFAULT NULL,
    `sph_right_as_string`                       varchar(10)   DEFAULT NULL,
    `sph_left_as_string`                        varchar(10)   DEFAULT NULL,
    `cyl_right_as_string`                       varchar(10)   DEFAULT NULL,
    `cyl_left_as_string`                        varchar(10)   DEFAULT NULL,
    `axis_right`                                decimal(4, 1) DEFAULT NULL,
    `axis_left`                                 decimal(4, 1) DEFAULT NULL,
    `near_add_right`                            decimal(4, 2) DEFAULT NULL,
    `near_add_left`                             decimal(4, 2) DEFAULT NULL,
    `inter_add_right`                           decimal(4, 2) DEFAULT NULL,
    `inter_add_left`                            decimal(4, 2) DEFAULT NULL,
    `dist_va_right_as_string`                   varchar(10)   DEFAULT NULL,
    `dist_va_left_as_string`                    varchar(10)   DEFAULT NULL,
    `dist_bin_va_as_string`                     varchar(10)   DEFAULT NULL,
    `near_va_right_as_string`                   varchar(10)   DEFAULT NULL,
    `near_va_left_as_string`                    varchar(10)   DEFAULT NULL,
    `pd_right`                                  decimal(3, 1) DEFAULT NULL,
    `pd_left`                                   decimal(3, 1) DEFAULT NULL,
    `bvd`                                       decimal(4, 2) DEFAULT NULL,
    `prism_distance_horizontal_left_as_string`  varchar(10)   DEFAULT NULL,
    `prism_distance_horizontal_right_as_string` varchar(10)   DEFAULT NULL,
    `prism_distance_vertical_left_as_string`    varchar(10)   DEFAULT NULL,
    `prism_distance_vertical_right_as_string`   varchar(10)   DEFAULT NULL,
    `prism_near_horizontal_left_as_string`      varchar(10)   DEFAULT NULL,
    `prism_near_horizontal_right_as_string`     varchar(10)   DEFAULT NULL,
    `prism_near_vertical_left_as_string`        varchar(10)   DEFAULT NULL,
    `prism_near_vertical_right_as_string`       varchar(10)   DEFAULT NULL,
    `notes`                                     text,
    PRIMARY KEY (`rx_id`)
);

CREATE TABLE `sight_test`
(
    `sight_test_id`                int(11) NOT NULL AUTO_INCREMENT,
    `appointment_type`             varchar(255)         DEFAULT NULL,
    `dispense_notes`               text,
    `status`                       varchar(15) NOT NULL DEFAULT '',
    `sa_authority_id`              int(11) NOT NULL DEFAULT '0',
    `prescribed_rx_id`             bigint(20) DEFAULT NULL,
    `refracted_rx_id`              bigint(20) DEFAULT NULL,
    `staff_id`                     int(11) DEFAULT NULL,
    `nhs_voucher_id`               int(11) DEFAULT NULL,
    `clinical_test_conditions_id`  int(11) DEFAULT NULL,
    `applanation_tonometry_id`     int(11) DEFAULT NULL,
    `objective_and_iop_id`         int(11) DEFAULT NULL,
    `tr_number`                    int(11) NOT NULL DEFAULT '0',
    `habitual_rx_id`               int(11) DEFAULT NULL,
    `symptom_id`                   int(11) DEFAULT NULL,
    `advice`                       varchar(255)         DEFAULT NULL,
    `added_to_basket`              char(1)              DEFAULT NULL,
    `life_style_id`                int(11) DEFAULT NULL,
    `sight_test_driver_answers_id` int(11) DEFAULT NULL,
    `old_tr_number`                int(11) DEFAULT NULL,
    `patient_referral_letter_id`   bigint(20) DEFAULT NULL,
    `abandoned_notes`              varchar(255)         DEFAULT NULL,
    PRIMARY KEY (`sight_test_id`),
    UNIQUE KEY `tr_number` (`tr_number`),
    UNIQUE KEY `old_tr_number` (`old_tr_number`)
);

CREATE TABLE `staff`
(
    `staff_id`                      int(11) NOT NULL AUTO_INCREMENT,
    `ipaddress`                     varchar(255) NOT NULL DEFAULT '',
    `gocnumber`                     varchar(255)          DEFAULT NULL,
    `local_practitioner_code`       varchar(255)          DEFAULT NULL,
    `opthalmic_list_number`         varchar(255)          DEFAULT NULL,
    `cyl_preference`                varchar(255)          DEFAULT NULL,
    `vapreference`                  varchar(255)          DEFAULT NULL,
    `user_name`                     varchar(12)           DEFAULT NULL,
    `password`                      varchar(12)           DEFAULT NULL,
    `til_id`                        int(11) DEFAULT NULL,
    `till_access_level_name`        varchar(255)          DEFAULT NULL,
    `inventory_access_level_name`   varchar(255)          DEFAULT NULL,
    `back_office_access_level_name` varchar(255)          DEFAULT NULL,
    `store_access_level_id`         int(11) DEFAULT NULL,
    `job_code_id`                   varchar(255)          DEFAULT NULL,
    `start_date`                    datetime              DEFAULT NULL,
    `leaving_date`                  datetime              DEFAULT NULL,
    `blocked`                       char(1)               DEFAULT NULL,
    `unsuccessful_login_attempts`   int(11) DEFAULT NULL,
    `name_id`                       bigint(20) DEFAULT NULL,
    `store_id`                      varchar(50)  NOT NULL DEFAULT '',
    `payee_staff_id`                int(11) DEFAULT NULL,
    `email`                         varchar(255)          DEFAULT NULL,
    PRIMARY KEY (`staff_id`)
);

CREATE TABLE `refracted_rx` (
  `refracted_rx_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `specific_add_right` decimal(4,2) DEFAULT NULL,
  `specific_add_left` decimal(4,2) DEFAULT NULL,
  `specific_add_reason` varchar(250) DEFAULT NULL,
  `current_specs_va_right_as_string` varchar(10) DEFAULT NULL,
  `current_specs_va_left_as_string` varchar(10) DEFAULT NULL,
  `rx_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`refracted_rx_id`)
);
