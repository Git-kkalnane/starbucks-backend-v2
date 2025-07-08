-- 한글 정렬을 위한 Collation 설정
-- PostgreSQL에서 한글 정렬 개선

-- 1. 사용 가능한 collation 확인
SELECT collname FROM pg_collation WHERE collname LIKE '%ko%' OR collname LIKE '%korean%';

-- 2. ICU extension이 있는 경우 사용
CREATE COLLATION IF NOT EXISTS korean_collation (provider = icu, locale = 'ko-KR');

-- 3. 기본 C collation을 사용하는 대신 numeric 정렬 개선
ALTER TABLE beverage_items ALTER COLUMN name_ko TYPE VARCHAR(50) COLLATE "C";

-- 4. 또는 UTF8 기반 정렬 사용
-- ALTER TABLE beverage_items ALTER COLUMN name_ko TYPE VARCHAR(50) COLLATE "en_US.UTF-8";

-- 현재 가장 호환성이 좋은 방법: 데이터베이스 전체 locale 확인
SELECT datcollate, datctype FROM pg_database WHERE datname = current_database();

-- 컬럼별 collation 확인
SELECT column_name, collation_name
FROM information_schema.columns
WHERE table_name = 'beverage_items' AND column_name = 'name_ko';
