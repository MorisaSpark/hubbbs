import Cookies from 'js-cookie'

const TokenKey = 'User‐Token';
const RolesKey = 'User‐Roles';
const NameKey = 'User‐Nickname';
const IconKey = 'User‐Icon';
const UserId = 'User‐UserId';

export function setUser(token, roles, nickname, icon, userId) {
  Cookies.set(TokenKey, token);
  Cookies.set(RolesKey, roles);
  Cookies.set(NameKey, nickname);
  Cookies.set(IconKey, icon);
  Cookies.set(UserId, userId)
}

export function setThing(thingName, thingValue) {
  if (thingName === 'token') {
    Cookies.set(TokenKey, thingValue);
  } else if (thingName === 'roles') {
    Cookies.set(RolesKey, thingValue);
  } else if (thingName === 'nickname') {
    Cookies.set(NameKey, thingValue);
  } else if (thingName === 'icon') {
    Cookies.set(IconKey, thingValue);
  } else if (thingName === 'userId') {
    Cookies.set(UserId, thingValue);
  }
}

export function getUser() {
  return {
    token: Cookies.get(TokenKey),
    roles: Cookies.get(RolesKey),
    nickname: Cookies.get(NameKey),
    icon: Cookies.get(IconKey),
    userId: Cookies.get(UserId)
  }
}

export function removeUser() {
  Cookies.remove(TokenKey);
  Cookies.remove(RolesKey);
  Cookies.remove(NameKey);
  Cookies.remove(IconKey);
  Cookies.remove(UserId)
}
