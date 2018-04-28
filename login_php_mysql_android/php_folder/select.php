<?php

$link =mysqli_connect('localhost','root','root','exam');

if(!$link)
{
	echo "MYSQL connect error :";
	echo mysqli_connect_error();
	exit();
}

mysqli_set_charset($link,"utf8");

$sql = 'select * from Person';

$result =mysqli_query($link,$sql);
$data = array();
if($result)
{
	while($row=mysqli_fetch_array($result))
	{
		array_push($data,array('num'=>$row[0],'id'=>$row[1],'password'=>$row[2],'name'=>$row[3],'address'=>$row[4]));
	}
	header('Content-Type: application/json; charset=utf8');
	$json = json_encode(array('Person_json_data'=>$data),JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE); // change json format
	echo $json;
}
else {
	echo "SQL processing error : ";
	echo mysqli_error($link);

}
mysqli_close($link);

