<?php
error_reporting(E_ALL);
ini_set("display_errors", 1);

$word = $_POST["word"];
$url = 'https://montanaflynn-dictionary.p.mashape.com/define?word='.urlencode($word);

// use key 'http' even if you send the request to https://...
$options = array(
    'http' => array(
        'header'  => "X-Mashape-Authorization: v85yzp0k1AHceRjwhqEBmSMwnEmSjaKU",
        'method'  => 'POST'
    ),
);
$context  = stream_context_create($options);
$result = file_get_contents($url, false, $context);


//var_dump($result)

$string = $result;
$pattern = '\w*\(\d+\)\s';
$replacement = '';
$def_array = json_decode($result,true);

for($i = 0; $i < sizeof($def_array["definitions"]); $i++)
{
    echo $def_array["definitions"][$i]["text"];

}

?>