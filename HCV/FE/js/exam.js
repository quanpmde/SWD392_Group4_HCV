document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem("authToken");
    const sidebarNav = document.getElementById('sidebar-nav');
    const examList = document.getElementById('exam-list');
    const examDetail = document.getElementById('exam-detail');
    const backButton = document.getElementById('back-button');
    const searchContainer = document.querySelector('.search-container'); // Thanh tìm kiếm
    const takeExamButton = document.getElementById('take-exam-button');
    const commentSection = document.getElementById('comment-section');


    // Hàm giải mã JWT để lấy userId từ token
    function getUserIdFromToken(token) {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.userId; // Lấy userId từ payload
    }

    const userId = getUserIdFromToken(token);

    // Tạo phần tử "Tất cả" ở đầu sidebar và gán sự kiện hiển thị tất cả các exam khi nhấn vào
    const allExamsLink = document.createElement('li');
    allExamsLink.classList.add("nav-item");
    allExamsLink.innerHTML = `
        <a href="#" class="nav-link" id="all-exams">
            <i class="bi bi-list"></i><span>Tất cả</span>
        </a>
    `;
    sidebarNav.prepend(allExamsLink);

    // Sự kiện khi nhấn vào "Tất cả" để hiển thị tất cả các exam
    allExamsLink.addEventListener("click", function (event) {
        event.preventDefault();
        fetchAndDisplayAllExams();
    });

    // Hàm lấy tất cả exam từ các subject
    function fetchAndDisplayAllExams() {
        fetch("http://localhost:8080/Elearning/exam", {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                examList.innerHTML = '';
                const subjectsMap = new Map();

                // Phân loại exam theo từng subject
                data.result.forEach(exam => {
                    if (!subjectsMap.has(exam.subjectName)) {
                        subjectsMap.set(exam.subjectName, []);
                    }
                    subjectsMap.get(exam.subjectName).push(exam);
                });

                // Hiển thị exam theo từng subject
                subjectsMap.forEach((exams, subjectName) => {
                    const subjectHeading = document.createElement('h4');
                    subjectHeading.textContent = subjectName;
                    examList.appendChild(subjectHeading);

                    exams.forEach(exam => {
                        const examCard = document.createElement('div');
                        examCard.classList.add('col-md-4', 'mb-4');
                        examCard.innerHTML = `
                        <div class="card">
                            <img src="${exam.examImage}" class="card-img-top" alt="${exam.examName}">
                            <div class="card-body">
                                <h5 class="card-title">${exam.examName}</h5>
                                <p class="card-text">Lượt xem: ${exam.examViewCount}</p>
                                <p class="card-text">Người tạo: ${exam.fullName}</p>
                                <p class="card-text">Lượt thích: ${exam.examLikeCount}</p>
                                <button class="btn btn-primary" onclick="showExamDetail(${exam.examId})">Xem bài thi</button>
                            </div>
                        </div>
                    `;
                        examList.appendChild(examCard);
                    });
                });
            })
            .catch(error => console.error('Lỗi khi lấy tất cả bài thi:', error));
    }

    // Hàm lấy và hiển thị các subject trong sidebar
    function fetchAndDisplaySubjects() {
        fetch("http://localhost:8080/Elearning/subject", {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.result && Array.isArray(data.result)) {
                    data.result.forEach(subject => {
                        const subjectItem = document.createElement('li');
                        subjectItem.classList.add("nav-item");
                        subjectItem.innerHTML = `
                        <a href="#" class="nav-link" data-subject-id="${subject.subjectId}">
                            <i class="bi bi-book"></i><span>${subject.subjectName}</span>
                        </a>
                    `;
                        sidebarNav.appendChild(subjectItem);
                    });

                    // Thêm sự kiện cho các subject
                    sidebarNav.addEventListener('click', function (event) {
                        if (event.target.closest('.nav-link') && event.target.closest('.nav-link').id !== "all-exams") {
                            const subjectId = event.target.closest('.nav-link').getAttribute('data-subject-id');
                            fetchAndDisplayExams(subjectId);
                        }
                    });
                }
            })
            .catch(error => console.error('Error fetching subjects:', error));
    }

    // Hàm lấy danh sách bài thi theo subjectId
    function fetchAndDisplayExams(subjectId) {
        fetch(`http://localhost:8080/Elearning/exam/subject/all/${subjectId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                examList.innerHTML = ''; // Clear the list before displaying new exams

                // Kiểm tra dữ liệu trả về
                if (data.result && data.result.length > 0) {
                    const subjectName = data.result[0].subjectName; // Lấy tên môn học từ bài thi đầu tiên
                    const subjectHeading = document.createElement('h4');
                    subjectHeading.textContent = subjectName;
                    examList.appendChild(subjectHeading);

                    // Duyệt qua từng bài thi trong mảng result
                    data.result.forEach(exam => {
                        const examCard = document.createElement('div');
                        examCard.classList.add('col-md-4', 'mb-4');
                        examCard.innerHTML = `
                        <div class="card">
                            <img src="${exam.examImage}" class="card-img-top" alt="${exam.examName}">
                            <div class="card-body">
                                <h5 class="card-title">${exam.examName}</h5>
                                <p class="card-text">Lượt xem: ${exam.examViewCount}</p>
                                <p class="card-text">Người tạo: ${exam.fullName}</p>
                                <p class="card-text">Lượt thích: ${exam.examLikeCount}</p>
                                <button class="btn btn-primary" onclick="showExamDetail(${exam.examId})">Xem bài thi</button>
                            </div>
                        </div>
                    `;
                        examList.appendChild(examCard);
                    });
                } else {
                    examList.innerHTML = '<p>Không có bài thi nào cho môn học này.</p>';
                }
            })
            .catch(error => {
                console.error('Lỗi khi lấy bài thi:', error);
                examList.innerHTML = '<p>Đã có lỗi xảy ra khi lấy bài thi.</p>';
            });
    }

    // Hàm hiển thị chi tiết bài thi
    window.showExamDetail = function (examId) {
        // Ẩn danh sách bài thi và thanh tìm kiếm, hiển thị chi tiết bài thi và nút "Quay lại"
        examList.style.display = "none";
        examDetail.style.display = "block";
        searchContainer.style.display = "none";
        backButton.style.display = "block";

        // Gọi API để kiểm tra bài thi của người dùng
        fetch(`http://localhost:8080/Elearning/test/user/${userId}/exam/${examId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.status === 400) {
                    // Không tìm thấy bài test nào
                    takeExamButton.textContent = "Làm bài thi";
                    takeExamButton.onclick = () => {
                        if (confirm("Bạn có muốn làm bài thi này không?")) {
                            startNewExam(examId);
                        }
                    };
                    commentSection.style.display = "block";
                    return null;
                }
                return response.json();
            })
            .then(testData => {
                if (testData && testData.result && testData.result.length > 0) {
                    const latestTest = testData.result[0];
        
                    if (latestTest.testStatus === "in_progress") {
                        // Đang làm bài dở, đổi nút thành "Tiếp tục"
                        takeExamButton.textContent = "Tiếp tục";
                        takeExamButton.onclick = () => {
                            if (confirm("Bạn có muốn tiếp tục làm bài thi này không?")) {
                                continueExam(latestTest.testId, examId);
                            }
                        };
                        commentSection.style.display = "none";
                    } else if (latestTest.testStatus === "submitted") {
                        // Đã hoàn thành, đổi nút thành "Làm bài thi"
                        takeExamButton.textContent = "Làm bài thi";
                        takeExamButton.onclick = () => {
                            if (confirm("Bạn có muốn làm bài thi này không?")) {
                                startNewExam(examId);
                            }
                        };
                        commentSection.style.display = "block";
                    }
        
                    // Kiểm tra giới hạn attempt của bài thi
                    if (latestTest.testAttempt >= latestTest.examAttempt) {
                        takeExamButton.textContent = "Bạn đã quá số lần làm bài";
                        takeExamButton.disabled = true;
                    }
                } else {
                    console.warn("Không tìm thấy bài thi hoặc dữ liệu không hợp lệ.");
                }
            })
            .catch(error => console.error('Lỗi khi kiểm tra bài thi của người dùng:', error));


        // Gọi API để lấy thông tin chi tiết của bài thi
        fetch(`http://localhost:8080/Elearning/exam/${examId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                // Đặt nền cho `card-header` và điền dữ liệu
                document.getElementById("exam-header").style.backgroundImage = `url('${data.result.examImage}')`;
                document.getElementById("subjectName").textContent = data.result.subjectName;
                document.getElementById("examName").textContent = data.result.examName;

                // Sử dụng default-avatar nếu không có hình ảnh người dùng
                document.getElementById("userImage").src = data.result.userImage || "img/fe-img/default-avatar.jpg";
                document.getElementById("userName").textContent = data.result.fullName || "Tên Người Dùng";
                document.getElementById("userRole").textContent = data.result.userRole || "Vai trò";

                document.getElementById("examStartDate").textContent = data.result.examStartDate;
                document.getElementById("examEndDate").textContent = data.result.examEndDate;
                document.getElementById("examTimer").textContent = data.result.examTimer;
                document.getElementById("examDescription").textContent = data.result.examDescription;

                // Cập nhật các chỉ số
                document.getElementById("examLikeCount").textContent = `${data.result.examLikeCount} Lượt thích`;
                document.getElementById("examCommentCount").textContent = `${data.result.examCommentCount} Bình luận`;
                document.getElementById("examViewCount").textContent = `${data.result.examViewCount} Lượt xem`;

            })
            .catch(error => console.error('Lỗi khi lấy chi tiết bài thi:', error));
    };

    // Nút "Quay lại" để hiển thị lại danh sách bài thi và thanh tìm kiếm
    backButton.addEventListener("click", function () {
        examList.style.display = "block";
        examDetail.style.display = "none";
        searchContainer.style.display = "flex";
        backButton.style.display = "none";
    });

    // Hàm bắt đầu làm bài thi mới
    function startNewExam(examId) {
        if (confirm("Bạn có muốn làm bài thi này không?")) {
            // Thực hiện POST để bắt đầu bài thi mới
            fetch(`http://localhost:8080/Elearning/test`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userId: userId,
                    examId: examId,
                    questionIds: [], // Cập nhật danh sách câu hỏi tùy vào yêu cầu
                    answerIds: []
                })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Lỗi khi bắt đầu bài thi mới.");
                }
                return response.json(); // Lấy dữ liệu JSON từ phản hồi
            })
            .then(data => {
                console.log("Data từ POST API:", data); // Kiểm tra dữ liệu nhận được từ API
                const testId = data.result.testId; // Lấy `testId` từ phản hồi
                if (testId) {
                    // Chuyển hướng sang test.html với các tham số cần thiết
                    window.location.href = `test.html?examId=${examId}&userId=${userId}&testId=${testId}`;
                } else {
                    console.error("Không tìm thấy testId trong phản hồi từ API.");
                }
            })
            .catch(error => console.error("Lỗi khi bắt đầu bài thi mới:", error));
        }
    }

    // Hàm tiếp tục bài thi đang làm dở
    function continueExam(testId, examId) {
        if (confirm("Bạn có muốn tiếp tục làm bài thi này không?")) {
            // Thực hiện PUT để tiếp tục bài thi đang làm dở
            fetch(`http://localhost:8080/Elearning/test/${testId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userId: userId,
                    examId: examId,
                })
            })
            .then(response => {
                if (response.ok) {
                    window.location.href = `test.html?examId=${examId}&userId=${userId}&testId=${testId}`;
                } else {
                    console.error("Lỗi khi tiếp tục bài thi.");
                }
            })
            .catch(error => console.error("Lỗi khi tiếp tục bài thi:", error));
        }
    }


    // Gọi phương thức khi trang được tải
    fetchAndDisplaySubjects();
    fetchAndDisplayAllExams(); // Mặc định hiển thị tất cả các bài thi
});
