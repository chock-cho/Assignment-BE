# Ringle-BE

---

- API Test 가능한 서버 배포 주소: http://43.201.123.185/swagger-ui/index.html#/

---

## ✏️ 사전 과제 요구 사항 분석

- 학생과 튜터 간 1:1 수업 예약을 위한 API 서비스
- 예약할 수 있는 수업의 타입: [30분 수업]과 [60분 수업]
    - 모든 수업은 정각(예: 10:00) 또는 30분 단위(예: 10:30)로 시작
- 튜터가 자신의 가능한 시간을 관리하고, 학생이 효율적으로 수업을 예약할 수 있도록 지원

## ✅ 실행 방법

### [로컬 서버 실행] 터미널에서 실행

본 실행 방법은 Linux OS, JAVA openjdk-17, MySQL 8.0, Gradle, Docker Compose가 설치되어있는 환경을 가정합니다.


1️⃣ github 레포지토리에서 클론을 받아옵니다.
```bash
git clone https://github.com/chock-cho/Ringle-BE.git
```

2️⃣ 프로젝트의 루트로 이동합니다.
```bash
cd Ringle-BE
```

3️⃣ `.env` 파일을 생성합니다. (환경 변수들의 안전한 관리를 위함) 
```bash
sudo vim .env
```

- `.env 파일`
    
    ```
    MYSQL_URL={mysql url: 로컬일 경우 localhost}
    MYSQL_PORT=3306
    MYSQL_DB={db이름}
    MYSQL_USER={db username}
    MYSQL_PW={db password}
    ```
    

4️⃣ 도커 이미지를 새로 빌드하고 컨테이너를 실행합니다. 
```bash
docker-compose up --build 
```

5️⃣ Swagger 접속
API Test를 위해,  http://localhost:8080/swagger-ui.html 로 접속해봅니다.

---
### [로컬 서버 실행] IDE에서 실행
본 상황은 IntelliJ  IDE 가 설치된 환경을 가정합니다.

1️⃣ 프로젝트 루트 디렉토리에 `.env` 파일을 생성합니다.
<br> ![image](https://github.com/user-attachments/assets/81b7b0b1-89db-4fd4-86f1-645569628701)

2️⃣ .env파일에 다음 정보를 입력합니다.

```
    MYSQL_URL={mysql url: 로컬일 경우 localhost}
    MYSQL_PORT=3306
    MYSQL_DB={db이름}
    MYSQL_USER={db username}
    MYSQL_PW={db password}
```
    
3️⃣ 상단의 `▶️` 버튼을 눌러 실행합니다.<br>
![image](https://github.com/user-attachments/assets/ec64bd17-6922-4257-91de-f029b5dfce5c)
<br>
4️⃣ Swagger 접속
API Test를 위해,  http://localhost:8080/swagger-ui.html 로 접속해봅니다.

## 기능

### 1. 튜터용 API:`/api/tutor`

---

### 1-1. 수업 가능 시간대 생성: `POST /api/tutor`

- 튜터가 지정된 타임슬롯(30분 단위)을 드래그하여, 수업 가능한 시간대를 생성

### 1-2. 수업 가능 시간대 삭제: `DELETE /api/tutor`

- 튜터가 생성된 수업 가능 시간대를 삭제

### 2. 학생용 API: `/api/student`

### 2-1. 수업 가능 시간대 조회: `GET /api/student/available-lectures`

- 특정 기간(`startDate-endDate`), 수업 길이(`lectureType`)에 따라 현재 가능한 시간대를 조회

### 2-2. 수업 가능한 튜터 조회: `GET /api/student/available-tutors`

- 특정 날짜(`date`), 시작 시간대(`startTimeSlot`)와 수업 길이(`lectureType`)에 따라 예약 가능한 튜터를 조회

### 2-3. 새로운 수업 신청: `POST /api/student/reservation`

- 조회한 수업 정보를 바탕으로, 수업을 예약

### 2-4. 예약된 수업 조회: `GET /api/student/myReservation`

- 학생이 신청한 수업 목록(예정된 수업 및 완료된 수업)을 확인

![Ringle-인턴-과제-수행-회고록](https://github.com/user-attachments/assets/f73da75a-ccb4-41fd-a50d-d743b8d87b1f)
