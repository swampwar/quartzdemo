#!/bin/zsh

##########################################################################################################################################################
#
# 지역정산 세종 카드사입금(청구)[ERP] 작업
#
##########################################################################################################################################################
unmask 002

SYSTEM_CODE=$1

echo "##########################################"
echo "$(date) : ### Region_ERP_SJ.sh start."
echo "_SYSTEM_CODE[$SYSTEM_CODE]"

echo "JAB_CreateErpfile($SYSTEM_CODE)"
#JAB_UpdateJobStatus($SYSTEM_CODE, $WORK_ID, $WORK_STS)
sleep 1s

echo "$(date) : ### Region_ERP_SJ.sh finish."
echo "##########################################"

