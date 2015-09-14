#! /bin/sh

sudo iptables -t nat -A OUTPUT -d 127.0.0.2 -p tcp --dport 443 -j REDIRECT --to-port 9000
sudo iptables -t nat -A OUTPUT -d 127.0.0.3 -p tcp --dport 80 -j REDIRECT --to-port 8080
sudo iptables -t nat -A OUTPUT -d 127.0.0.4 -p tcp --dport 80 -j REDIRECT --to-port 8081
