<?php

//use Eloquent\Lcs\LcsSolver;
require_once 'src/LcsSolver.php';

$solver = new LcsSolver;
/*
$sequenceA = array('B', 'A', 'N', 'A', 'N', 'A');
$sequenceB = array('A', 'T', 'A', 'N', 'A');
*/


$sequenceA = array('1', '2');
$sequenceB = array('1', '3');

// calculates the LCS to be array('A', 'A', 'N', 'A')
$lcs = $solver->longestCommonSubsequence($sequenceA, $sequenceB);
var_dump($lcs);
$a=count($lcs);
$b=count($sequenceA);
print("PLSC similiarity of ri, rj: ".$a);
print("<br>");
print("ri cardinal: ".$b);
print("<br>");
print("**************************************");
print("<br>");
print("normalized: ".$a/$b);
print("----------------------------------------------------------------------------------------------------------------");
print("----------------------------------------------------------------------------------------------------------------");

?>