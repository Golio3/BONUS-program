var editsummernote = function () {
    $('.click2edit').summernote({focus: true});
};

var save111 = function () {
    var markup = $('.click2edit').summernote('code');
    alert(markup);
    $('.click2edit').summernote('destroy');
};
$(document).ready(function () {
    $('.summernote').summernote();


});

function btnEditTog(title, content, id) {
    if (title) {
        document.getElementById('togglesTitle').value = title;
    }
    if (content) {
        document.getElementById('togglesContent').value = content;
    }
    if (id) {
        document.getElementById('togglesId').value = id;
    }
}

function btnAddTog() {
    document.getElementById('togglesTitle').value = '';
    document.getElementById('togglesContent').value = '';
    document.getElementById('togglesId').value = null;
}

function btnRemTog(data, remIndex) {
    $.ajax({
        type: "POST",
        url: "/manage/page/page-edit/rem-tog",
        data: {
            page: data,
            remIndex: remIndex
        },
        dataType: 'json',
        timeout: 600000,
        success: function (postData) {
        },
        error: function (err) {
        }
    });
}

function btnEditFolder(title, content, id) {
    if (title) {
        document.getElementById('foldersTitle').value = title;
    }
    if (content) {
        document.getElementById('foldersContent').value = content;
    }
    if (id) {
        document.getElementById('foldersId').value = id;
    }
}

function btnAddFolder() {
    document.getElementById('foldersTitle').value = '';
    document.getElementById('foldersContent').value = '';
    document.getElementById('foldersId').value = null;
}


function btnRemFolder(data, remIndex) {
    $.ajax({
        type: "POST",
        url: "/manage/home/rem-folder",
        data: {
            home: data,
            remIndex: remIndex
        },
        dataType: 'json',
        timeout: 600000,
        success: function (postData) {
        },
        error: function (err) {
        }
    });
}

function btnRemImage(data) {
    $.ajax({
        type: "POST",
        url: "/manage/page/page-edit/rem-image",
        data: {
            page: data
        },
        dataType: 'json',
        timeout: 600000,
        success: function (postData) {
        },
        error: function (err) {
        }
    });
}

function btnRemAtt(data) {
    $.ajax({
        type: "POST",
        url: "/manage/page/page-edit/rem-att",
        data: {
            page: data
        },
        dataType: 'json',
        timeout: 600000,
        success: function (postData) {
        },
        error: function (err) {
        }
    });
}

function changeParent(parent) {
    if (parent !== 'j1_1') {
        return parent;
    } else {
        return 0;
    }
}

function byteToStr(byte) {
    return String.fromCharCode.apply(String, byte);
}

function test(b) {
    return 'test';
}