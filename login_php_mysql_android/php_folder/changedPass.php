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
$password = isset($_POST['password'])? $_POST['password']:'';
$id = isset($_POST['id'])? $_POST['id']:'';
$changedpass= isset($_POST['changedpass'])? $_POST['changedpass']:'';

$pass_sql = "select * from Person where password='$password' and id='$id'";
$pass_result =mysqli_query($link,$pass_sql);
$count = mysqli_num_rows($pass_result);

if($count==0)
{
	echo "fail, ";
	echo mysqli_error($link);

}
else
{
	$change_pass_sql = "update Person set password='$changedpass' where password='$password' and id='$id'";
	$change_pass_result = mysqli_query($link,$change_pass_sql);

	if($change_pass_result==0)
	{
		echo "fail, ";
		echo mysqli_error($link);

	}
	else
	{
		echo "success,";
		echo $changedpass;
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
		Password : <input type = "text" name = "password" />
		ChangedPassword : <input type="text" name="changedpass"/>
	<input type="submit"/>
	</form>
</body>
</html>
<?php
}
?>
