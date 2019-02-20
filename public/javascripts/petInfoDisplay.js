$(function () {
  dispayMap(document.getElementById('lat').value, document.getElementById('lng').value)
});

function dispayMap(lat, lng) {
    console.log("dispayMap呼び出し")
    console.log("lat -> " + lat + " lng -> " + lng)


    if (lat != null && lng != null) {

        var MyLatLng = new google.maps.LatLng(lat, lng);
        var Options = {
         zoom: 15,      //地図の縮尺値
         center: MyLatLng,    //地図の中心座標
         mapTypeId: 'roadmap'   //地図の種類
        };
        var map = new google.maps.Map(document.getElementById('map-canvas'), Options);

        var marker
        // マーカーを設置
        marker = new google.maps.Marker({
            position: MyLatLng,
            map: map
        });

        // 座標の中心をずらす
        map.panTo(MyLatLng)
    } else {
        document.getElementById('map-canvas').textContent = "地図情報が登録されていません。"
    }
}
