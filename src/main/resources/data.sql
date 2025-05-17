-- 회원 데이터 삽입
INSERT INTO member (id, created_at, modified_at, preg_date, height, weight, bmi, diseases, pre_pregnant, has_morning_sickness, vegan_level, veg_proteins, banned_vegetables)
VALUES (UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), NOW(), NOW(), '2023-09-01', 165, 58, 21.3, NULL, true, false, 'FLEXITARIAN', '두부, 콩류', '가지, 브로콜리');

-- 식단 데이터 삽입
INSERT INTO diet (date, name, meal_type, image, ingredients, receipts, nutrients, member_id, created_at, modified_at)
VALUES 
('2024-05-01', '애플 베리 오트밀', 'BREAKFAST', 
'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCAAyADIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBR', 
'오트밀, 사과, 블루베리, 아몬드 밀크', 
'1. 오트밀에 아몬드 밀크를 넣고 끓입니다. 2. 사과와 블루베리를 올려 완성합니다.', 
'단백질: 8g, 탄수화물: 45g, 지방: 5g', 
UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), NOW(), NOW());

INSERT INTO diet (date, name, meal_type, image, ingredients, receipts, nutrients, member_id, created_at, modified_at)
VALUES 
('2024-05-01', '퀴노아 샐러드', 'LUNCH', 
'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCAAyADIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBR', 
'퀴노아, 아보카도, 토마토, 오이, 올리브 오일', 
'1. 퀴노아를 삶습니다. 2. 채소를 썰어 퀴노아와 함께 담습니다. 3. 올리브 오일로 간을 합니다.', 
'단백질: 12g, 탄수화물: 35g, 지방: 15g', 
UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), NOW(), NOW());

INSERT INTO diet (date, name, meal_type, image, ingredients, receipts, nutrients, member_id, created_at, modified_at)
VALUES 
('2024-05-01', '버섯 리소토', 'DINNER', 
'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCAAyADIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBR', 
'현미, 버섯, 양파, 식물성 치즈, 채소 스톡', 
'1. 양파를 볶습니다. 2. 현미를 넣고 볶다가 버섯을 넣습니다. 3. 채소 스톡을 넣고 끓입니다. 4. 식물성 치즈를 올려 완성합니다.', 
'단백질: 10g, 탄수화물: 50g, 지방: 8g', 
UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), NOW(), NOW());

INSERT INTO diet (date, name, meal_type, image, ingredients, receipts, nutrients, member_id, created_at, modified_at)
VALUES 
('2024-05-02', '그린 스무디 볼', 'BREAKFAST', 
'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCAAyADIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBR', 
'바나나, 시금치, 케일, 아보카도, 코코넛 물', 
'1. 재료를 모두 블렌더에 넣고 갈아줍니다. 2. 그래놀라와 함께 볼에 담아 완성합니다.', 
'단백질: 6g, 탄수화물: 30g, 지방: 12g', 
UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), NOW(), NOW());

INSERT INTO diet (date, name, meal_type, image, ingredients, receipts, nutrients, member_id, created_at, modified_at)
VALUES 
('2024-05-02', '렌틸 수프', 'LUNCH', 
'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCAAyADIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBR', 
'렌틸콩, 당근, 샐러리, 토마토, 양파, 마늘', 
'1. 양파와 마늘을 볶습니다. 2. 당근과 샐러리를 넣고 볶습니다. 3. 렌틸콩과 토마토를 넣고 끓입니다.', 
'단백질: 15g, 탄수화물: 40g, 지방: 2g', 
UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), NOW(), NOW());

INSERT INTO diet (date, name, meal_type, image, ingredients, receipts, nutrients, member_id, created_at, modified_at)
VALUES 
('2024-05-02', '두부 스테이크', 'DINNER', 
'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAMCAgICAgMCAgIDAwMDBAYEBAQEBAgGBgUGCQgKCgkICQkKDA8MCgsOCwkJDRENDg8QEBEQCgwSExIQEw8QEBD/2wBDAQMDAwQDBAgEBAgQCwkLEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBD/wAARCAAyADIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBR', 
'두부, 버섯, 양파, 간장, 참기름', 
'1. 두부를 으깨서 버섯과 양파를 넣고 섞습니다. 2. 스테이크 모양으로 성형하여 굽습니다. 3. 간장 소스를 만들어 끼얹고 참기름을 뿌립니다.', 
'단백질: 18g, 탄수화물: 10g, 지방: 12g', 
UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), NOW(), NOW()); 