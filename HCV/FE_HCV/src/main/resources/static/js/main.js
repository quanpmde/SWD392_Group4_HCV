const Main = {
    logout: function () {
        var data = { token: AUTH_TOKEN };
        postJsonAPI('auth/logout', data, function () {
            localStorage.clear();
            window.location.reload();
        });
    },
    renderAva:function(){
        $("#avatarDropdown")
    },
   

    checkInfo: async function () {
        let page = this;
        let html = '';
        if(AUTH_TOKEN!=null){
            let data = await getDataAPI('users/userme');
            var userInfo = data.result;
    
            html = `
                <img src="${userInfo.userImage?userInfo.userImage:'https://i.pinimg.com/736x/ff/9c/47/ff9c47a994bd5523722255c062edaa31.jpg'}" alt="Avatar" class="avatar dropdown-toggle mx-5" 
                     id="avatarDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="avatarDropdown">
                    <li><a class="dropdown-item" href="profile">Profile</a></li>
                    <li><hr class="dropdown-divider"></li>
                    <li><a class="dropdown-item" href="#">Setting</a></li>
                    <li><hr class="dropdown-divider"></li>
                    <li><a class="dropdown-item text-danger logout-btn" id="logout" href="#">Log Out</a></li>
                </ul>`;
        }else{
            html = `<a href="login" class="btn btn-primary py-4 px-lg-5 d-none d-lg-block">Tham gia ngay 
                    <i class="fa fa-arrow-right ms-3" style="right: 100%"></i></a>`;
        }
        $("#auth-container").html(html);
    
       page.SetListener();
    },
    

    SetListener: function () {
        $("#logout").click(() => this.logout());
    },

    init: function () {
        this.checkInfo();
    }
};

$(document).ready(function () {
    Main.init();
});
