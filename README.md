# Movie Theater MVC


Dự án Movie Theater được xây dựng theo mô hình Spring MVC, cung cấp các chức năng quản lý rạp chiếu phim như quản lý phim, lịch chiếu, tài khoản và đặt vé.

---

## Mục Lục

1. [Giới Thiệu]
2. [Yêu Cầu Hệ Thống]
3. [Cách Cài Đặt]
4. [Cách Sử Dụng]

---

## Giới Thiệu

> Dự án này là một hệ thống quản lý rạp chiếu phim với các chức năng chính:
> - Quản lý phim và lịch chiếu.
> - Quản lý nhân viên
> - Quản lý vé xem phim
> - Cập nhật thông tin người dùng
> - Đặt vé xem phim

---

## Yêu Cầu Hệ Thống

- **Ngôn ngữ/Libraries**: Java 22, Spring Boot, Hibernate, Thymeleaf
- **Cơ sở dữ liệu**: Microsoft SQL Server
- **Công cụ phát triển**: Maven, IntelliJ IDEA

---

## Cách Cài Đặt

### 1. Clone repository
```bash
git clone https://github.com/SoraWork/movietheater_mvc.git
cd movietheater_mvc
```

### 2. Cấu hình cơ sở dữ liệu

> **Lưu ý:** Đảm bảo bạn đã cài đặt và cấu hình Microsoft SQL Server trên máy của mình.

1. Mở file `src/main/resources/application.properties`.
2. Cập nhật thông tin kết nối cơ sở dữ liệu như sau:
```properties

# SQL Server Configuration
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=movietheater;encrypt=true;trustServerCertificate=true;loginTimeout=30;
spring.datasource.username=your_database_username
spring.datasource.password=your_database_password
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
# Giới hạn kích thước ảnh
spring.servlet.multipart.max-file-size=1000MB
spring.servlet.multipart.max-request-size=1000MB

```
### 3. Cách sử dụng
```properties

# Sử dụng Maven để build dự án:
mvn clean install
# Chạy ứng dụng
mvn spring-boot:run
# Mở trình duyệt và chạy
localhost:8080
```
## Kết quả
1. Giao diện đăng nhập
![image](https://github.com/user-attachments/assets/7401f591-5aad-44aa-9f66-d975cc643b14)
2. Giao diện đăng ký
![image](https://github.com/user-attachments/assets/9826b17e-152a-4ba3-a186-ae8f7160e31f)
3. Giao diện trang chủ chưa đăng nhập
![image](https://github.com/user-attachments/assets/17af51ca-817c-4152-88c6-79d84e650ced)
4. Giao diện quản lý thông tin người dùng
![image](https://github.com/user-attachments/assets/e57a3176-2c7e-4af5-b627-6394a6f2efdd)
5. Giao diện quản lý lịch sử vé xem phim của từng người
![image](https://github.com/user-attachments/assets/6589d73c-f6a0-49b1-aca9-07ab6c0577ee)
6. Giao diện quản lý nhân viên
![image](https://github.com/user-attachments/assets/7844fac4-25a4-4486-9f4b-764d3d9db757)
7. Giao diện quản lý phim
![image](https://github.com/user-attachments/assets/5ad338b6-b4d6-4084-b398-6253e63844d9)
8. Giao diện quản lý phòng chiếu
![image](https://github.com/user-attachments/assets/c4568347-2db7-462a-9c0c-3d9c46207933)
9. Giao diện quản lý vé xem phim
![image](https://github.com/user-attachments/assets/a97068a3-0451-4970-8add-139ef5b39915)
10. Giao diện chọn ghế xem phim
![image](https://github.com/user-attachments/assets/70123d5a-8a67-4a17-83c4-1df562bcc5ff)
11. Giao diện xác nhận thông tin vé xem phim
![image](https://github.com/user-attachments/assets/3bbcdb51-d4c8-48d4-8e21-77531204a047)














