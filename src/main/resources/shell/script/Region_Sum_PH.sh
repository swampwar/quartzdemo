#!/bin/zsh

##########################################################################################################################################################
#
# 지역정산 포항 배분작업
#
##########################################################################################################################################################
unmask 002

SYSTEM_CODE=$1
BEFORE_PRDAY=$(date +%Y%m%d)

echo "##########################################"
echo "$(date) : ### Region_Sum_PH.sh start."

echo "##### 포항 배분 작업 시작 #####"

echo"---------- 배분금액집계 작업 시작 ----------"
echo "JAB_CreateBusSta $BEFORE_PRDAY $SYSTEM_CODE"
#JAB_CreateBusSta $BEFORE_PRDAY $SYSTEM_CODE
sleep 1s # Business Logic
echo"---------- 배분금액집계 작업 종료 ----------"


echo"---------- 버스 사업자별 집계 작업 시작 ----------"
echo "JAB_CreateBusUerTot $BEFORE_PRDAY $SYSTEM_CODE"
#JAB_CreateBusUerTot $BEFORE_PRDAY $SYSTEM_CODE
sleep 1s # Business Logic
echo"---------- 버스 사업자별 집계 작업 종료 ----------"

echo"---------- 사업자별 수입금 확정 작업 시작 ----------"
echo "JAB_CreatePmtAmt $BEFORE_PRDAY $SYSTEM_CODE"
#JAB_CreatePmtAmt $BEFORE_PRDAY $SYSTEM_CODE
sleep 590s # Business Logicss

echo"---------- 사업자별 수입금 확정 작업 종료 ----------"

echo"---------- 사업자별 수수료 작업 시작 ----------"
echo "JAB_CreatePmtFee $BEFORE_PRDAY $SYSTEM_CODE"
#JAB_CreatePmtFee $BEFORE_PRDAY $SYSTEM_CODE
sleep 1s # Business Logic
echo"---------- 사업자별 수수료 작업 종료 ----------"

echo "$(date) : ### Region_Sum_PH.sh finish."
echo "##########################################"