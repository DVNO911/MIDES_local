#gnome-terminal -- /bin/bash -c 'cd formal8;. install/setup.bash; ls'
#TERMPID=$(ps -ef |
#              grep gnome-terminal | grep ROSSIM |
#              head -1 | awk '{ print $2 }')
#kill $TERMPID
#pkill gripper_simulat
#pkill opcua_ros2_brid
xdotool windowactivate --sync $(xdotool search --name "ROSSIM") key --clearmodifiers alt+F4
gnome-terminal --title="ROSSIM" -- /bin/bash -c 'cd ../..; ls; cd formal8;. install/setup.bash; ros2 launch dorna_example test.launch.py'


#echo $! > /var/tmp/ROSSIM.pid
