echo 'start kill ezyfox server process'
bash stop-service.sh
echo 'ezyfox server process killed, slepping ...'
sleep 5
echo 'wakeup, starting ezyfox server ...'
bash start-service.sh
echo 'start ezyfox server done done'