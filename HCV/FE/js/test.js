document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const userId = urlParams.get("userId");
    const examId = urlParams.get("examId");
    const testId = urlParams.get("testId");
    const token = localStorage.getItem("authToken");

    const questionsContainer = document.getElementById("questions-container");
    const sidebarContainer = document.getElementById("question-nav");
    const examTimerElement = document.getElementById("exam-timer");
    const remainingTimeElement = document.getElementById("remaining-time");
    const totalQuestionsElement = document.getElementById("total-questions");

    const backButton = document.querySelector(".back-button");
    const submitButton = document.querySelector(".btn-submit");

    let currentAction = null; // Biến để lưu trạng thái hành động hiện tại

    // Fetch Existing Answers
    function fetchExistingAnswers() {
        fetch(`http://localhost:8080/Elearning/test/${testId}`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        })
            .then(response => response.json())
            .then(data => {
                const test = data.result;

                if (test && Array.isArray(test.questions) && Array.isArray(test.answers)) {
                    // Gọi hàm đánh dấu các đáp án đã chọn
                    markExistingAnswers(test.answers);
                } else {
                    console.error("API trả về dữ liệu không hợp lệ:", test);
                }
            })
            .catch(error => console.error("Error fetching existing test data:", error));
    }

    // Mark Existing Answers in the UI
    function markExistingAnswers(answers) {
        answers.forEach(answer => {
            const questionId = answer.questionId;
            const answerId = answer.answerId;

            // Find the question box with the matching questionId
            const questionBox = document.querySelector(`.question-box[data-question-id="${questionId}"]`);
            if (questionBox) {
                // Find and check the input with the matching answerId
                const input = questionBox.querySelector(`input[value="${answerId}"]`);
                if (input) {
                    input.checked = true;
                }
            }
        });
    }

    // Fetch Exam Data
    fetch(`http://localhost:8080/Elearning/exam/${examId}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then(data => {
            const exam = data.result;

            // Display Exam Timer
            examTimerElement.textContent = `Thời gian làm bài: ${exam.examTimer} phút`;
            totalQuestionsElement.textContent = `Số câu: ${exam.questions.length} câu`;

            // Generate Question Boxes and Sidebar Navigation
            exam.questions.forEach((question, index) => {
                // Generate Question Box
                const questionBox = document.createElement("div");
                questionBox.classList.add("question-box");
                questionBox.id = `question-${index + 1}`;
                questionBox.setAttribute("data-question-id", question.questionId); // Add questionId as a data attribute

                // Question Description
                questionBox.innerHTML = `
                    <p><strong>Câu ${index + 1}:</strong> ${question.questionDescription}</p>
                `;

                // Generate Answer Options
                question.answers.forEach((answer, answerIndex) => {
                    const option = document.createElement("div");
                    option.classList.add("option");
                    option.innerHTML = `
                        <input type="radio" id="q${index + 1}-option${answerIndex + 1}" name="q${index + 1}" class="form-check-input" value="${answer.answerId}"> 
                        <label for="q${index + 1}-option${answerIndex + 1}">${answer.answerDescription}</label>
                    `;
                    questionBox.appendChild(option);
                });

                // Add "Clear Choice" Option
                const clearChoice = document.createElement("span");
                clearChoice.classList.add("remove-choice");
                clearChoice.textContent = "Xóa lựa chọn";
                clearChoice.onclick = () => {
                    document.querySelectorAll(`input[name="q${index + 1}"]`).forEach(input => input.checked = false);
                };
                questionBox.appendChild(clearChoice);

                // Append Question Box to Questions Container
                questionsContainer.appendChild(questionBox);

                // Generate Sidebar Question Number Navigation
                const questionNumber = document.createElement("div");
                questionNumber.classList.add("question-number");
                questionNumber.textContent = index + 1;
                questionNumber.onclick = () => {
                    document.getElementById(`question-${index + 1}`).scrollIntoView({ behavior: "smooth" });
                };
                sidebarContainer.appendChild(questionNumber);
            });

            // Fetch existing answers after questions are loaded
            fetchExistingAnswers();
        })
        .catch(error => console.error("Error fetching exam data:", error));

    // Fetch Remaining Time for Test
    function fetchRemainingTime() {
        fetch(`http://localhost:8080/Elearning/test/time-left/${testId}`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        })
            .then(response => response.json())
            .then(data => {
                const secondsLeft = data.result;
                const minutes = Math.floor(secondsLeft / 60);
                const seconds = secondsLeft % 60;
                remainingTimeElement.textContent = `Thời gian còn lại: ${minutes} phút ${seconds} giây`;

                // If time is up, alert and submit the test
                if (secondsLeft <= 0) {
                    submitTest(); // Implement this function to handle submission
                }
            })
            .catch(error => console.error("Error fetching remaining time:", error));
    }

    // Update remaining time every second
    setInterval(fetchRemainingTime, 1000);

    // Make Sidebar Follow Scrolling
    window.addEventListener("scroll", () => {
        const sidebar = document.querySelector(".sidebar");
        const sidebarTop = sidebar.getBoundingClientRect().top;

        if (sidebarTop < 0) {
            sidebar.style.position = "fixed";
            sidebar.style.top = "10px";
        } else {
            sidebar.style.position = "relative";
            sidebar.style.top = "auto";
        }
    });

    // Back Button Functionality
    backButton.addEventListener("click", () => {
        currentAction = "back";
        confirmationModalLabel.textContent = "Bạn có chắc là mình muốn thoát ra ngoài không?";
        confirmationModalBody.innerHTML = `
            <p>Hệ thống sẽ giữ nguyên tiến trình dành cho bạn.</p>
            <p>Bạn vẫn có thể tiếp tục bài kiểm tra nếu muốn.</p>
            <p>Tuy nhiên, thời gian sẽ vẫn chạy như bình thường.</p>
        `;
        const modal = new bootstrap.Modal(confirmationModal);
        modal.show();
    });

    // Submit Button Functionality
    submitButton.addEventListener("click", () => {
        currentAction = "submit";
        confirmationModalLabel.textContent = "Bạn có chắc chắn muốn nộp bài?";
        confirmationModalBody.innerHTML = `
            <p>Hệ thống sẽ ghi nhận và chấm điểm bài làm của bạn.</p>
            <p>Bạn không thể thay đổi sau khi nộp bài.</p>
        `;
        const modal = new bootstrap.Modal(confirmationModal);
        modal.show();
    });

    // Save Test Functionality
    function saveTest() {
        const answers = collectAnswers();
        fetch(`http://localhost:8080/Elearning/test/save/${testId}`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                userId: userId,
                examId: examId,
                questionIds: answers.map(answer => answer.questionId), // Extract questionIds
                answerIds: answers.map(answer => answer.answerId) // Extract answerIds
            })
        })
            .then(response => {
                if (response.ok) {
                    // Chuyển về exam.html và giữ trạng thái đang hiển thị chi tiết bài kiểm tra
                    window.location.href = `exam.html?examId=${examId}`;
                } else {
                    alert("Lỗi khi lưu bài kiểm tra.");
                }
            })
            .catch(error => console.error("Error saving test:", error));
    }

    // Submit Test Functionality
    function submitTest() {
        // Lưu câu trả lời trước khi nộp bài
        const answers = collectAnswers(); // Thu thập các câu trả lời từ giao diện
        fetch(`http://localhost:8080/Elearning/test/save/${testId}`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                userId: userId,
                examId: examId,
                questionIds: answers.map(answer => answer.questionId), // Lưu danh sách questionIds
                answerIds: answers.map(answer => answer.answerId) // Lưu danh sách answerIds
            })
        })
            .then(response => {
                if (response.ok) {
                    // Tiến hành nộp bài ngay sau khi lưu thành công
                    fetch(`http://localhost:8080/Elearning/test/submit/${testId}`, {
                        method: "POST",
                        headers: {
                            "Authorization": `Bearer ${token}`,
                            "Content-Type": "application/json"
                        }
                    })
                        .then(submitResponse => {
                            if (submitResponse.ok) {
                                // Chuyển về trang exam.html và giữ trạng thái đang hiển thị chi tiết bài kiểm tra
                                window.location.href = `exam.html?examId=${examId}`;
                            } else {
                                alert("Lỗi khi nộp bài.");
                            }
                        })
                        .catch(error => console.error("Error submitting test:", error));
                } else {
                    alert("Lỗi khi lưu bài kiểm tra trước khi nộp bài.");
                }
            })
            .catch(error => console.error("Error saving test before submission:", error));
    }


    // Function to collect selected answers along with their questionId
    function collectAnswers() {
        const answers = []; // Array to store {questionId, answerId} pairs

        // Iterate through all question elements
        document.querySelectorAll(".question-box").forEach((questionBox, index) => {
            const questionId = questionBox.getAttribute("data-question-id"); // Assuming each question-box has a data-question-id attribute

            // Find the selected answer for this question
            const selectedAnswer = questionBox.querySelector(`input[name="q${index + 1}"]:checked`);
            if (selectedAnswer) {
                const answerId = selectedAnswer.value;
                answers.push({
                    questionId: parseInt(questionId, 10), // Ensure questionId is an integer
                    answerId: parseInt(answerId, 10) // Ensure answerId is an integer
                });
            }
        });

        return answers; // Return collected answers

    }

    // Xử lý khi người dùng xác nhận hành động trong modal
    confirmActionButton.addEventListener("click", () => {
        if (currentAction === "back") {
            saveTest(); // Gọi hàm lưu bài kiểm tra khi quay lại
        } else if (currentAction === "submit") {
            submitTest(); // Gọi hàm nộp bài kiểm tra khi nộp bài
        }
    });
}); 