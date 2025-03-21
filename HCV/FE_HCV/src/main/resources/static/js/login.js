$(document).ready(function() {
    function switchForm(formId) {
        $(".card").hide();
        $("#" + formId).show();
    }

    // Hiển thị form đăng nhập mặc định
    switchForm("loginForm");

    $("#login-button").click(async function () {
        const email = $("#login-email").val().trim();
        const password = $("#login-password").val().trim();
    
        if (!email || !password) {
            alert("Vui lòng nhập email và mật khẩu.");
            return;
        }
    
        try {
            const data = await postNoAuAPI("auth/login", { email, password });
    
            if (data?.result?.token) {
                localStorage.setItem("authToken", data.result.token);
                window.location.href = "home";
            } else {
                alert(data.message || "Đăng nhập thất bại. Vui lòng kiểm tra lại email và mật khẩu.");
            }
        } catch (error) {
            console.error("Lỗi khi đăng nhập:", error);
            alert("Đã xảy ra lỗi. Vui lòng thử lại sau.");
        }
    });

    $(".btn-link").click(function() {
        switchForm("forgotForm");
    });

    window.onload = function () {
        switchForm("loginForm");
    };
});