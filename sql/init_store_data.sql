ALTER TABLE stores ALTER COLUMN merchant_id DROP NOT NULL; --- merchant_id는  NULL 가능

INSERT INTO stores (
    id, created_at, modified_at, address, crowd_level, has_drive_through, image_url, latitude, longitude, name, opening_hours, phone, seating_capacity, merchant_id
) VALUES
      (1, NULL, NULL, '서울 강남구 강남대로 390', 'LOW', false, 'https://image.istarbucks.co.kr//upload/store/2017/08/%5B3478%5D_20170822020135_a2lao.png', NULL, NULL, '강남R점', '07:00-22:30', '1522-3230', 100, null),
      (2, NULL, NULL, '서울 강남구 강남대로 456 한석타워 2층 1-2호', 'LOW', false, 'https://image.istarbucks.co.kr//upload/store/2023/02/%5B9824%5D_20230203102338_io5t7.jpg', NULL, NULL, '강남대로점', '08:00-22:00', '1522-3231', 60, null),
      (3, NULL, NULL, '서울 강남구 학동로 419', 'LOW', false, 'https://image.istarbucks.co.kr//upload/store/2017/03/%5B3397%5D_20170328092242_kyldo.jpg', NULL, NULL, '강남구청정문DT점', '07:00-22:00', '1522-3232', 50, null),
      (4, NULL, NULL, '서울 강남구 테헤란로 520', 'LOW', false, 'https://image.istarbucks.co.kr//upload/store/2022/07/%5B4319%5D_20220704024046_xwja3.jpg', NULL, NULL, '삼성역점', '07:00-22:00', '1522-3233', 65, null),
      (5, NULL, NULL, '서울 강남구 도산대로 108', 'LOW', false, 'https://image.istarbucks.co.kr//upload/store/2022/01/%5B3858%5D_20220127064243_5khni.jpg', NULL, NULL, '신사역성일빌딩점', '07:00-22:00', '1522-3234', 40, null),
      (6, NULL, NULL, '서울 강남구 도산대로 442', 'LOW', false, 'https://image.istarbucks.co.kr//upload/store/2020/09/%5B3366%5D_20200919014258_y0mzi.jpg', NULL, NULL, '청담스타R점', '07:00-23:00', '1522-3235', 80, null),
      (7, NULL, NULL, '서울 강남구 압구정로 328', 'LOW', false, 'https://image.istarbucks.co.kr//upload/store/2020/10/%5B9545%5D_20201027103930_nyjip.jpg', NULL, NULL, '압구정로데오역점', '08:00-22:00', '1522-3236', 45, null),
      (8, NULL, NULL, '서울 강남구 테헤란로 145', 'LOW', false, 'https://image.istarbucks.co.kr//upload/store/2022/05/%5B4293%5D_20220511055826_8m4a3.jpg', NULL, NULL, '역삼역점', '07:00-22:00', '1522-3237', 55, null),
      (9, NULL, NULL, '서울 강남구 도곡로 112', 'LOW', false, 'https://image.istarbucks.co.kr//upload/store/2023/01/%5B3009%5D_20230118065252_xpepz.jpg', NULL, NULL, '도곡DT점', '07:00-21:00', '1522-3238', 25, null),
      (10, NULL, 