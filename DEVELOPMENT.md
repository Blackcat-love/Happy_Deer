## 前言
欢迎来到开发文档，在这里我将详细说明项目构架，数据库构成等方便快速上手开发

## 数据库
目前是数据库结构是:
- `ID` 是一个整数字段，自动递增，作为表的主键。
- `Date` 是一个日期字段，记录发生日期。
- `Time` 是一个时间字段，记录具体时间。
- `Frequency` 是一个整数字段，记录同一天中的操作次数，默认值为1。
- `Last_datetime` 是一个日期时间字段，记录上一次操作的时间。
- `Interval_time` 是一个整数字段，记录两次操作之间的时间间隔（以分钟为单位）。
- `Remarks` 是一个文本字段，用于存储任何额外的备注信息。
- `FOREIGN KEY (ID) REFERENCES Users(ID)` 是一个外键约束，假设你有一个名为 `Users` 的表，并且 `ID` 是用户的标识符。如果你不需要这个外键关系，可以去掉这一行。

SQL:
```sql
CREATE TABLE HealthRecords (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Date DATE NOT NULL,
    Time TIME NOT NULL,
    Frequency INT DEFAULT 1,
    Last_datetime DATETIME,
    Interval_time INT,
    Remarks TEXT,
    FOREIGN KEY (ID) REFERENCES Users(ID)
);
```
**例子:**

| ID   | Date       | Time     | Frequency | Last_datetime       | Interval_time | 备注                       |
| :--- | :--------- | :------- | :-------- | :------------------ | :------------ | :------------------------- |
| 1    | 2024-01-01 | 08:00:00 | 1         | -                   | -             | 用户A的第一次记录          |
| 2    | 2024-01-02 | 09:30:00 | 1         | 2024-01-01 08:00:00 | 1440          | 用户A的第二次记录，间隔1天 |
| 3    | 2024-01-02 | 11:00:00 | 2         | 2024-01-02 09:30:00 | 90            | 用户A同一天第二次操作      |
| 4    | 2024-01-03 | 10:15:00 | 1         | 2024-01-02 11:00:00 | 1290          | 用户B的第一次记录，间隔1天 |
| 5    | 2024-01-04 | 14:00:00 | 1         | 2024-01-03 10:15:00 | 10710         | 用户A的记录，间隔2天       |

