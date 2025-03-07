document.addEventListener("DOMContentLoaded", function() {
    const loginButton = document.getElementById("login-button");

    loginButton.addEventListener("click", async function() {
        const email = document.getElementById("login-email").value;
        const password = document.getElementById("login-password").value;

        if (!email || !password) {
            alert("Vui lòng nhập email và mật khẩu.");
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/Elearning/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ email, password }),
            });

            if (response.ok) {
                const data = await response.json();
                const token = data.result.token;

                // Lưu token vào localStorage hoặc sessionStorage
                localStorage.setItem("authToken", token);

                // Chuyển hướng tới trang home.html
                window.location.href = "home.html";
            } else {
                alert("Đăng nhập thất bại. Vui lòng kiểm tra lại email và mật khẩu.");
            }
        } catch (error) {
            console.error("Lỗi khi đăng nhập:", error);
            alert("Đã xảy ra lỗi. Vui lòng thử lại sau.");
        }
    });
});
