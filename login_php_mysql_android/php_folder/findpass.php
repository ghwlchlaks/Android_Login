<html> 
<head> <meta http-equiv="Content-Type" content="text/html; charset=utf-8"></head>
<body>

<?php

$link =mysqli_connect('localhost','root','root','exam');

if(!$link)
{
	echo "MYSQL connect error :";
	echo mysqli_connect_error();
	exit();
}

mysqli_set_charset($link,"utf8");
$email = isset($_POST['email'])? $_POST['email']:'';
$id = isset($_POST['id'])? $_POST['id']:'';

$pass_sql = "select password from Person where address='$email' and id='$id'";
$pass_result =mysqli_query($link,$pass_sql);
$count = mysqli_num_rows($pass_result);

if($count==0)
{
	echo "fail, ";
	echo mysqli_error($link);

}
else
{
	$alpha= array('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z');
	$new_pass = "";
	for ($i=0; $i<8; $i++)
	{
		$new_pass.=$alpha[rand(0,count($alpha))];
	}
	$new_pass_sql = "update Person set password='$new_pass' where address='$email' and id='$id'";
	$new_pass_result = mysqli_query($link,$new_pass_sql);

	if($new_pass_result==0)
	{
		echo "fail, ";
		echo mysqli_error($link);

	}
	else
	{
		echo "success,";
		echo $new_pass;
	}
}
mysqli_close($link);
?>

</body>
</html>
<?php

#$android =strpos($_SERVER['HTTP_USER_AGENT'],"Android");

#if (!$android)
{
?>

<html>
<body>

	<form action="<?php $_PHP_SELF ?>" method="POST">
		Id :<input type="text" name ="id" />
		Email : <input type = "text" name = "email" />
	<input type="submit"/>
	</form>
</body>
</html>
<?php
}
?>
