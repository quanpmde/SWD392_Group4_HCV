const Profile = {

    
    getUserInfo:function(){
        getDataAPI('users/userme',function(data){
            var userInfo = data.result
            var view = $("#view-mode")
            view.find("#user-name").text(userInfo.userName);
            view.find("#user-ava").attr("src",userInfo.userImage?userInfo.userImage:'https://i.pinimg.com/736x/ff/9c/47/ff9c47a994bd5523722255c062edaa31.jpg');
            view.find("#user-email").text(userInfo.userEmail);
            view.find("#user-phone").text(userInfo.userPhone);
            view.find("#user-dob").text(userInfo.userDob);
            view.find("#user-vip").text(userInfo.userVip);

        })
    },
    renderUserInfo:function(){

        $("#view-mode").hide();
        $("#update-mode").show();

        getDataAPI('users/userme',function(data){
            var userInfo = data.result
            var update = $("#update-mode")
            update.find("#user-name").attr("value",userInfo.userName);
            update.find("#user-ava").attr("src",userInfo.userImage?userInfo.userImage:'https://i.pinimg.com/736x/ff/9c/47/ff9c47a994bd5523722255c062edaa31.jpg');
            update.find("#user-email").attr("value",userInfo.userEmail);
            update.find("#user-phone").attr("value",userInfo.userPhone);
            update.find("#user-dob").attr("value",userInfo.userDob);
            update.find("#user-vip").attr("value",userInfo.userVip);

        })


    },

    saveInfo: function () {
        var formData = new FormData();
        var update = $("#update-mode");
        var email =  update.find("#user-email").val().trim();
    
        formData.append("userName", update.find("#user-name").val());
        formData.append("userPhone", update.find("#user-phone").val());
        formData.append("userDob", update.find("#user-dob").val());
    
        var file = $("#upload-avatar")[0].files[0];
        if (file) {
            formData.append("userImage", file);
        }
    
        putFormDataAPI(`users/${email} `, formData, function () {
            window.location.reload();
        });
    },
    

    SetListener: function () {
        let page=this;
        $('#upload-avatar').change(function (event) { 
            var file = event.target.files[0];
            if(file){
                var render = new FileReader();
                render.onload=function(e){
                    $("#update-mode #user-ava").attr("src",e.target.result);
                }
                render.readAsDataURL(file);
            }
        });

        $("#update-me").click(function () { 
            page.renderUserInfo();
        });

        $("#save-btn").click(()=>page.saveInfo())
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
