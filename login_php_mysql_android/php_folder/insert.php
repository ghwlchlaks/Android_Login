<html>
<head> <meta http-equiv="Content-Type" content="text/html; charset=utf-8"></head>
<body>
<?php  
error_reporting(E_ALL); 
ini_set('display_errors',1); 

$link=mysqli_connect("localhost","root","root","exam"); 
if (!$link)  
{ 
   echo "MySQL 접속 에러 : ";
   echo mysqli_connect_error();
   exit();
}  


mysqli_set_charset($link,"utf8");  

//POST 값을 읽어온다.
$id = isset($_POST['id'])? $_POST['id']:'';
$password = isset($_POST['password']) ? $_POST['password'] :'';
$name=isset($_POST['name']) ? $_POST['name'] : '';  
$address=isset($_POST['address']) ? $_POST['address'] : '';  
$id_checked = "select * from Person where id='$id'";
$id_result  = mysqli_query($link,$id_checked);
$count = mysqli_num_rows($id_result);

if ($count==0)
{
	if ($id !="" and $password!="" and $name !="" and $address !="" ){   
	    $sql="insert into Person(id,password,name,address) values('$id','$password','$name','$address')";  
	    $result=mysqli_query($link,$sql);  
	    mysqli_query($link,"ALTER TABLE Person AUTO_INCREMENT=1");
	    mysqli_query($link,'SET @CNT=0');
	    mysqli_query($link,'UPDATE Person SET Person.num = @CNT:=@CNT+1');
	    if($result){
	       echo "success";  
	    }  
	    else{  
	       echo "SQL문 처리중 에러 발생 : "; 
	       echo mysqli_error($link);
	    } 
 
	}
	 else {
	    echo "데이터를 입력하세요 ";
	}	
}
else 
{
echo "fail";
echo mysqli_error($link);
}


mysqli_close($link);
?>
</body>
</html>
<?php

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if (!$android){
?>

<html>
   <body>
   
      <form action="<?php $_PHP_SELF ?>" method="POST">
         Id: <input type = "text" name = "id" />
         Password: <input type = "text" name = "password" />
         Name: <input type = "text" name = "name" />
         Address: <input type = "text" name = "address" />
         <input type = "submit" />
      </form>
   
   </body>
</html>
<?php
}
?>
