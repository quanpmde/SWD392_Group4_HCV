const URL_API="http://localhost:8080/Elearning/"
const AUTH_TOKEN = localStorage.getItem("authToken");

function getUserId() {
    var payload = JSON.parse(atob(AUTH_TOKEN.split('.')[1]));
    return payload.userId;
}

function getAPI(url, data) {
    return $.ajax({
        url: URL_API + url,
        type: "GET",
        contentType: "application/json",
        headers: AUTH_TOKEN ? { "Authorization": `Bearer ${AUTH_TOKEN}` } : {},
        data: JSON.stringify(data),
    }).fail(handleAPIError);
}

function postAPI(url, data) {
    return $.ajax({
        url: URL_API+url,
        type: "POST",
        contentType: "application/json",
        headers: AUTH_TOKEN ? { "Authorization": `Bearer ${AUTH_TOKEN}` } : {},
        data: JSON.stringify(data),
    }).fail(handleAPIError);
}

function postNoAuAPI(url, data) {
    return $.ajax({
        url: URL_API+url,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(data),
    }).fail(handleAPIError);
}

function putAPI(url, data) {
    return $.ajax({
        url: URL_API+url,
        type: "PUT",
        contentType: "application/json",
        headers: AUTH_TOKEN ? { "Authorization": `Bearer ${AUTH_TOKEN}` } : {},
        data: JSON.stringify(data),
    }).fail(handleAPIError);
}

function deleteAPI(url) {
    return $.ajax({
        url: URL_API+url,
        type: "DELETE",
        contentType: "application/json",
        headers: AUTH_TOKEN ? { "Authorization": `Bearer ${AUTH_TOKEN}` } : {},
    }).fail(handleAPIError);
}


//Không cần Param
function getDataAPI(url,success,errorcallback) {
    return $.ajax({
        url: URL_API + url ,
        type: "GET",
        contentType: "application/json",
        headers: AUTH_TOKEN ? { "Authorization": `Bearer ${AUTH_TOKEN}` } : {},
        success: success,
        error: function (error) {
            if (typeof errorcallback === "function") {
                errorcallback(error);
            }
        }
    });
}

function postDataAPI(url,success,error) {
    return $.ajax({
        url: URL_API+url,
        type: "POST",
        contentType: "application/json",
        headers: AUTH_TOKEN ? { "Authorization": `Bearer ${AUTH_TOKEN}` } : {},
        success: success,
        error: error
    });
}

function getJsonAPI(url,data,success) {
    return $.ajax({
        url: URL_API + url ,
        type: "GET",
        contentType: "application/json",
        headers: AUTH_TOKEN ? { "Authorization": `Bearer ${AUTH_TOKEN}` } : {},
        data: JSON.stringify(data),
        success: function (response) {
            if (typeof successCallback === "function") {
                successCallback(response);
            }
        },
        error: function (error) {
            handleAPIError(error);
        }
    });
}

function postJsonAPI(url, data, successCallback) {
     $.ajax({
        url: URL_API + url,
        type: "POST",
        contentType: "application/json",
        headers: AUTH_TOKEN ? { "Authorization": `Bearer ${AUTH_TOKEN}` } : {},
        data: JSON.stringify(data),
        success: function (response) {
            if (typeof successCallback === "function") {
                successCallback(response);
            }
        },
        error: function (error) {
            handleAPIError(error);
        }
    });
}

function putFormDataAPI(url, formData, successCallback) {
    $.ajax({
        url: URL_API + url,
        type: "PUT",
        processData: false,
        contentType: false,
        headers: AUTH_TOKEN ? { "Authorization": `Bearer ${AUTH_TOKEN}` } : {},
        data: formData,
        success: function (response) {
            if (typeof successCallback === "function") {
                successCallback(response);
            }
        },
        error: function (error) {
            handleAPIError(error);
        }
    });
}

//ko can authen
function postJsonDataAPI(url, data, successCallback) {
    $.ajax({
       url: URL_API + url,
       type: "POST",
       contentType: "application/json",
       data: JSON.stringify(data),
       success: function (response) {
           if (typeof successCallback === "function") {
               successCallback(response);
           }
       },
       error: function (error) {
           handleAPIError(error);
       }
   });
}

function handleAPIError(jqXHR) {
    console.error("API Error:", jqXHR);
    if (jqXHR.status === 401) {
        alert("Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại.");
    }

}
