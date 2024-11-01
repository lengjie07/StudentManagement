# プロジェクト概要
このプロジェクトは、オンラインスクールの受講生を管理するためのアプリケーションです。<br>
受講生および受講しているコース情報の登録、検索、更新、削除(論理削除)を行うことができます。

# ER図
![受講生管理システム_ER図](https://github.com/user-attachments/assets/7af64527-e8c5-4593-a5ae-e3af7874b72f)

# インフラ構成図
![インフラ構成図](https://github.com/user-attachments/assets/5965c44e-c33f-4b8c-a69f-b234f11087eb)

# 機能紹介
### 1. 検索機能
①条件付き検索<br>
![条件付き検索](https://github.com/user-attachments/assets/e386a130-b278-4edb-b701-fe41031f6b94)
- 条件を指定して検索することができます
- 条件を指定しなかった場合、全件を返します
- 複数条件、部分一致検索も可能です

②ID指定検索<br>
![ID指定検索](https://github.com/user-attachments/assets/a517efc2-1b92-4d36-9c42-dc5a5804dc27)
- 受講生IDを指定して検索する機能
- 情報を更新する際に、受講生IDに紐づく受講生情報を取得する想定

### 2. 登録機能
![登録機能](https://github.com/user-attachments/assets/f25510ab-da4f-42c5-8652-f371d6a490dc)
- 各IDは`Auto Increment`による自動採番
- `StudentCourse`の`startDate`は現在の日時を、`endDate`は現在の日時から1年後の日時を自動で設定
```
private void initStudentCourse(StudentCourse studentCourse, Student student) {
    LocalDateTime now = LocalDateTime.now();

    studentCourse.setStudentId(student.getId()); // 登録した受講生のIDをセット
    studentCourse.setStartDate(now); // 開始日を設定
    studentCourse.setEndDate(now.plusYears(1)); // 終了日を開始日の1年後に設定
  }
```
- `CourseApplicationStatus`の`status`はデフォルトで「仮申込」を設定
```
courseApplicationStatus.setCourseId(studentCourse.getId());
      if (courseApplicationStatus.getStatus() == null) {
        courseApplicationStatus.setStatus("仮申込");
      }
```

### 3. 更新機能
![更新機能](https://github.com/user-attachments/assets/1d8b84ad-f178-4163-a098-0ca7c6569bf4)
- `Student`の`isDeleted`を`true`にすることで論理削除ができます

# 工夫したポイント
### 1. 条件付き検索機能のSQL
- 条件付き検索を行う際に、`INNER JOIN`を用いて、各テーブルからまとめて情報を取得
- 指定されていない条件は無視されるように`if`タグを使用
- 取得した情報を、受講生ごとに情報をまとめた`StudentDetail`オブジェクトにマッピング
```
<!-- 条件を指定して受講生詳細を検索 -->
  <select id="searchStudentDetail" resultMap="StudentDetailResultMap">
    SELECT
      s.id AS student_id,
      s.full_name,
      s.furigana,
      s.nickname,
      s.email_address,
      s.area,
      s.age,
      s.gender,
      s.remark,
      s.is_deleted,
      sc.id AS student_course_id,
      sc.student_id,
      sc.course_name,
      sc.start_date,
      sc.end_date,
      cas.id AS course_application_status_id,
      cas.course_id,
      cas.status
    FROM
      students s
    INNER JOIN
      students_courses sc ON s.id = sc.student_id
    INNER JOIN
      course_application_statuses cas ON sc.id = cas.course_id
    WHERE
      1=1
      <if test="fullName != null and fullName != ''">
        AND s.full_name LIKE CONCAT('%', #{fullName}, '%')
      </if>
      <if test="furigana != null and furigana != ''">
        AND s.furigana LIKE CONCAT('%', #{furigana}, '%')
      </if>
      <if test="nickname != null and nickname != ''">
        AND s.nickname LIKE CONCAT('%', #{nickname}, '%')
      </if>
      <if test="emailAddress != null and emailAddress != ''">
        AND s.email_address LIKE CONCAT('%', #{emailAddress}, '%')
      </if>
      <if test="area != null and area != ''">
        AND s.area LIKE CONCAT('%', #{area}, '%')
      </if>
      <if test="age != null">
        AND s.age = #{age}
      </if>
      <if test="gender != null and gender != ''">
        AND s.gender = #{gender}
      </if>
      <if test="remark != null and remark != ''">
        AND s.remark LIKE CONCAT('%', #{remark}, '%')
      </if>
    <if test="isDeleted != null">
      AND s.is_deleted = #{isDeleted}
    </if>
      <if test="courseName != null and courseName != ''">
        AND sc.course_name LIKE CONCAT('%', #{courseName}, '%')
      </if>
      <if test="startDate != null">
        AND sc.start_date &gt;= #{startDate, jdbcType=TIMESTAMP}
      </if>
      <if test="endDate != null">
        AND sc.end_date &lt;= #{endDate, jdbcType=TIMESTAMP}
      </if>
      <if test="status != null and status != ''">
        AND cas.status LIKE CONCAT('%', #{status}, '%')
      </if>
  </select>
```
```
<!-- StudentDetail のマッピング -->
  <resultMap id="StudentDetailResultMap" type="raisetech.studentmanagement.domain.StudentDetail">
    <!-- Student のマッピング -->
    <association property="student" javaType="raisetech.studentmanagement.data.Student">
      <id column="student_id" property="id"/>
      <result column="full_name" property="fullName"/>
      <result column="furigana" property="furigana"/>
      <result column="nickname" property="nickname"/>
      <result column="email_address" property="emailAddress"/>
      <result column="area" property="area"/>
      <result column="age" property="age"/>
      <result column="gender" property="gender"/>
      <result column="remark" property="remark"/>
      <result column="is_deleted" property="isDeleted"/>
    </association>

    <!-- StudentCourseDetail のマッピング -->
    <collection property="studentCourseDetailList" ofType="raisetech.studentmanagement.domain.StudentCourseDetail">
      <!-- StudentCourse のマッピング -->
      <association property="studentCourse" javaType="raisetech.studentmanagement.data.StudentCourse">
        <id column="student_course_id" property="id"/>
        <result column="student_id" property="studentId"/>
        <result column="course_name" property="courseName"/>
        <result column="start_date" property="startDate"/>
        <result column="end_date" property="endDate"/>
      </association>

      <!-- CourseApplicationStatus のマッピング -->
      <association property="courseApplicationStatus" javaType="raisetech.studentmanagement.data.CourseApplicationStatus">
        <id column="course_application_status_id" property="id"/>
        <result column="course_id" property="courseId"/>
        <result column="status" property="status"/>
      </association>
    </collection>
  </resultMap>
```

### 2. 重複した受講生の統合処理
- 上記のSQLでは、複数のコースを受講している場合、別レコードとして取得される
- `StudentConverter`クラスで一つの`StudentDetail`オブジェクトにまとめる処理を行う
```
  public List<StudentDetail> convertSearchedStudentDetailList(
      List<StudentDetail> studentDetailList) {
    Map<Integer, StudentDetail> studentDetailMap = new HashMap<>();

    studentDetailList.forEach(studentDetail -> {
      int studentId = studentDetail.getStudent().getId();
      if (studentDetailMap.containsKey(studentId)) {
        studentDetailMap.get(studentId).getStudentCourseDetailList()
            .addAll(studentDetail.getStudentCourseDetailList());
      } else {
        studentDetailMap.put(studentId, studentDetail);
      }
    });
    return new ArrayList<>(studentDetailMap.values());
  }
```

### 3. GitHub Actionsを用いたCI/CD処理
```
name: Java CD with Gradle

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

env:
  EC2_USER: 'ec2-user'
  EC2_HOST: '52.197.219.38'
  SRC_PATH: 'build/libs/*.jar'
  DEST_PATH: '/home/ec2-user/StudentManagement.jar'

jobs:
  deploy:

    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'

      - name: Setup Gradle
        uses: gradle/actions/setup-gradle@af1da67850ed9a4cedd57bfd976089dd991e2582 # v4.0.0

      - name: Grant execute permission for gradlew
        run: chmod +x gradlew

      - name: Test with Gradle Wrapper
        run: ./gradlew test

      - name: Upload Test Report
        if: failure()
        uses: actions/upload-artifact@v3
        with:
          name: test-report
          path: build/reports/tests/test

      - name: Build with Gradle Wrapper
        run: ./gradlew bootJar

      - name: SCP Copy Application to EC2
        env:
          PRIVATE_KEY: ${{ secrets.AWS_EC2_PRIVATE_KEY }}
        run: |
          echo "$PRIVATE_KEY" > private_key && chmod 600 private_key
          scp -o StrictHostKeyChecking=no -i private_key $SRC_PATH $EC2_USER@$EC2_HOST:$DEST_PATH

      - name: SSH Application Deploy
        uses: appleboy/ssh-action@master
        with:
          host: ${{ env.EC2_HOST }}
          username: ${{ env.EC2_USER }}
          key: ${{ secrets.AWS_EC2_PRIVATE_KEY }}
          envs: DEST_PATH
          script: |
            sudo yum update -y
            if sudo systemctl status StudentManagement 2>&1 | grep "Active: active (running)" ; then
              sudo systemctl restart StudentManagement
            else
              sudo systemctl start StudentManagement
            fi
```
