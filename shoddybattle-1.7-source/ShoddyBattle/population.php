<?php

function getServerPopulation($host, $port) {
	$socket = socket_create(AF_INET, SOCK_DGRAM, SOL_UDP);
	if (!is_resource($socket))
		return 0;
	socket_sendto($socket, 'x', 1, 0x100, getHostByName($host), $port);
	socket_recvfrom($socket, $b, 4, 0, $client, $clientPort);
	$s = (ord($b[3]) << 0)
		+ (ord($b[2]) << 8)
		+ (ord($b[1]) << 16)
		+ (ord($b[0]) << 24);
	socket_recvfrom($socket, $b, $s, 0, $client, $clientPort);
	return $b;
}

echo getServerPopulation('colin.shoddybattle.com', 1234) . "\n";

?>
