echo "-------------- trigger3.sh START --------------"
echo "시작시간 : " $(date +'%Y/%m/%d %H:%M:%S')

MY_VAR=VALUE1
PARAM1=$1
PARAM2=$2
MY_VAR_ADDED="${MY_VAR} PARAM1:${PARAM1} PARAM2:${PARAM2}"

echo ${MY_VAR_ADDED}
echo "모든 파라미터 : [" "$*" "]" # shell script의 인자 [ 1 2 ]
echo "모든 파라미터 갯수 : " $#
echo "-----------------------------------------------"

exit 0;

