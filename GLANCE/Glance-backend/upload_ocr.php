<?php
if ($_FILES["file"]["error"] > 0)
{
    echo "Return Code: " . $_FILES["file"]["error"] . "<br>";
}
else
{
   // echo "Upload: " . $_FILES["file"]["name"] . "<br>";
    //echo "Type: " . $_FILES["file"]["type"] . "<br>";
    //echo "Size: " . ($_FILES["file"]["size"] / 1024) . " kB<br>";
    //echo "Temp file: " . $_FILES["file"]["tmp_name"] . "<br>";

    if (file_exists("upload/" . $_FILES["file"]["name"]))
    {
        //echo $_FILES["file"]["name"] . " already exists. ";
    }
    else
    {
        move_uploaded_file($_FILES["file"]["tmp_name"],
            "upload/" . $_FILES["file"]["name"]);
        //echo "Stored in: " . "upload/" . $_FILES["file"]["name"];
    }
}

require_once '/var/www/html/unirest-php-master/lib/Unirest.php';

$response = Unirest::post(
    "https://poiuytrez-ocrapiservice.p.mashape.com/1.0/rest/ocr",
    array(
        "X-Mashape-Authorization" => "v85yzp0k1AHceRjwhqEBmSMwnEmSjaKU"
    ),
    array(
        "image" => "@/var/www/html/upload/image.jpg",
        "language" => "en"
    )
);

//echo($response->body);

$subject = $response->body;
$pattern = '/\w+\b/i';
preg_match_all($pattern, $subject, $matches);
echo json_encode($matches);
?>