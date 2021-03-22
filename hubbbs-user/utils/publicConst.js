import {getUser} from "@/utils/auth";

export const Authorization = (getUser().token === undefined ? "Hello " : "Bearer " + getUser().token);

export const publicTencent = "https://hub-1258341727.cos.ap-guangzhou.myqcloud.com/hubbbs";
export const imgCookiesTrue = "/public/CookiesTrue.svg";
export const imgCookiesFalse = "/public/CookiesFalse.svg";
export const uploadPicUrl = 'http://localhost:9009/upload';

export let sexList = [
  {
    id: 1,
    value: "man",
    label: "男",
  },
  {
    id: 0,
    value: "woman",
    label: "女",
  },
  {
    id: -1,
    value: "what",
    label: "未知",
  }
];

export let linkList = [
  [
    {
      id: 0,
      url: "http://www.baidu.com",
      name: "百度"
    },
    {
      id: 1,
      url: "http://www.baidu.com",
      name: "百度2"
    },
    {
      id: 2,
      url: "http://www.baidu.com",
      name: "百度3"
    },
  ],
  [
    {}, {}, {}
  ],
  [
    {}, {}, {}
  ]
];
