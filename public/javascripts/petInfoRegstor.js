$(function(){
  $('#petImg').change(function(e){
    //ファイルオブジェクトを取得する
    var file = e.target.files[0];
    var reader = new FileReader();

    //画像でない場合は処理終了
    if(file.type.indexOf("image") < 0){
      alert("画像ファイルを指定してください。");
      return false;
    }

    //アップロードした画像を設定する
    reader.onload = (function(file){
      return function(e){
        $("#img1").attr("src", e.target.result);
        $("#img1").attr("title", file.name);
      };
    })(file);
    reader.readAsDataURL(file);

  });
});


function openKiyaku() {
    window.open("/kiyaku","window1","width=600,height=600");
}

function clickHandler(e) {
    window.open("/kiyaku","window1","width=600,height=600");
}

document.addEventListener('DOMContentLoaded', function() {
    document.querySelector('.kiyaku').addEventListener('click', clickHandler);
});