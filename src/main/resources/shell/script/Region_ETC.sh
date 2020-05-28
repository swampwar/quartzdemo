#!/bin/zsh

##########################################################################################################################################################
#
# 지역정산 세종 카드사입금(청구)[ERP] 작업
#
##########################################################################################################################################################
unmask 002

PRDAY=$(date +%Y%m%d)
BEFORE_PRDAY=$(date +%Y%m%d)

echo "##########################################"
echo "$(date) : ### Region_etc.sh start."

echo"---------- 카드사입금(청구)[ERP] 작업 시작 ----------"
echo "JAB_CreateCardDmdErp $PRDAY"
#JAB_CreateCardDmdErp $PRDAY
sleep 1s # Business Logic
echo"---------- 카드사입금(청구)[ERP] 작업 종료 ----------"

echo"---------- 청구내역 회계파일 [파일->획계] 작업 시작 ----------"
echo "JAB_CreateCardFile $PRDAY"
sleep 1s # Business Logic
echo"---------- 청구내역 회계파일 [파일->획계] 작업 종료 ----------"

echo"---------- 운송대금파일[ERP] 작업 시작 ----------"
echo "JAB_CreateSettmErp $PRDAY"
sleep 1s # Business Logic
echo"---------- 운송대금파일[ERP] 작업 종료 ----------"

echo"---------- 배분내역 회계파일 [파일->회계] 작업 시작 ----------"
echo "JAB_CreateSettmFile $PRDAY"
sleep 1s # Business Logic

echo"---------- 배분내역 회계파일 [파일->회계] 작업 종료 ----------"

echo"---------- 운송대금파일(공항) 작업 시작 ----------"
echo "JAB_CreateLimousineSettmErp $PRDAY"
sleep 1s # Business Logic

echo"---------- 운송대금파일(공항) 작업 종료 ----------"

echo"---------- 배분내역 회계파일(공항) [파일->회계] 작업 시작 ----------"
echo "JAB_CreateLimousineSettmFile $PRDAY"
sleep 1s # Business Logic
echo"---------- 배분내역 회계파일(공항) [파일->회계] 작업 종료 ----------"

echo"---------- 현금계수기 회계 내역 [ERP] 작업 시작 ----------"
sleep 1s # Business Logic
echo"---------- 현금계수기 회계 내역 [ERP] 작업 종료 ----------"

echo"---------- 현금계수기 회계파일 [파일->회계] 작업 시작 ----------"
sleep 1s # Business Logic
echo"---------- 현금계수기 회계파일 [파일->회계] 작업 종료 ----------"

echo "$(date) : ### Region_etc.sh finish."
echo "##########################################"