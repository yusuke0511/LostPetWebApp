$('#update').on('click', function() {
    var id = $(this).parent().data("id");
    console.log(id + "を更新しますか");

    location.href="/petInfoEdit?id=" + id;
})

$(function() {
  $("#delete").click(function() {
    var id = $(this).parent().data("id");

    $("#cdl1").dialog({
      modal:true,
      title:"確認画面",
      buttons: {
        "はい": function() {
        $(this).dialog("close");
        location.href = "/deletePetInfoList?id=" + id;
        },
        "いいえ": function() {$(this).dialog("close");}
      }
    });
  });
});

