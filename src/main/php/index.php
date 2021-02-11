<html lang="de">

    <head>
        <title>Image Upload</title>
        <link rel="stylesheet" type="text/css" href="style.css">
    </head>

    <body>
        <div id="structureBox">

        </div>
        <div>
            <h1>ProPra Lego WebInterface</h1>
            <form action="index.php" method="post" enctype="multipart/form-data">
                <input type="file" name="image">
                <select id ="profile" name = "profile">
                    <?php
                        $pdo = new PDO('mysql:host=localhost;dbname=propra_lego', 'propra_lego', 'legodb2020');

                        $profiles = $pdo->query("SELECT profile FROM struct");
                        $num = 1;
                        while($row = $profiles->fetch(PDO::FETCH_ASSOC)) {
                            echo '<option value = "'.$num.'">'.$row['profile'].'</option>';
                            $num = $num + 1;
                        }
                        $pdo = null;
                    ?>
                </select>
                <input type="submit" name="submit" value="Upload">
            </form>
        </div>


        <?php
            if(isset($_POST['submit'])){
                $pdo = new PDO('mysql:host=localhost;dbname=propra_lego', 'propra_lego', 'legodb2020');

                $imgData = file_get_contents($_FILES['image']['tmp_name']);
                $imgType = $_FILES['image']['type'];
                $structID = $_POST['profile'];

                if(substr($imgType,0,5) == 'image'){
                    if(substr($imgType,6,10) == 'jpeg'){
                        $sql = "INSERT INTO struct_img (imgData, struct) VALUES (?,?)";
                        $pdo->prepare($sql)->execute([$imgData, $structID]);
                        echo 'Bild hochgeladen!';
                    }else{
                        echo 'Bild ist kein JPG!';
                    }
                }else{
                    echo 'Datei ist kein Bild!';
                }
            }
        ?>
    </body>

</html>