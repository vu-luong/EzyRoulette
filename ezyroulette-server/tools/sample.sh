top -n1 -H | grep -m1 java
PID=$(top -n1 | grep -m1 java | perl -pe 's/\e\[?.*?[\@-~] ?//g' | cut -f1 -d' ')
echo 'pid = ' $PID
NID=$(printf "%x" $(top -n1 -H | grep -m1 java | perl -pe 's/\e\[?.*?[\@-~] ?//g' | cut -f1 -d' '))
echo 'nid = ' $NID $((16#$NID))
if [ -z "$1" ];
then
    jstack $PID | grep -A500 $NID | grep -m1 "^$" -B 500 > ouput/sampling_trace.txt;
else
    jstack $1 $PID | grep -A500 $((16#$NID)) | grep -m1 "^$" -B 500 > ouput/sampling_trace.tx;
fi
