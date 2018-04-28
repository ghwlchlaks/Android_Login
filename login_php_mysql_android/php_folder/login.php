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
$id = isset($_POST['id'])? $_POST['id']:'';
$password = isset($_POST['password'])?$_POST['password']:'';
$login_sql = "select * from Person where id='$id' and password='$password'";
$login_result =mysqli_query($link,$login_sql);
$count = mysqli_num_rows($login_result);

if($count==0)
{
	echo "fail ";
	echo mysqli_error($link);
}
else 
{
	echo "success";
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
	   Id: <input type = "text" name = "id" />
         Password: <input type = "text" name = "password" />
	<input type="submit"/>
	</form>
</body>
</html>
<?php
}
?>
