PHP
登陆认证：

$query_data['user_token'] = 'rkmi2huqu9dv6750g5os11ilv2'; // 获取的user_token
$query_data['mem_id'] = '23'; // 获取的用户ID
$query_data['app_id'] = '1'; // 获取的游戏APPID
$app_key = 'de933fdbede098c62cb309443c3cf251'; // 获取的游戏APPKEY
$url = "https://sdkapi.5taogame.com/api/v7/cp/user/check";
$signstr = "app_id=" . $query_data['app_id'] . "&mem_id=" . $query_data['mem_id'] . "&user_token=" . $query_data['user_token'] . "&app_key=" . $app_key;
$query_data['sign'] = md5($signstr);
/* http请求 */
$rdata = http_post_data($url, $query_data);
if ($rdata) {
    $rdata = json_decode($rdata,true);
    if ('1' == $rdata['status']) {
        // CP操作,请求成功,用户有效
        echo $rdata['data'];
    }
}
// HTTP json数据请求函数
function http_post_data($url, array $query_data) {
    $post_str = http_build_query($query_data);
    $curl = curl_init(); // 初始化curl
    curl_setopt($curl, CURLOPT_URL, $url);
    curl_setopt($curl, CURLOPT_HEADER, 0); // 过滤HTTP头
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1); // 显示输出结果
    curl_setopt($curl, CURLOPT_POST, 1); // post传输数据
    curl_setopt($curl, CURLOPT_POSTFIELDS, $post_str); // post传输数据
    curl_setopt($curl, CURLOPT_CONNECTTIMEOUT, 3); // 设置等待时间
    $header = array(
        "Content-Type: application/x-www-form-urlencoded; charset=UTF-8"
    );
    curl_setopt($curl, CURLOPT_HTTPHEADER, $header);
    //https 请求
    if (strlen($url) > 5 && strtolower(substr($url, 0, 5)) == "https") {
        curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, false);
    }
    $return_content = curl_exec($curl);
    curl_close($curl);
    return $return_content;
}
