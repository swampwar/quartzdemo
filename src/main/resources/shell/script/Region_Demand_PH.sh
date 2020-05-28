#!/bin/zsh

##########################################################################################################################################################
#
# 지역정산 발행사 청구작업
#
##########################################################################################################################################################
unmask 002

SYSTEM_CODE=$1
MSTOR_GROUP_CD=$2
PRDAY=$(date +%Y%m%d)

echo "##########################################"항
echo "$(date) : ### Region_Demand_PH.sh start."

echo "##### 포항 발행사별 청수 작업 시작 #####"

echo "---------- 국민은행 청구 작업 시작 ----------"
echo "JAB_CreateCreditCardDmdFile $PRDAY 111111 111 $SYSTEM_CODE"
#JAB_CreateCreditCardDmdFile $PRDAY 111111 222 $SYSTEM_CODE
sleep 1s
echo "---------- 국민은행 청구 작업 종료 ----------"

echo "---------- 하나SK카드 청구 작업 시작 ----------"
echo "JAB_CreateCreditCardDmdFile $PRDAY 222222 111 $SYSTEM_CODE"
#JAB_CreateCreditCardDmdFile $PRDAY 222222 222 $SYSTEM_CODE
sleep 1s
echo "---------- 하나SK카드 청구 작업 종료 ----------"

echo "---------- 삼성카드 청구 작업 시작 ----------"
echo "JAB_CreateCreditCardDmdFile $PRDAY 333333 111 $SYSTEM_CODE"
#JAB_CreateCreditCardDmdFile $PRDAY 333333 222 $SYSTEM_CODE
sleep 1s # Business Logic
echo "---------- 삼성카드 청구 작업 종료 ----------"

echo "---------- 신한카드 청구 작업 시작 ----------"
echo "JAB_CreateCreditCardDmdFile $PRDAY 444444 111 $SYSTEM_CODE"
#JAB_CreateCreditCardDmdFile $PRDAY 444444 222 $SYSTEM_CODE
sleep 1s # Business Logic
echo "---------- 신한카드 청구 작업 종료 ----------"


echo "##### 선불 발행사별 청구 작업 시작 #####"

echo "---------- 티머 청구 작업 시작 ----------"
echo "JAB_CreateOurCardDmdFile $PRDAY 101000 111 $SYSTEM_CODE"
#JAB_CreateOurCardDmdFile $PRDAY 101000 222 $SYSTEM_CODE
sleep 1s # Business Logic
echo "---------- 티머니 청구 작업 종료 ----------"

echo "---------- 한페이카드 청구 작업 시작 ----------"
echo "JAB_CreateOurCardDmdFile $PRDAY 111000 111 $SYSTEM_CODE"
#JAB_CreateOurCardDmdFile $PRDAY 111000 222 $SYSTEM_CODE
sleep 1s # Business Logic
echo "---------- 한페이카드 청구 작업 종료 ----------"

echo "---------- EB카드 청구 작업 시작 ----------"
echo "JAB_CreatExPrePasscardDmd $PRDAY 121000 111 $SYSTEM_CODE"
#JAB_CreatExPrePasscardDmd $PRDAY 121000 222 $SYSTEM_CODE
sleep 1s # Business Logic
echo "---------- EB카드 청구 작업 종료 ----------"

echo "$(date) : ### Region_Demand.sh finish."
echo "##########################################"