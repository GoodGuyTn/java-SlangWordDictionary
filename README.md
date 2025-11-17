# Đồ Án #1: Slang Dictionary (Từ điển Slang Word)

**Môn học:** Lập trình ứng dụng Java (CSC13002)  
**Trường:** Đại học Khoa học Tự nhiên - ĐHQG TP.HCM

---

## 👨‍🎓 Thông tin sinh viên
* **Họ và tên:** Phan Trung Tuấn
* **MSSV:** 23127138
* **Lớp:** 23KTPM1
* **Video Demo:** 

---

## 📝 Giới thiệu
Ứng dụng **Slang Dictionary** là phần mềm tra cứu, quản lý và đố vui về các từ lóng (Slang Words). Ứng dụng được xây dựng bằng ngôn ngữ **Java** với giao diện đồ họa **JavaFX**, đáp ứng yêu cầu xử lý dữ liệu lớn (hơn 40.000 từ) với tốc độ tìm kiếm tức thì (< 1 giây).

---

## ✨ Tính năng chính
Ứng dụng bao gồm đầy đủ 10 chức năng theo yêu cầu đồ án:

1.  **🔍 Tìm kiếm theo Slang:** Tra cứu nhanh nghĩa của từ lóng.
2.  **📖 Tìm kiếm theo Definition:** Tìm ngược các từ lóng dựa trên từ khóa trong định nghĩa.
3.  **📜 Lịch sử tìm kiếm:** Tự động lưu và hiển thị danh sách các từ đã tra cứu.
4.  **➕ Thêm từ mới:** Hỗ trợ thêm từ lóng mới. Xử lý thông minh khi trùng từ (Ghi đè hoặc Tạo bản sao).
5.  **✏️ Chỉnh sửa (Edit):** Cho phép sửa nội dung từ lóng.
6.  **🗑️ Xóa (Delete):** Xóa từ lóng khỏi danh sách (có xác nhận).
7.  **🔄 Reset dữ liệu:** Khôi phục danh sách từ điển về trạng thái gốc ban đầu.
8.  **🎲 Random Slang:** Chức năng "On this day slang word" - Hiển thị từ ngẫu nhiên.
9.  **❓ Đố vui (Mode 1):** Cho Slang, đoán Definition (Trắc nghiệm 4 đáp án).
10. **❓ Đố vui (Mode 2):** Cho Definition, đoán Slang (Trắc nghiệm 4 đáp án).

---

## 🛠️ Công nghệ & Kỹ thuật sử dụng
* **Ngôn ngữ:** Java (JDK 8+)
* **Giao diện (GUI):** JavaFX
* **Cấu trúc dự án:** Layered Architecture (UI - Utils - Service - Repository - Model).
* **Cấu trúc dữ liệu:**
    * Sử dụng `HashMap` để lưu trữ dữ liệu, đảm bảo độ phức tạp thuật toán tìm kiếm là **O(1)** (đáp ứng yêu cầu tìm kiếm < 1s cho 100.000 từ).
    * Sử dụng `List` để lưu lịch sử.
* **Quản lý phiên bản:** Git & GitHub.

---

## 📂 Cấu trúc thư mục
```text
SlangDictionary/
├── data/
│   ├── slang.txt          # File dữ liệu gốc (Source)
│   ├── working_data.dat  # File làm việc (Sinh ra khi chạy app)
│   └── slang_history.txt  # File lưu lịch sử tìm kiếm
├── src/
│   ├── Model/         # Chứa đối tượng SlangWord
│   ├── Repository/    # Xử lý đọc/ghi file, cấu trúc dữ liệu Map
│   ├── Service/       # Xử lý logic (Random, Quiz, Search)
│   ├── ui/            # Giao diện JavaFX (MainApp)
│   ├── utils/         # Các tiện ích đọc file (FileUtils)
│   └── Launcher.java  # Chạy chương trình
├── out/
│   └── artifacts/     # Chứa file .jar để chạy
└── README.md