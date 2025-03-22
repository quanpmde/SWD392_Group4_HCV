const Profile = {


    getUserInfo: function () {
        getDataAPI('users/userme', function (data) {
            var userInfo = data.result
            var view = $("#view-mode")
            view.find("#user-name").text(userInfo.userName);
            view.find("#user-ava").attr("src", userInfo.userImage ? userInfo.userImage : 'https://i.pinimg.com/736x/ff/9c/47/ff9c47a994bd5523722255c062edaa31.jpg');
            view.find("#user-email").text(userInfo.userEmail);
            view.find("#user-phone").text(userInfo.userPhone);
            view.find("#user-dob").text(userInfo.userDob);
            view.find("#user-vip").text(userInfo.userVip);

            $("#userId").attr("value", userInfo.userId);

        })
    },
    renderUserInfo: function () {
        $("#view-mode").hide();
        $("#update-mode").show();

        getDataAPI('users/userme', function (data) {
            var userInfo = data.result
            var update = $("#update-mode")
            update.find("#user-name").attr("value", userInfo.userName);
            update.find("#user-ava").attr("src", userInfo.userImage ? userInfo.userImage : 'https://i.pinimg.com/736x/ff/9c/47/ff9c47a994bd5523722255c062edaa31.jpg');
            update.find("#user-email").attr("value", userInfo.userEmail);
            update.find("#user-phone").attr("value", userInfo.userPhone);
            update.find("#user-dob").attr("value", userInfo.userDob);
            update.find("#user-vip").attr("value", userInfo.userVip);

        })


    },

    // saveInfo: function () {
    //     confirmModal("Are you sure updating your information", () => {

    //         var update = $("#update-mode");
    //         var userId = $("#userId").val();
    //         var data = {
    //             "userId": userId,
    //             "userName": update.find("#user-name").val(),
    //             "userPhone": update.find("#user-phone").val(),
    //             "userDob": update.find("#user-dob").val()
    //         }

    //         putJsonAPI(`users/update-user`, data, function () {
    //             var file = $('#upload-avatar')[0].files[0];
    //             if (file) {
    //                 var formData = new FormData();
    //                 formData.append("file",file);
    //                 putFormDataAPI(`users/image/${userId}`, formData)
    //             }

    //             window.location.reload();
    //         });

    //     })

    // },

    saveInfo: function () {
        confirmModal("Are you sure updating your information", () => {

            var update = $("#update-mode");
            var userId = $("#userId").val();
            
            var formData = new FormData();
             var data = {
                "userId": userId,
                "userName": update.find("#user-name").val(),
                "userPhone": update.find("#user-phone").val(),
                "userDob": update.find("#user-dob").val()
            }

            
            formData.append("data",JSON.stringify(data));
           
            var file = $('#upload-avatar')[0].files[0];
            if (file) {
                
            formData.append("file",file);
                
            }
            putFormDataAPI(`users/update-me`, formData,function(){
                window.location.reload();
            })
            

        })

    },


    SetListener: function () {
        let page = this;
        $('#upload-avatar').change(function (event) {
            var file = event.target.files[0];
            if (file) {
                var render = new FileReader();
                render.onload = function (e) {
                    $("#update-mode #user-ava").attr("src", e.target.result);
                }
                render.readAsDataURL(file);
            }
        });

        $("#update-mode #user-phone").on("input", function () {
            $(this).val($(this).val().replace(/\D/g, '').substring(0, 10));
        });


        $("#update-me").click(function () {
            page.renderUserInfo();
        });

        $("#save-btn").click(() => page.saveInfo())
    },

    init: function () {
        $("#view-mode").show();
        $("#update-mode").hide();


        this.getUserInfo();

        this.SetListener();
    }
};

$(document).ready(function () {
    Profile.init();
});
