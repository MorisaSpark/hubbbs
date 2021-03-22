import messageApi from "@/api/message"
import reportApi from "@/api/report"
import {sexList} from "@/utils/publicConst"

export function toTime(timeStamp) {
  let date = new Date(timeStamp * 1);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
  let Y = date.getFullYear() + '-';
  let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
  let D = date.getDate() + ' ';
  let h = date.getHours() + ':';
  let m = date.getMinutes() + ':';
  let s = date.getSeconds();
  return Y + M + D + h + m + s;//时分秒可以根据自己的需求加上

}

export function toTimes(timeStamps, keyName) {
  for (let i = 0; i < timeStamps.length; i++) {
    timeStamps[i][keyName] = toTime(timeStamps[i][keyName]);
  }
  return timeStamps;
}


export function toRealSex(sexInt) {
  for (let i in sexList) {
    if (sexList[i]['id'] === sexInt) {
      return sexList[i]['label']
    }
  }
}

export function messageBtnBridge(btnName, btnType, messageType, messageTable, messageId) {
  return {
    btnName: btnName,
    btnType: btnType,
    messageType: messageType,
    messageTable: messageTable,
    messageId: messageId
  }
}

export  function resMessage(the, res) {
  if (res.data.flag) {
    the.$Message.success(res.data.message);
  } else {
    the.$Message.error(res.data.message);
  }
}
