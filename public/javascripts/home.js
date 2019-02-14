$('#update').on('click', function() {
    var id = $(this).parent().data("id");
    console.log(id + "を更新しますか");
})

$(function() {
  $("#delete").click(function() {
    var id = $(this).parent().data("id");
    console.log(id + "を削除しますか");

    $("#cdl1").dialog({
      modal:true,
      title:"確認画面",
      buttons: {
        "ＯＫ": function() {
        $(this).dialog("close");
        location.href = "/deletePetInfoList?id=" + id;
        },
        "キャンセル": function() {$(this).dialog("close");}
      }
    });
  });
});

window.onload = function() {
    console.log("読込完了");
};
