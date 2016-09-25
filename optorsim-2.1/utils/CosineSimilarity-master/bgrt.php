<?php

require_once 'CosineSimilarity.php';

$v1 = array('f1' => 2, 'f2' => 3,  'f3' => 3,  'f4' => 3,  'f5' => 1);
$v2 = array('f1' => 2, 'f2' => 6,  'f3' => 6,  'f4' => 6,  'f5' => 6);

$v3 = array('f1' => 2, 'f2' => 6,  'f3' => 6,  'f4' => 8,  'f5' => 3);

$v4 = array('f1' => 6, 'f2' => 9,  'f3' => 9,  'f4' => 9,  'f5' => 1);

$v5 = array('f1' => 8, 'f2' => 9,  'f3' => 9,  'f4' => 11,  'f5' => 1);

$v6 = array('f1' => 6, 'f2' => 15,  'f3' => 15,  'f4' => 15,  'f5' => 2);

$v7 = array('f1' => 6, 'f2' => 15,  'f3' => 15,  'f4' => 17,  'f5' => 3);

$v8 = array('f1' => 8, 'f2' => 15,  'f3' => 15,  'f4' => 17,  'f5' => 2);

$v9 = array('f1' => 8, 'f2' => 15,  'f3' => 15,  'f4' => 19,  'f5' => 3);


$cs = new CosineSimilarity();


// -------------------------------------------------------------------
$v1 = array('f1' => 1, 'f2' => 6,  'f3' => 4,  'f4' => 6,  'f5' => 2);
$v2 = array('f1' => 3, 'f2' => 9,  'f3' => 5,  'f4' => 9,  'f5' => 2);
$v3 = array('f1' => 1, 'f2' => 8,  'f3' => 6,  'f4' => 9,  'f5' => 2);
$v4 = array('f1' => 3, 'f2' => 11,  'f3' => 7,  'f4' => 12,  'f5' => 2);

// -------------------------------------------------------------------
$result1 = $cs->similarity($v1,$v2); // similarity of 1 and 2
$result2 = $cs->similarity($v1,$v3); // similarity of 1 and 3
$result3 = $cs->similarity($v1,$v4); // similarity of 1 and 4
$result4 = $cs->similarity($v2,$v3); 
$result5 = $cs->similarity($v2,$v4);
$result6 = $cs->similarity($v3,$v4);


var_dump($result1); 
var_dump($result2); 
var_dump($result3); 
var_dump($result4); 
var_dump($result5); 
var_dump($result6);

/*
$result1 = $cs->similarity($v1,$v2); // similarity of 1 and 2
$result2 = $cs->similarity($v1,$v3); // similarity of 1 and 3
$result3 = $cs->similarity($v1,$v4); // similarity of 1 and 4
$result4 = $cs->similarity($v1,$v5); 
$result5 = $cs->similarity($v1,$v6); 
$result6 = $cs->similarity($v1,$v7); 
$result7 = $cs->similarity($v1,$v8); 
$result8 = $cs->similarity($v1,$v9); 

var_dump($result1); 
var_dump($result2); 
var_dump($result3); 
var_dump($result4); 
var_dump($result5); 
var_dump($result6); 
var_dump($result7); 
var_dump($result8); 
var_dump("***************************************************");
*/

/*
$result2 = $cs->similarity($v2,$v3); // similarity of 2 and 3
$result3 = $cs->similarity($v2,$v4); // similarity of 2 and 4
$result4 = $cs->similarity($v2,$v5); 
$result5 = $cs->similarity($v2,$v6); 
$result6 = $cs->similarity($v2,$v7); 
$result7 = $cs->similarity($v2,$v8); 
$result8 = $cs->similarity($v2,$v9); 

var_dump($result2); 
var_dump($result3); 
var_dump($result4); 
var_dump($result5); 
var_dump($result6); 
var_dump($result7); 
var_dump($result8); 
var_dump("***************************************************");

$result3 = $cs->similarity($v3,$v4); // similarity of 3 and 4
$result4 = $cs->similarity($v3,$v5); 
$result5 = $cs->similarity($v3,$v6); 
$result6 = $cs->similarity($v3,$v7); 
$result7 = $cs->similarity($v3,$v8); 
$result8 = $cs->similarity($v3,$v9); 

var_dump($result3); 
var_dump($result4); 
var_dump($result5); 
var_dump($result6); 
var_dump($result7); 
var_dump($result8); 
var_dump("***************************************************");

$result4 = $cs->similarity($v4,$v5); 
$result5 = $cs->similarity($v4,$v6); 
$result6 = $cs->similarity($v4,$v7); 
$result7 = $cs->similarity($v4,$v8); 
$result8 = $cs->similarity($v4,$v9); 

var_dump($result4); 
var_dump($result5); 
var_dump($result6); 
var_dump($result7); 
var_dump($result8); 
var_dump("***************************************************");

$result5 = $cs->similarity($v5,$v6); 
$result6 = $cs->similarity($v5,$v7); 
$result7 = $cs->similarity($v5,$v8); 
$result8 = $cs->similarity($v5,$v9); 

var_dump($result5); 
var_dump($result6); 
var_dump($result7); 
var_dump($result8); 
var_dump("***************************************************");

$result6 = $cs->similarity($v6,$v7); 
$result7 = $cs->similarity($v6,$v8); 
$result8 = $cs->similarity($v6,$v9); 

var_dump($result6); 
var_dump($result7); 
var_dump($result8); 
var_dump("***************************************************");

$result7 = $cs->similarity($v7,$v8); 
$result8 = $cs->similarity($v7,$v9); 

var_dump($result7); 
var_dump($result8); 
var_dump("***************************************************");

$result8 = $cs->similarity($v8,$v9); 

var_dump($result8); 
var_dump("***************************************************");
*/
?>