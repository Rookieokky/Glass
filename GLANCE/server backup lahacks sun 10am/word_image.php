<?php
error_reporting(E_ALL);
ini_set("display_errors", 1);
$word = $_POST["word"];
$json = json_decode(file_get_contents('http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q='.$word),true);
//var_dump($json);
$image_url = $json["responseData"]["results"][0]["unescapedUrl"];
echo $image_url;
?>