<?php
echo "meow";


try
  {
  
$response = Unirest::get(
  "https://montanaflynn-dictionary.p.mashape.com/define?word=irony",
  array(
    "X-Mashape-Authorization" => "v85yzp0k1AHceRjwhqEBmSMwnEmSjaKU"
  ),
  null
);
  echo 'response succeeded';
  }

//catch exception
catch(Exception $e)
  {
  echo 'Error message: ' .$e->getMessage();
  }


echo "woof";
echo $response;

?>