(function () {
    var selectors = {};
    //滑动的按钮
    selectors.pull_button = "#nc_1_n1z";
    //默认的起始坐标
    var xClientStart = 103;
    var yClientStart = 223;

    var SlideBot = function () {

    };

    SlideBot.prototype.loginSlide = function () {
        //获取起始坐标
        getBtnClientRect();
        //执行滑动
        setTimeout(function(){
            setTimeout(function(){
                startPlay(selectors.pull_button)
            }, 500);
        }, 500);
    }

    //滑动轨迹
    var moveList = MOVE_LIST_FORMAT;

    //获取滑块按钮所在位置
    function getBtnClientRect(){
        //得到滑动按钮的位置
        var button = document.querySelector(selectors.pull_button);
        var button_rect = button.getBoundingClientRect();
        // console.log("current rect: " + JSON.stringify(button_rect));
        //得到滑动按钮的坐标
        xClientStart = (button_rect.top + button_rect.bottom) / 2;
        yClientStart = (button_rect.left + button_rect.right) / 2;
        // console.log("current point: " + xClientStart + ", " + yClientStart);
    }

    //执行滑动
    function startPlay(selector) {
        var xstart = xClientStart + Math.random() * 10;
        var ystart = yClientStart + Math.random() * 10;

        var fireOnThis = document.querySelector(selector);

        //执行鼠标按下事件
        var evObj = document.createEvent('MouseEvents');
        evObj.initMouseEvent('mousedown', true, true, window, 1, xstart, ystart, xstart, ystart, false, false, true, false, 0, null);
        evObj.isTrusted = true;
        fireOnThis.dispatchEvent(evObj);
        // console.log("moudown points: " + xstart + ", " + ystart);

        var currentx = xstart;
        var currenty = ystart;
        for(var i = 0; i < moveList.length; i++){
            var movearr = moveList[i];
            (function(movex,movey,sleeptime){
                setTimeout(function(){
                    //得到下次坐标
                    currentx = currentx + movex;
                    currenty = currenty + movey;

                    var x = currentx;
                    var y = currenty;
                    // console.log("current points: " + x + ", " + y);
                    //执行移动
                    //var evObj = document.createEvent('MouseEvents');
                    evObj.initMouseEvent('mousemove', true, true, window, 1, x, y, x, y, false, false, true, false, 0, null);
                    evObj.isTrusted = true;
                    fireOnThis.dispatchEvent(evObj);
                }, sleeptime);
            })(movearr[0],movearr[1],movearr[2])
        }
    }

    window.slide_bot = new SlideBot();
    slide_bot.loginSlide();

})();